package com.pitange.usuariodecarros.controller;

import java.util.List;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pitange.usuariodecarros.exception.ApiErrors;
import com.pitange.usuariodecarros.exception.DuplicateLoginException;
import com.pitange.usuariodecarros.exception.UserCreationException;

/**
 * Classe que fornece tratamento global de exceções para controladores.
 * Esta classe intercepta exceções de validação e de argumentos inválidos.
 * Autor: [Fabiano Girardi]
 * Data: [25/04/2024]
 */
//@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	/**
     * Handle validation errors.
     *
     * @param ex the MethodArgumentNotValidException
     * @return the ApiErrors object containing error messages
     */
	
	@ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationErrors(MethodArgumentNotValidException ex) {
		
		BindingResult bindingResult = ex.getBindingResult();
		
		List<String> messages = bindingResult.getAllErrors()
								.stream()
								.map(objError -> objError.getDefaultMessage())
								.collect(Collectors.toList());
		
		return new ApiErrors(messages);
	}
	
	/**
     * Handle duplicate login errors.
     *
     * @param ex the DuplicateLoginException
     * @return the ApiErrors object containing the error message
     */
    @ExceptionHandler(DuplicateLoginException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrors handleDuplicateLoginException(DuplicateLoginException ex) {
        return new ApiErrors(List.of(ex.getMessage()));
    }
    
    @ExceptionHandler(UserCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrors handleUserCreationException(UserCreationException ex) {
        return new ApiErrors(List.of(ex.getMessage()));
    }
	
}
