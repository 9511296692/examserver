package com.exam.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repo.RoleRepository;
import com.exam.repo.UserRepository;
import com.exam.service.UserService;


@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private RoleRepository roleRepository;

	
	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		
	User local=	this.userRepository.findByUsername(user.getUsername());
		if(local != null)
		{
			System.out.println("User is already there !!");
			throw new Exception("User alreday present !!");
		}else {
			
			for(UserRole ur:userRoles)
			{
				roleRepository.save(ur.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);
		 local=	this.userRepository.save(user);
			
		}
		
		
		return  local;
	}


	@Override
	public User getUser(String username) {		
		return this.userRepository.findByUsername(username);
	}


	@Override
	public void deleteUser(Long userId) {
		
		this.userRepository.deleteById(userId);
		
	}

	@Override
	public User updateUser(User user) {
		this.userRepository.findById(user.getId()).orElseThrow();
		return userRepository.save(user);
	}


}
