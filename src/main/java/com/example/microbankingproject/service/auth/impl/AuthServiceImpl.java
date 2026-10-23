package com.example.microbankingproject.service.auth.impl;

import com.example.microbankingproject.dto.auth.AuthenticationDto;
import com.example.microbankingproject.dto.request.*;
import com.example.microbankingproject.entity.UserEntity;
import com.example.microbankingproject.entity.enums.EntranceType;
import com.example.microbankingproject.entity.enums.Roles;
import com.example.microbankingproject.entity.enums.TokenType;
import com.example.microbankingproject.exception.*;
import com.example.microbankingproject.mapper.UserMapper;
import com.example.microbankingproject.repository.UserRepository;
import com.example.microbankingproject.service.auth.AuthService;
import com.example.microbankingproject.service.auth.AuthUserDetailsService;
import com.example.microbankingproject.service.auth.JwtBlacklistService;
import com.example.microbankingproject.service.auth.JwtService;
import com.example.microbankingproject.service.cookie.CookieService;
import com.example.microbankingproject.service.sms.SMSService;
import com.example.microbankingproject.utils.OTPCodeGenerator;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    JwtService jwtService;
    JwtBlacklistService jwtBlacklistService;
    AuthUserDetailsService authUserDetailsService;
    CookieService cookieService;
    SMSService smsService;
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void signUp(UserRegistrationDto userRegistrationDto) {
        log.info("Sign Up Started...");

        String phone = userRegistrationDto.getPhone();
        String keyOTP = phone + ":OTP";
        String keyData = phone + ":Data";

        if (phoneExists(phone)) {
            throw new ApplicationException(CommonErrors.PHONE_ALREADY_REGISTERED);
        }

        String OTP = OTPCodeGenerator.generateCode();
        redisTemplate.opsForValue().set(keyOTP, OTP, 60, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(keyData, userRegistrationDto, 600, TimeUnit.SECONDS);
//        smsService.sendOTP(phone, OTP);

        log.info("Sign Up Ended");
    }

    @Override
    public void loginPhone(LoginPhoneDto loginPhoneDto) {
        log.info("Login Phone Started...");

        String phone = loginPhoneDto.getPhone();
        UserDetails account = getUserOrAgentByPhone(phone);
        String keyOTP = phone + ":OTP";

        if (account == null) {
            throw new ApplicationException(CommonErrors.ACCOUNT_NOT_FOUND);
        }

        String OTP = OTPCodeGenerator.generateCode();
        redisTemplate.opsForValue().set(keyOTP, OTP, 60, TimeUnit.SECONDS);
//        smsService.sendOTP(phone, OTP);

        log.info("Login Phone Ended");
    }

    @Override
    @Transactional
    public AuthenticationDto verifyOTP(OTPRequestDto otpRequestDto, HttpServletResponse response) {
        log.info("Verify OTP Started...");

        String phone = otpRequestDto.getPhone();
        String keyOTP = phone + ":OTP";
        String keyData = phone + ":Data";

        String inputOTP = otpRequestDto.getOTP();
        String cachedOTP = (String) redisTemplate.opsForValue().get(keyOTP);
        Long TTL = redisTemplate.getExpire(keyOTP, TimeUnit.SECONDS);

        if (expiryOTP(TTL)) {
            throw new ApplicationException(CommonErrors.OTP_CODE_EXPIRED);
        }

        if (checkOTP(cachedOTP, inputOTP)) {
            throw new ApplicationException(CommonErrors.OTP_CODE_INCORRECT);
        }

        UserDetails account;
        if (EntranceType.SIGNUP.equals(otpRequestDto.getEntranceType())) {
            UserRegistrationDto userRegistrationDto = (UserRegistrationDto) redisTemplate.
                    opsForValue().get(keyData);
            account = userMapper.
                    mapUserRegistrationDtoToEntity(userRegistrationDto);
            setRolesAndSave((UserEntity) account);

            redisTemplate.delete(keyOTP);
            redisTemplate.delete(keyData);
        } else {
            account = getUserOrAgentByPhone(phone);
            redisTemplate.delete(keyOTP);
        }

        String refreshToken = generateRefreshToken(account);
        generateCookie(refreshToken, response);
        return generateAccessToken(account);
    }

    @Override
    public void resendOTP(ResendOTPRequestDto resendOTPRequestDto) {
        log.info("Resend OTP Started...");

        String phone = resendOTPRequestDto.getPhone();
        String keyOTP = phone + ":OTP";
        String keyData = phone + ":Data";
        String OTP = OTPCodeGenerator.generateCode();

        if (EntranceType.SIGNUP.equals(resendOTPRequestDto.getEntranceType())) {
            UserRegistrationDto userRegistrationDto = (UserRegistrationDto) redisTemplate.opsForValue().get(keyData);
            redisTemplate.opsForValue().set(keyData, userRegistrationDto, 600, TimeUnit.SECONDS);
        }

        redisTemplate.opsForValue().set(keyOTP, OTP, 60, TimeUnit.SECONDS);
//        smsService.sendOTP(phone, OTP);

        log.info("Resend OTP Ended");
    }

    @Override
    public AuthenticationDto refreshToken(AccessTokenRequestDto accessTokenRequestDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        log.info("Refresh Token Started...");

        String accessToken = accessTokenRequestDto.getAccessToken();
        jwtBlacklistService.addBlacklist(accessToken, 19L);

        String refreshToken = getRefreshToken(request);
        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = authUserDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(refreshToken, userDetails) &&
                    !jwtBlacklistService.isBlacklisted(refreshToken)) {
                return generateAccessToken(userDetails);
            }
        } catch (ExpiredJwtException exception) {
            log.info("JWT Expired: " + exception.getMessage());
        }

        jwtBlacklistService.addBlacklist(refreshToken, 43200L);
        deleteCookie(response);

        throw new ApplicationException(CommonErrors.INVALID_TOKENS);
    }

    @Override
    public void logout(AccessTokenRequestDto accessTokenRequestDto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        log.info("Log Out Started...");

        String accessToken = accessTokenRequestDto.getAccessToken();
        String refreshToken = getRefreshToken(request);
        jwtBlacklistService.addBlacklist(accessToken, 19L);
        jwtBlacklistService.addBlacklist(refreshToken, 43200L);
        deleteCookie(response);

        log.info("Log Out Ended");
    }

    @Override
    public UserDetails getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserEntity) {
            return (UserEntity) authentication.getPrincipal();
        }
        return (AgentEntity) authentication.getPrincipal();
    }

    private boolean phoneExists(String phone) {
        return userRepository.findByPhone(phone) != null || agentRepository.findByPhone(phone) != null;
    }

    private boolean checkOTP(String cachedOTP, String OTP) {
        return cachedOTP != null && !cachedOTP.equals(OTP);
    }

    private boolean expiryOTP(Long TTL) {
        return TTL == null || TTL < 0;
    }

    private UserEntity createUser(String name, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFullName(name);
        userEntity.setEmail(email);
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ROLE_USER);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
        return userEntity;
    }

    private void setRolesAndSave(UserEntity userEntity) {
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.ROLE_USER);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
    }

    private UserDetails getUserOrAgentByPhone(String phone) {
        UserDetails account = Optional
                .ofNullable(userRepository.findByPhoneWithRoles(phone))
                .map(a -> (UserDetails) a)
                .orElseGet(() -> agentRepository.findByPhoneWithRoles(phone));
        return account;
    }

    private UserDetails getUserOrAgentByEmail(String email) {
        UserDetails account = Optional
                .ofNullable(userRepository.findByEmailWithRoles(email))
                .map(a -> (UserDetails) a)
                .orElseGet(() -> agentRepository.findByEmailWithRoles(email));
        return account;
    }

    private AuthenticationDto generateAccessToken(UserDetails user) {
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS);
        return AuthenticationDto.
                builder().
                accessToken(accessToken).
                build();
    }

    private String generateRefreshToken(UserDetails user) {
        return jwtService.generateToken(user, TokenType.REFRESH);
    }

    private void generateCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie cookie = cookieService.generateCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void deleteCookie(HttpServletResponse response) {
        ResponseCookie cookie = cookieService.deleteCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String getRefreshToken(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refresh_token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
