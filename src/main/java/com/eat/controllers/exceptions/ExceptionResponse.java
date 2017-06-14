package com.eat.controllers.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = -1455697708325593458L;
    private HttpStatus httpStatus;
    private String error;
    private String errorDescription;
    private String errorCauseLocalized;

    public ExceptionResponse(Exception ex) {
        setHttpStatus(HttpStatus.BAD_REQUEST);
        setError(ex.getClass().getSimpleName());
        setErrorDescription(ex.getLocalizedMessage());
        setErrorCauseLocalized(nonNull(ex.getCause()) ? ex.getCause().getLocalizedMessage() : null);
    }

}