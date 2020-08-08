package team.j2e8.findcateserver.controllers;

import team.j2e8.findcateserver.exceptions.*;
import team.j2e8.findcateserver.valueObjects.ErrorCode;
import team.j2e8.findcateserver.valueObjects.ErrorMessage;
import team.j2e8.findcateserver.utils.ErrorMessageBuilder;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
//全局的异常处理，在services丢出异常时，就会在这里进行处理，返回相应的错误提醒数据
//ExceptionHandler中指定改函数所监听的是哪个异常
//HttpStatus表示返回的httpCode,有500,400,401等
@ControllerAdvice
public class BusinessExceptionController {
    @Autowired
    private ErrorMessageBuilder errorMessageBuilder;
    // 异常统一处理


    @ResponseBody
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<?> handleBizExp(UnAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessageBuilder
                .build(HttpStatus.UNAUTHORIZED.value(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ResponseBody
    @ExceptionHandler(UnProcessableException.class)
    public ResponseEntity<?> handleBizExp(UnProcessableException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessageBuilder
                .build(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getErrorCode(), ex.getErrorMessage()));
    }


    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleBizExp(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageBuilder
                .build(HttpStatus.NOT_FOUND.value(), ex.getErrorCode(), ex.getErrorMessage()));
    }


    @ResponseBody
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleBizExp(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessageBuilder
                .build(HttpStatus.UNAUTHORIZED.value(), ErrorCode.INVALID_TOKEN.getCode(),
                        ErrorMessage.INVALID_TOKEN.getMessage()));
    }


    @ResponseBody
    @ExceptionHandler(ArgumentErrorException.class)
    public ResponseEntity<?> handleBizExp(ArgumentErrorException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessageBuilder
                .build(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleBizExp(Exception ex) {
        Throwable throwable = new Throwable();
        if (ex.getCause() != null && ex.getCause().getCause() != null) {
            throwable = ex.getCause().getCause();
        }
        if (throwable == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageBuilder
                    .build(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "很抱歉，服务器发生了错误，我们已对此错误进行了记录，并会尽快处理"));
        }

        if (throwable instanceof ConstraintViolationException) {
            StringBuilder stringBuilder = new StringBuilder();
            ((ConstraintViolationException) throwable).getConstraintViolations().forEach(constraintViolation -> stringBuilder.append(constraintViolation.getMessage()).append(";"));

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageBuilder
                    .build(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), stringBuilder.toString()));
        }

        if (ex instanceof ObjectOptimisticLockingFailureException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageBuilder
                    .build(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "当前数据已被其他人修改，请重试"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageBuilder
                .build(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "很抱歉，服务器发生了错误，我们已对此错误进行了记录，并会尽快处理"));
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleBizExp(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessageBuilder
                .build(HttpStatus.CONFLICT.value(), ErrorCode.DATA_CONFLICT.getCode(), ErrorMessage.DATA_CONFLICT.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    public ResponseEntity<?> handleBizExp(JpaObjectRetrievalFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessageBuilder
                .build(HttpStatus.CONFLICT.value(), ErrorCode.DATA_CONFLICT.getCode(), ErrorMessage.DATA_CONFLICT.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleBizExp(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessageBuilder
                .build(HttpStatus.CONFLICT.value(), ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleBizExp(ConstraintViolationException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        ex.getConstraintViolations().forEach(constraintViolation -> stringBuilder.append(constraintViolation.getMessage()).append(";"));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessageBuilder
                .build(HttpStatus.CONFLICT.value(), ex.getLocalizedMessage(), stringBuilder.toString()));
    }

    @ResponseBody
    @ExceptionHandler(InvalidObjectSelectionException.class)
    public ResponseEntity<?> handleBizExp(InvalidObjectSelectionException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessageBuilder
                .build(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getErrorCode(), ex.getErrorMessage()));
    }
}
