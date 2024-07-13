package com.ranjit.todo.todo.exceptions;

import com.ranjit.todo.todo.dtos.ResponseBody;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.internal.matchers.Null;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> AuthenticationException(Exception ex, WebRequest request) {
        ResponseBody<String> responseBody = ResponseBody.error(ex.getMessage());
        return new ResponseEntity<Object>(responseBody, HttpStatus.UNAUTHORIZED);
    }
}