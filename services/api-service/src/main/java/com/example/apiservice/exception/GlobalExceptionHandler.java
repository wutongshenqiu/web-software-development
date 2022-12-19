package com.example.apiservice.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.example.apiservice.domain.dto.ExceptionDto;
import com.example.apiservice.domain.dto.ResponseDto;
import com.example.apiservice.exception.form.QueryFormException;
import com.example.apiservice.exception.form.SubmitFormException;
import com.example.apiservice.exception.group.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                new ResponseDto()
                        .setCode(510001)
                        .setMessage("参数类型、格式不符合要求")
                        .setData(errors),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<?> notLoginException(Exception ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setMessage("token验证失败（过期或无效）")
                        .setCode(511001),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setMessage(ex.getMessage())
                        .setCode(ex.getCode()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CreateGroupException.class)
    public ResponseEntity<?> createGroupException(CreateGroupException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(DeleteGroupException.class)
    public ResponseEntity<?> deleteGroupException(DeleteGroupException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(JoinGroupException.class)
    public ResponseEntity<?> joinGroupException(JoinGroupException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(QuitGroupException.class)
    public ResponseEntity<?> quitGroupException(QuitGroupException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(QueryGroupException.class)
    public ResponseEntity<?> queryGroupException(QueryGroupException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(TransferGroupException.class)
    public ResponseEntity<?> transferGroupException(TransferGroupException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(SubmitFormException.class)
    public ResponseEntity<?> createFormException(SubmitFormException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(QueryFormException.class)
    public ResponseEntity<?> queryFormException(QueryFormException ex) {
        return new ResponseEntity<>(
                new ExceptionDto()
                        .setCode(ex.getCode())
                        .setMessage(ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }
}
