package com.dev.blackspace.DTOs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDetailsDTOMapper {
    @Mapping(source = "skills", target = "skills", qualifiedByName = "mapJsonToObject")
    @Mapping(source = "userExperience", target = "userExperience", qualifiedByName = "mapJsonToObject")
    @Mapping(source = "userExperience", target = "experience", qualifiedByName = "calculateExperience")
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

    @Named("calculateExperience")
    default double calculateExperience(String jsonString) {
        if (jsonString == null) return 0;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Object obj = objectMapper.readValue(jsonString, Object.class);
            JsonNode experiences = objectMapper.readTree(jsonString);
            return calculateTotalExperience(experiences);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static double calculateTotalExperience(JsonNode experiences) {
        double totalExperience = 0.0;
        LocalDate currentDate = LocalDate.now();
        for (JsonNode experience : experiences) {
            LocalDate fromDate = experience.get("fromDate") != null && !experience.get("fromDate").isNull()
                    ? LocalDate.parse(experience.get("fromDate").asText())
                    : null;

            LocalDate toDate = experience.get("toDate") != null && !experience.get("toDate").isNull()
                    ? LocalDate.parse(experience.get("toDate").asText())
                    : null;
            int isCurrentOrganization = experience.get("isCurrentOrganization").asInt();
            if (fromDate == null) {
                continue;
            }
            if (isCurrentOrganization == 1 && fromDate.isBefore(currentDate)) {
                totalExperience += ChronoUnit.DAYS.between(fromDate, currentDate) / 365.0;
            } else if (isCurrentOrganization == 0 && toDate != null) {
                totalExperience += ChronoUnit.DAYS.between(fromDate, toDate) / 365.0;
            }
        }
        return totalExperience;
    }
}

