package com.example.demo.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService,UserDetailsService{
	@Autowired
	private UserRepository repo;
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	@Override
	public Integer saveUser(User user) {
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		return repo.save(user).getId();
	}

	@Override
	public Optional<User> findByUsername(String username) {
	
		return repo.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt=findByUsername(username);
		User user=opt.get();
		if(opt.isEmpty())throw new UsernameNotFoundException("user not exists");
		return new org.springframework.security.core.userdetails.User(username, 
				user.getPassword(),
				user.getRoles().stream()
				.map(role->new SimpleGrantedAuthority(role))
				.collect(Collectors.toList())
				);
	}
	

}
