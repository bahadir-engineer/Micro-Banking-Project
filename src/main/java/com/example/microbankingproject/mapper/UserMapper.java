package com.example.microbankingproject.mapper;

import com.example.microbankingproject.dto.request.UserRegistrationDto;
import com.example.microbankingproject.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phone", source = "phone")
    UserEntity mapUserRegistrationDtoToEntity(UserRegistrationDto userRegistrationDto);
}
