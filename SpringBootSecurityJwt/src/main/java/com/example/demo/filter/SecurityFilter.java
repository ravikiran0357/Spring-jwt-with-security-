package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.util.JwtUtil;

@Configuration
public class SecurityFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil util;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Autherization");
		if (token != null) {
			String username = util.getUsername(token);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails usr = userDetailsService.loadUserByUsername(username);
				boolean isValid = util.ValidateToken(token, usr.getUsername());

				if (isValid) {
					UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(username,
							usr.getPassword(),usr.getAuthorities());
					authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authtoken);
				}
			}
		}
		filterChain.doFilter(request, response);

	}

}
