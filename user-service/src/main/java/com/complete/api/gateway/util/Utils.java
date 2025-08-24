package com.complete.api.gateway.util;

import com.complete.api.gateway.enums.UserType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.validator.routines.EmailValidator;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static com.complete.api.gateway.util.UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter;


public class Utils {

    public static Gson getGson() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, getUnixEpochDateTypeAdapter())
                .create();

        return gson;
    }

    static EmailValidator validator = EmailValidator.getInstance();

    public static Boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        if (!pat.matcher(email).matches()) {
            return false;
        } else {
            return validator.isValid(email);
        }
    }

    public static Boolean isValidUserType(UserType userType) {
        if (Objects.isNull(userType)) {
            return false;
        }
        for (UserType myEnum : UserType.values()) {
            if (myEnum.label.equals(userType.label)) {
                return true;
            }
        }
        return false;
    }

    public static String convertLocalDateTimeToString(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                .format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static LocalDateTime convertDateStringToLocalDateTime(String date) {
        if (Objects.isNull(date)) {
            return null;
        }
        try {
            if (date.length() <= 10) {
                date += " 00:00:00";
            }
            return LocalDateTime
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC+1"))
                    .toLocalDateTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
