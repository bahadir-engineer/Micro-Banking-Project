//package com.example.microbankingproject.config;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Bucket4j;
//import io.github.bucket4j.Refill;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class RateLimitingFilter implements Filter {
//
//    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
//
//    private Bucket createNewBucket() {
//        Refill refill = Refill.of(15, Duration.ofMinutes(1));
//        return Bucket4j.
//                builder().
//                addLimit(Bandwidth.classic(15, refill)).
//                build();
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        String IP = req.getRemoteAddr();
//
//        Bucket bucket = buckets.computeIfAbsent(IP, k -> createNewBucket());
//
//        if (bucket.tryConsume(1)) {
//            chain.doFilter(request, response);
//        } else {
//            res.setStatus(429);
//            res.getWriter().write("Request count exceeded !");
//        }
//    }
//}
