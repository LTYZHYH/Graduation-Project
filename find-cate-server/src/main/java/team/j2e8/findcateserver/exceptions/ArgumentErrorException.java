package team.j2e8.findcateserver.exceptions;

import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
//自定义的异常
public class ArgumentErrorException extends BusinessException {
    private ErrorCode errorCode;
    private ErrorMessage errorMessage;
    private String argument;

    public ArgumentErrorException(ErrorCode errorCode, ErrorMessage errorMessage, String argument) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.argument = argument;
    }

    public ArgumentErrorException(String argument) {
        this.errorCode = ErrorCode.REQUIRED_ARGUMENT_ERROR;
        this.errorMessage = ErrorMessage.REQUIRED_ARGUMENT_ERROR;
        this.argument = argument;
    }

    @Override
    public String getErrorCode() {
        return errorCode.getCode();
    }

    @Override
    public String getErrorMessage() {
        if (argument != null && !argument.trim().isEmpty()) {
            return argument;
        }
        return errorMessage.getMessage();
    }
}
