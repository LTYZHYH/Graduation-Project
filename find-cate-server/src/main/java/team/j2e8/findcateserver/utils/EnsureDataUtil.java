package team.j2e8.findcateserver.utils;

import team.j2e8.findcateserver.exceptions.ArgumentErrorException;
import team.j2e8.findcateserver.exceptions.ConflictException;
import team.j2e8.findcateserver.exceptions.ResourceNotFoundException;
import team.j2e8.findcateserver.exceptions.UnProcessableException;
import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;

public class EnsureDataUtil {
    //验证数据是否为空
    public static void ensureNotEmptyData(Object data, String errorMessage) {
        if (data == null) {
            throw new UnProcessableException(errorMessage);
        } else if (data instanceof String && ((String) data).trim().isEmpty()) {
            throw new UnProcessableException(errorMessage);
        }
    }

    public static void ensureCanFoundData(Object data, String errorMessage) {
        if (data == null) {
            throw new ResourceNotFoundException(errorMessage);
        }
    }

    public static void ensureSelectString(String select) {
        if (select.trim().equals("")) {
            throw new ArgumentErrorException(ErrorCode.REQUIRED_ARGUMENT_ERROR, ErrorMessage.REQUIRED_ARGUMENT_ERROR, "select");
        }
    }

    public static void ensureUnique(Object object, String errorMessage) {
        if (object != null) {
            throw new ConflictException(ErrorCode.ENTITY_ALREADY_EXIST,
                    ErrorMessage.ENTITY_ALREADY_EXIST, errorMessage);
        }
    }
}
