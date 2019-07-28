package com.oocl.ita.ivy.parkinglot.exception;

import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponseEntity {
    private Date timestamp;
    private HttpStatus status;
    private String error;
    private String message;
    private int errorCode;
    private String trace;
    private String errorType;

    static ExceptionResponseEntity convert(BusinessException ex) {
        ExceptionResponseEntity exceptionResponseEntity = new ExceptionResponseEntity();
        exceptionResponseEntity.timestamp = new Date();
        exceptionResponseEntity.status = HttpStatus.BAD_REQUEST;
        exceptionResponseEntity.error = HttpStatus.BAD_REQUEST.getReasonPhrase();
        exceptionResponseEntity.message = ex.getMessage();
        exceptionResponseEntity.errorCode = ex.getType().getId();
        exceptionResponseEntity.errorType = ex.getType().getName();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement :
                stackTraceElements) {
            stringBuilder.append(stackTraceElement.toString()).append("\n");
        }
        exceptionResponseEntity.trace = stringBuilder.toString();
        return exceptionResponseEntity;
    }
}
