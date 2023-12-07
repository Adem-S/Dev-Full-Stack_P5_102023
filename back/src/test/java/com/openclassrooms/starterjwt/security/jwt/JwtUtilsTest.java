package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
class JwtUtilsTest {

	@Autowired
	private JwtUtils jwtUtils;

	@Test
	public void testGenerateJwtToken() {

		String mail = "test@gmail.com";
		String password = "newPassword";

		UserDetails userDetails = new UserDetailsImpl(null, mail, password, null, null, null);

		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);

		String token = jwtUtils.generateJwtToken(auth);

		assertAll(() -> assertFalse(token.isEmpty()), () -> assertEquals(mail, jwtUtils.getUserNameFromJwtToken(token)),
				() -> assertTrue(jwtUtils.validateJwtToken(token)));
	}

	@Test
	public void testInvalidSignatureJwt() {

		String email = "test@email.com";
		long jwtExpirationMs = 86400000;
		String jwtSecret = "invalidJwt";

		String jwt = Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

		assertFalse(jwtUtils.validateJwtToken(jwt));
	}

	@Test
	public void testMalformedJwt() {
		assertFalse(jwtUtils.validateJwtToken("malformedJwt"));
	}

	@Test
	public void IllegalArgumentException() {
		assertFalse(jwtUtils.validateJwtToken(null));
	}

}
