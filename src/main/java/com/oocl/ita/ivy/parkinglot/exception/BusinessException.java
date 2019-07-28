package com.oocl.ita.ivy.parkinglot.exception;

import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private BusinessExceptionType type;

    public BusinessException(BusinessExceptionType type) {
        super(type.getDescription());
        this.type = type;
    }

    public BusinessException(BusinessExceptionType type, String message) {
        super(message);
        this.type = type;
    }
}
