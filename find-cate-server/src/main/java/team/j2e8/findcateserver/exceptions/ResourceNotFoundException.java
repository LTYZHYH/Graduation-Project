package team.j2e8.findcateserver.exceptions;

import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
public class ResourceNotFoundException extends BusinessException {
    private ErrorCode errorCode;
    private ErrorMessage errorMessage;
    private String customMessage;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(ErrorMessage errorMessage) {
        this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        this.errorMessage = errorMessage;
    }

    public ResourceNotFoundException(String customMessage) {
        this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        this.customMessage = customMessage;
    }

    public static ResourceNotFoundException getCustomInstance(String customMessage) {
        return new ResourceNotFoundException(customMessage);
    }

    @Override
    public String getErrorCode() {
        return errorCode.getCode();
    }

    @Override
    public String getErrorMessage() {
        if (customMessage == null) {
            return errorMessage.getMessage();
        }
        return customMessage;
    }
}