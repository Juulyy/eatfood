package com.eat.controllers.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Removed action denied")
public class RemoveActionDeniedException extends RuntimeException {

    private static final long serialVersionUID = -5332838683414809011L;

    public RemoveActionDeniedException() {
        super("RemoveActionDeniedException");
    }

}