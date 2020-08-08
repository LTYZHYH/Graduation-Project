package team.j2e8.findcateserver.exceptions;

import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
//自定义的异常
public class InvalidObjectSelectionException extends BusinessException{

    public InvalidObjectSelectionException(){
        this.errorCode = ErrorCode.INVALID_OBJECT_SELECTION;
        this.errorMessage = ErrorMessage.INVALID_OBJECT_SELECTION;
    }

    public InvalidObjectSelectionException(String select){
        this();
        this.select = select;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    private String select;
}
