package team.j2e8.findcateserver.exceptions;

import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
import org.springframework.stereotype.Service;
//自定义的异常
@Service
public class UnAuthorizedException extends BusinessException {
    protected ErrorCode errorCode;
    protected ErrorMessage errorMessage;
    private String customMessage;

    public UnAuthorizedException() {
        super();
    }

    public UnAuthorizedException(ErrorMessage errorMessage) {
        this.errorCode = ErrorCode.UNAUTHORIZED_ACCESS;
        this.errorMessage = errorMessage;
    }

    public UnAuthorizedException(String message) {
        this.errorCode = ErrorCode.UNAUTHORIZED_ACCESS;
        this.customMessage = message;
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
