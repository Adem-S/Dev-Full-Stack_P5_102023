package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
class AuthTokenFilterTest {

	@Mock
	private JwtUtils jwtUtils;
	@Mock
	private UserDetailsServiceImpl userDetailsService;
	@InjectMocks
	private AuthTokenFilter authTokenFilter;

	@Test
	public void testDoFilterInternal() throws ServletException, IOException {

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain filterChain = mock(FilterChain.class);

		when(request.getHeader("Authorization")).thenReturn("Bearer jwt");
		when(jwtUtils.validateJwtToken("jwt")).thenReturn(true);
		when(userDetailsService.loadUserByUsername(any())).thenReturn(UserDetailsImpl.builder().build());

		authTokenFilter.doFilterInternal(request, response, filterChain);
		verify(filterChain).doFilter(request, response);
	}

}
