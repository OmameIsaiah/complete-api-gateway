package com.complete.api.gateway.util;

public class EndpointsURL {
    public static final String ONBOARDING_BASE_URL = "/api/v1/users/onboarding";
    public static final String ONBOARDING_SIGNUP = "/signup";

    public static final String ENTRANCE_BASE_URL = "/api/v1/users/entrance";
    public static final String ENTRANCE_SIGNIN = "/signin";

    public static final String PROFILE_BASE_URL = "/api/v1/users/profile";
    public static final String PROFILE_INFO = "";
    public static final String PROFILE_UPDATE_INFO = "/update";
    public static final String PROFILE_UPDATE_PASSWORD = "/update-password";
    public static final String PROFILE_SIGNOUT = "/signout";

    public static final String USER_MANAGEMENT_BASE_URL = "/api/v1/users/management";
    public static final String USER_MANAGEMENT_FIND_ALL = "/find-all";
    public static final String USER_MANAGEMENT_FILTER = "/filter";
    public static final String USER_MANAGEMENT_SEARCH = "/search";
    public static final String USER_MANAGEMENT_DELETE = "/delete/{uuid}";

    public static final String ROLES_BASE_URL = "/api/v1/roles";
    public static final String VIEW_PERMISSIONS = "/permissions";
    public static final String ADD_NEW_ROLE = "";
    public static final String UPDATE_ROLE = "";
    public static final String VIEW_ALL_ROLES = "";
    public static final String VIEW_ROLE_BY_UUID = "/{uuid}";
}
