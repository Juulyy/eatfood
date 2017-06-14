package com.eat.controllers.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such data")
public class DataNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5332838683414809011L;

    public DataNotFoundException() {
        super("DataNotFoundException!");
    }

    public DataNotFoundException(Long id) {
        super("DataNotFoundException with id=" + id);
    }

}