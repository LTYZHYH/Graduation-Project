package team.j2e8.findcateserver.exceptions;

import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
//自定义的异常
public class ConflictException extends DataIntegrityViolationException {

    private ErrorCode errorCode;
    private ErrorMessage errorMessage;

    private String argument;

    public ConflictException(ErrorCode errorCode, ErrorMessage errorMessage) {
        super("");
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ConflictException(ErrorCode errorCode, ErrorMessage errorMessage, String argument) {
        super("");
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.argument = argument;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

    public String getErrorMessage() {
        if (argument != null) {
            return argument + errorMessage.getMessage();
        }
        return errorMessage.getMessage();
    }
}
