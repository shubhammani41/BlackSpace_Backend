package com.dev.blackspace.DTOs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDetailsDTOMapper {
    @Mapping(source = "skills", target = "skills", qualifiedByName = "mapJsonToObject")
    @Mapping(source = "userExperience", target = "userExperience", qualifiedByName = "mapJsonToObject")
    UserDetailsDTO toUserDetailsDTO(UserDetailsProj userDetailsProj);
    List<UserDetailsDTO> toUserDetailsDTOList(List<UserDetailsProj> userDetailsProjList);

    @Named("mapJsonToObject")
    default Object mapJsonToObject(String jsonString) {
        if (jsonString == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

