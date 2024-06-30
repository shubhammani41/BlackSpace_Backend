package com.dev.blackspace.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StringUtil {

    public String escapeAndLowercase(String input) {
       try{
           String trimmedInput = input.trim();
           String escapedInput = trimmedInput
                   .replaceAll("([\\\\%_])", "\\\\$1")
                   .replaceAll("'", "''");

           escapedInput = escapedInput.replaceAll("\\+", "\\\\+");

           return escapedInput.toLowerCase();
       }
       catch (Exception e){
           log.error("Inside escapeAndLowercase() catch block:::{}",e);
           return "";
       }
    }

    public String getSearchRegex(String searchKeyWord) {
        return Arrays.stream(searchKeyWord.split("[,\\s]+")) // Split by comma or whitespace
                .map(keyword -> "(?i)" + this.escapeAndLowercase(keyword.trim()))
                .collect(Collectors.joining("|"));
    }
}
