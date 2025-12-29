package com.animalphidia.My_backend.util;

public class ApplicationConstants {

    // API Version
    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 12;
    public static final int MAX_PAGE_SIZE = 100;

    // File Upload
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    // Security
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 604800000; // 7 days

    // User Roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_CONTRIBUTOR = "ROLE_CONTRIBUTOR";
    public static final String ROLE_VIEWER = "ROLE_VIEWER";

    // Animal Status
    public static final String ANIMAL_STATUS_PENDING = "PENDING";
    public static final String ANIMAL_STATUS_VERIFIED = "VERIFIED";
    public static final String ANIMAL_STATUS_ACTIVE = "ACTIVE";
    public static final String ANIMAL_STATUS_INACTIVE = "INACTIVE";

    // Conservation Status
    public static final String CONSERVATION_EXTINCT = "Extinct";
    public static final String CONSERVATION_CRITICALLY_ENDANGERED = "Critically Endangered";
    public static final String CONSERVATION_ENDANGERED = "Endangered";
    public static final String CONSERVATION_VULNERABLE = "Vulnerable";
    public static final String CONSERVATION_NEAR_THREATENED = "Near Threatened";
    public static final String CONSERVATION_LEAST_CONCERN = "Least Concern";

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    // Cache
    public static final String CACHE_ANIMALS = "animals";
    public static final String CACHE_SPECIES = "species";
    public static final String CACHE_USERS = "users";
    public static final long CACHE_TTL = 3600; // 1 hour in seconds

    // Email Templates
    public static final String EMAIL_VERIFICATION_SUBJECT = "Verify Your Animalphidia Account";
    public static final String PASSWORD_RESET_SUBJECT = "Reset Your Animalphidia Password";
    public static final String WELCOME_EMAIL_SUBJECT = "Welcome to Animalphidia!";

    // Response Messages
    public static final String MSG_SUCCESS = "Success";
    public static final String MSG_ERROR = "Error";
    public static final String MSG_NOT_FOUND = "Resource not found";
    public static final String MSG_UNAUTHORIZED = "Unauthorized access";
    public static final String MSG_FORBIDDEN = "Access denied";
    public static final String MSG_VALIDATION_ERROR = "Validation error";
    public static final String MSG_SERVER_ERROR = "Internal server error";
}