package br.com.project.orderservice.core.handler;

import br.com.project.orderservice.core.domain.dto.OSErrorResponse;
import br.com.project.orderservice.core.exceptions.FeignClientBadRequestException;
import br.com.project.orderservice.core.exceptions.FeignClientNotFoundException;
import br.com.project.orderservice.core.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ResponseEntity<OSErrorResponse> handleInternalServerError(HttpServletRequest httpServletRequest, Throwable throwable) {
        return logStackTraceAndConstructOSErrorResponse(httpServletRequest, throwable, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ NotFoundException.class, FeignClientNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    ResponseEntity<OSErrorResponse> handleNotFound(HttpServletRequest httpServletRequest, Throwable throwable) {
        return logStackTraceAndConstructOSErrorResponse(httpServletRequest, throwable, NOT_FOUND);
    }

    @ExceptionHandler({ FeignClientBadRequestException.class, MethodArgumentNotValidException.class, HttpMediaTypeNotSupportedException.class })
    @ResponseStatus(BAD_REQUEST)
    ResponseEntity<OSErrorResponse> handleUnprocessableEntity(HttpServletRequest httpServletRequest, Throwable throwable) {
        return logStackTraceAndConstructOSErrorResponse(httpServletRequest, throwable, BAD_REQUEST);
    }

    private ResponseEntity<OSErrorResponse> logStackTraceAndConstructOSErrorResponse(
            HttpServletRequest httpServletRequest,
            Throwable throwable,
            HttpStatus httpStatus) {

        log.error(
                "Erro @ {} {}, {} ({}), resultando em {}: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                throwable.getClass().getSimpleName(),
                throwable.getMessage(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                throwable
        );

        var customMessage = throwable.getMessage();

        if(throwable instanceof MethodArgumentNotValidException exception) {
            customMessage = exception
                    .getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining());
        }

        var response = OSErrorResponse.builder()
                .message(customMessage)
                .path(httpServletRequest.getRequestURI())
                .statusCode(httpStatus.value())
                .statusMessage(httpStatus.getReasonPhrase())
                .build();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

        return new ResponseEntity<>(response, headers, httpStatus);
    }


}
