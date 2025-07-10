package com.roze.smarthr.constant;

public class GlobalMessage {
    public static final String CREATE_SUCCESS = "Created successfully";
    public static final String READ_SUCCESS = "Retrieved successfully";
    public static final String UPDATE_SUCCESS = "Updated successfully";
    public static final String DELETE_SUCCESS = "Deleted successfully";
    public static final String OPERATION_SUCCESS = "Operation completed successfully";

    // Authentication
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String PASSWORD_CHANGE_SUCCESS = "Password changed successfully";

    // File Operations
    public static final String UPLOAD_SUCCESS = "File uploaded successfully";
    public static final String DOWNLOAD_SUCCESS = "File downloaded successfully";

    // System
    public static final String CONFIGURATION_SUCCESS = "Configuration updated successfully";
    public static final String CACHE_CLEAR_SUCCESS = "Cache cleared successfully";

    // ========== FAILURE MESSAGES ==========
    public static final String NOT_FOUND = "Requested resource not found";
    public static final String ALREADY_EXISTS = "Resource already exists";
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Access denied";
    public static final String INTERNAL_ERROR = "Internal server error";

    // Authentication
    public static final String LOGIN_FAILED = "Invalid credentials";
    public static final String ACCOUNT_LOCKED = "Account is locked";
    public static final String SESSION_EXPIRED = "Session expired";

    // File Operations
    public static final String FILE_TOO_LARGE = "File size exceeds limit";
    public static final String INVALID_FILE_TYPE = "Invalid file type";

    // Data
    public static final String DATA_INTEGRITY_VIOLATION = "Data integrity violation";
    public static final String CONCURRENT_MODIFICATION = "Data was modified by another process";

    // ========== VALIDATION MESSAGES ==========
    public static final String FIELD_REQUIRED = "This field is required";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_DATE = "Invalid date format";
    public static final String INVALID_RANGE = "Value out of acceptable range";

    // ========== BUSINESS RULE MESSAGES ==========
    public static final String BUSINESS_RULE_VIOLATION = "Operation violates business rules";
    public static final String LIMIT_EXCEEDED = "Maximum limit reached";
    public static final String DEPENDENCY_EXISTS = "Cannot perform operation due to existing dependencies";

    // ========== SYSTEM MESSAGES ==========
    public static final String MAINTENANCE_MODE = "System under maintenance";
    public static final String SERVICE_UNAVAILABLE = "Service temporarily unavailable";
}
