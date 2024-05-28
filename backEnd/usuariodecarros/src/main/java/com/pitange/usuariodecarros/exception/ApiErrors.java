package com.pitange.usuariodecarros.exception;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class ApiErrors {
	
	@Getter
	private List<String> messages;
	
	public ApiErrors(List<String> messages) {
		this.messages = messages;
	}

	public ApiErrors(String messages) {
		this.messages = Arrays.asList(messages);
	}
	
}
