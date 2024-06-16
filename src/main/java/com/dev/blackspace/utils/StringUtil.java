package com.dev.blackspace.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
