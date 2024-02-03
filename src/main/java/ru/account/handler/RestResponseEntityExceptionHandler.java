package ru.account.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.account.models.ErrorModel;

@ControllerAdvice
@Log4j2
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RestClientException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error(ex.toString());
        return handleExceptionInternal(ex, new ErrorModel(httpStatus.value(), "Неверные данные"),
                new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleServerError(
            Exception ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(ex.toString());
        return handleExceptionInternal(ex, new ErrorModel(httpStatus.value(), "Ошибка на стороне сервера"),
                new HttpHeaders(), httpStatus, request);
    }
}
