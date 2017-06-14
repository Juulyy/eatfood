package com.eat.controllers.exceptions;

import com.google.common.base.Throwables;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError invalidUser(HttpServletRequest req, ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return ApiError.create().message(Throwables.getRootCause(ex).getLocalizedMessage()).status(HttpStatus.BAD_REQUEST).build();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid data sent to server")
    public void notValid() {
        System.out.println("Exception catched!");
    }

    @Getter
    @Setter
    @Builder(builderMethodName = "create")
    public static class ApiError {

        private HttpStatus status;
        private String message;
        private List<String> errors;

    }
}
