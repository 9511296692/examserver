package com.exam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.exam.security.JwtAuthenticationEntryPoint;
import com.exam.security.JwtAuthenticationFilter;
import com.exam.service.impl.UserDetailsServiceImpl;


@Configuration
public class SecurityConfig {
	

	 @Autowired
	    private JwtAuthenticationEntryPoint point;
	    
	    @Autowired
	    private JwtAuthenticationFilter filter;
	    
	
	  @Autowired
	    private UserDetailsServiceImpl userDetailsServiceImpl;
	    
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    
	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    	http.csrf(csrf->csrf.disable())
	    	.cors(cors->cors.disable())
	    	.authorizeHttpRequests(
	    			auth->auth.requestMatchers("/generate-token","/user/").permitAll()
	    			.requestMatchers(HttpMethod.OPTIONS)
	    			.permitAll()
	    			.anyRequest().authenticated())
	    	         .exceptionHandling(ex->ex.authenticationEntryPoint(point))
	    	         .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	    }
	    
	    
	    @Bean
	    public DaoAuthenticationProvider doAuthenticationProvider ()
	    {
	    	
	 
	    	DaoAuthenticationProvider doAuthenticationProvider=	new DaoAuthenticationProvider();
	    	doAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
	    	doAuthenticationProvider.setPasswordEncoder(passwordEncoder);
	    	
	    	
	    	return doAuthenticationProvider;
	    }
	    

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
	        return builder.getAuthenticationManager();
	    }



}
