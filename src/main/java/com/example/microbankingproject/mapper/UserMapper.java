package com.example.microbankingproject.mapper;

import com.example.investhome.dto.request.UserRegistrationDto;
import com.example.investhome.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phone", source = "phone")
    UserEntity mapUserRegistrationDtoToEntity(UserRegistrationDto userRegistrationDto);
}
