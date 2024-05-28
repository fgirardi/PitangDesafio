package com.pitange.usuariodecarros.exception;

public class DuplicateLoginException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateLoginException(String message) {
        super(message);
    }
}
