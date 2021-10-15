package org.edu.springmicroservice.config.validation;

import org.edu.springmicroservice.config.validation.dto.ErrorDTO;
import org.edu.springmicroservice.config.validation.dto.ValidationErrorDTO;
import org.edu.springmicroservice.config.validation.exception.BusinessRuleException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    private final MessageSource messageSource;

    public ErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationErrorDTO> handler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        return fieldErrors.stream().map(fieldError -> new ValidationErrorDTO(fieldError.getField(), messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessRuleException.class)
    public ErrorDTO handler(BusinessRuleException exception) {
        return new ErrorDTO(exception.getMessage());
    }
}
