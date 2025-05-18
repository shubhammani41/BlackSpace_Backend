package com.dev.blackspace.DTOs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostDetailsDTOMapper {
    Logger logger = LoggerFactory.getLogger(PostDetailsDTOMapper.class);

    @Mapping(source = "postContents", target = "postContents", qualifiedByName = "mapJsonToObject")
    @Mapping(source = "postHashtags", target = "postHashtags", qualifiedByName = "mapJsonToObject")
    @Mapping(source = "postViews", target = "postViews", qualifiedByName = "mapJsonToObject")
    PostDetailsDTO toPostDetailsDTO(PostDetailsProj postDetailsProj);
    List<PostDetailsDTO> toPostDetailsDTOList(List<PostDetailsProj> postDetailsProjList);

    @Named("mapJsonToObject")
    default Object mapJsonToObject(String jsonString) {
        if (jsonString == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Object.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON string in PostDetailsDTOMapper: {}", jsonString, e);
            return null;
        }
    }
}
