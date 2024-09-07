package com.pitange.usuariodecarros;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

public class JwtSecretCreaterTest {
	
	@Test
	public void generateSecretKey() {
		
		SecretKey key = Jwts.SIG.HS512.key().build();
		String encodeKey = DatatypeConverter.printHexBinary(key.getEncoded());
		System.out.printf("Key = [%s]\n", encodeKey);
		
	}

}
