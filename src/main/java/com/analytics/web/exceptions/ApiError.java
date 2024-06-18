package com.analytics.web.exceptions;

import com.analytics.web.utils.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {
    private ErrorCode errorCode;
    private Integer status;
    private String message;
}
