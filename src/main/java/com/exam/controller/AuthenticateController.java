package com.exam.controller;
import java.security.Principal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.exam.model.JwtRequest;
import com.exam.model.JwtResponse;
import com.exam.model.User;
import com.exam.security.JwtHelper;
import com.exam.service.impl.UserDetailsServiceImpl;
import com.exam.service.impl.UserServiceImpl;

@RestController
@CrossOrigin("*")
public class AuthenticateController {
	
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;
    
    private Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    private UserServiceImpl userServiceImpl;
    
    
    
    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
    {
    	
    	try {
    		authenticate(jwtRequest.getUsername(),jwtRequest.getPassword());
    				
    		
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("User Not Found !!" +e.getMessage());
		}
    	
   UserDetails  userDetails=	this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
    	String token=this.helper.generateToken(userDetails);
    	
    	return ResponseEntity.ok(new JwtResponse(token));
    }
	 private void authenticate(String username, String password) throws Exception {
		 try {
			 
			 manager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
		} catch (DisabledException e) {
			
			throw new Exception("user DIsabled" +e.getMessage());
		}catch (BadCredentialsException e) {
			
			throw new Exception("Invalid Credentials" + e.getMessage());

		} 

	 }	
	 @GetMapping("/current-user")
	 public User getCurrentUser(Principal principal)
	 {
		 return (User) this.userDetailsService.loadUserByUsername(principal.getName());
	 }
}
