package com.complete.api.gateway.util;

public class AppMessages {
    /////////////////////////////////////ERROR MESSAGES///////////////////////////////////
    public static final String EMAIL_NOT_FOUND = "Oops! No user found with this email: %s";
    public static final String INVALID_EMAIL = "Oops! Invalid email, please check.";
    public static final String WRONG_ACCOUNT_EMAIL = "Oops! No account with this email, please check.";
    public static final String WRONG_ACCOUNT_PASSWORD = "Oops! Your password is incorrect, please check.";
    public static final String WRONG_CURRENT_PASSWORD = "Oops! Your current password is incorrect, please check.";
    public static final String EMPTY_EMAIL = "Oops! Email cannot be empty, please check.";
    public static final String NULL_EMAIL = "Oops! Email cannot be null, please check.";
    public static final String MAX_NAME_LIMIT_EXCEEDED = "Oops! Your name cannot exceed 100 characters, please check.";
    public static final String NULL_NAME_PARAM = "Oops! Name cannot be null, please check.";
    public static final String EMPTY_NAME_PARAM = "Oops! Name cannot be empty, please check.";
    public static final String NULL_PASSWORD = "Oops! Password cannot be null, please check.";
    public static final String NULL_ROLE_NAME = "Oops! Role name cannot be null, please check.";
    public static final String NULL_ROLE_UUID = "Oops! Role uuid cannot be null, please check.";
    public static final String NULL_PASSWORD_PARAM = "Oops! Password/confirm password cannot be null, please check.";
    public static final String NULL_CURRENT_PASSWORD_PARAM = "Oops! Current password cannot be null, please check.";
    public static final String NULL_PERMISSIONS_PARAM = "Oops! Permissions list cannot be null or empty, please check.";
    public static final String DUPLICATE_ROLE_NAME = "Oops! A role with the same name already exists, please check.";
    public static final String EMPTY_PASSWORD = "Oops! Password cannot be empty, please check.";
    public static final String EMPTY_ROLE_NAME = "Oops! Role name cannot be empty, please check.";
    public static final String EMPTY_ROLE_UUID = "Oops! Role uuid cannot be empty, please check.";
    public static final String EMPTY_PASSWORD_PARAM = "Oops! Password/confirm password cannot be empty, please check.";
    public static final String EMPTY_CURRENT_PASSWORD_PARAM = "Oops! Current password cannot be empty, please check.";
    public static final String MIN_PASSWORD_LENGTH_NOT_REACHED = "Oops! Your password cannot be less than 8 characters, please check.";
    public static final String INVALID_REQUEST_PARAMETERS = "Oops! Your request parameters is either incomplete or invalid, please check.";
    public static final String INVALID_ACCESS_TOKEN = "Oops! Invalid access token, please check.";
    public static final String ACCOUNT_NOT_VERIFIED = "Oops! Your account is not verified. Kindly check for the OTP sent to your registered email or resent a new one to verify your account";
    public static final String INVALID_USER_TYPE = "Oops! Invalid user type. Valid user types are: ADMIN and USER.";
    public static final String PASSWORD_MISMATCH = "Oops! Password mismatch, kindly retype your password.";
    public static final String EMAIL_ALREADY_TAKEN = "Oops! An account with this email already exist in our record, kindly use another email.";
    public static final String ROLE_NOT_FOUND = "Oops! Role %s not found, please check!";
    public static final String INVALID_AUTHORIZATION_TOKEN = "Oops! Invalid authorization, please provide your authorization token in the request header.";
    public static final String NO_USER_FOUND = "Oops! No user found.";
    public static final String NO_ROLES_FOUND = "Oops! No roles found.";
    public static final String NO_PERMISSIONS_FOUND = "Oops! No permissions found.";
    public static final String NO_ROLE_FOUND_WITH_UUID = "Oops! No role found with the uuid provided, please check.";
    public static final String NO_USER_FOUND_WITH_UUID = "Oops! No user found with the uuid provided, please check.";
    public static final String NULL_KEYWORD_PARAM = "Oops! Keyword cannot be null, please check.";
    public static final String NULL_UUID_PARAM = "Oops! UUID cannot be null, please check.";
    /////////////////////////////////////SUCCESS MESSAGES///////////////////////////////////
    public static final String ACCOUNT_CREATED_SUCCESSFULLY = "Awesome! Your account has been created successfully.";
    public static final String SIGN_IN_SUCCESSFULLY = "Awesome! You have signed in successfully.";
    public static final String SIGN_OUT_SUCCESSFULLY = "Awesome! You have signed out successfully.";
    public static final String PROFILE_RETRIEVED_SUCCESSFULLY = "Awesome! User profile retrieved successfully.";
    public static final String USERS_RETRIEVED_SUCCESSFULLY = "Awesome! Users retrieved successfully.";
    public static final String PERMISSIONS_RETRIEVED_SUCCESSFULLY = "Awesome! Permissions retrieved successfully.";
    public static final String ROLES_RETRIEVED_SUCCESSFULLY = "Awesome! Roles retrieved successfully.";
    public static final String ROLE_RETRIEVED_SUCCESSFULLY = "Awesome! Role retrieved successfully.";
    public static final String ROLE_CREATED_SUCCESSFULLY = "Awesome! Role created successfully.";
    public static final String ROLE_UPDATED_SUCCESSFULLY = "Awesome! Role updated successfully.";
    public static final String USERS_DELETED_SUCCESSFULLY = "Awesome! User deleted successfully.";
    public static final String PROFILE_UPDATED_SUCCESSFULLY = "Awesome! User profile updated successfully.";
    public static final String PASSWORD_UPDATED_SUCCESSFULLY = "Awesome! Password updated successfully.";
}
