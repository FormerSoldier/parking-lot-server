package com.oocl.ita.ivy.parkinglot.exception;

import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;

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

    public BusinessExceptionType getType() {
        return type;
    }

    public void setType(BusinessExceptionType type) {
        this.type = type;
    }
}
