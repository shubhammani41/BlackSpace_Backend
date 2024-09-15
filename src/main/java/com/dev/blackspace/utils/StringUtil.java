package com.dev.blackspace.utils;

import com.dev.blackspace.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
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

    public static boolean verifyDomain(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            // Verify if the host matches the expected domain
            return AppConstants.VALID_DOMAIN.equalsIgnoreCase(host);
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
            return false;
        }
    }
}
