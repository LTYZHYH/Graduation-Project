package team.j2e8.findcateserver.valueObjects;

public enum ErrorCode {
    INVALID_TOKEN("invalid_token"),
    ENTITY_ALREADY_EXIST("entity_already_exist"),
    DATA_CONFLICT("data_conflict"),
    REQUIRED_ARGUMENT_ERROR("required_argument_error"),
    RESOURCE_NOT_FOUND("resource_not_found"),
    UNPROCESSABLE_ENTITY("unprocessable_entity"),
    UNAUTHORIZED_ACCESS("unauthorized_access"),
    INVALID_OBJECT_SELECTION("invalid_object_selection");
    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
    }
