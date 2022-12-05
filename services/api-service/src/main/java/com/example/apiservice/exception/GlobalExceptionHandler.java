package com.example.apiservice.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.example.apiservice.domain.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常
    @ExceptionHandler
    public ResponseEntity<?> globalException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setMessage(ex.getMessage())
                        .setCode(500),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<?> notLoginException(Exception ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setMessage(ex.getMessage())
                        .setCode(403),
                HttpStatus.FORBIDDEN
        );
    }

}
