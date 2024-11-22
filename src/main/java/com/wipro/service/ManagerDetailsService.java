package com.wipro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wipro.entity.Employee;
import com.wipro.repo.EmployeeRepository;

@Service
public class ManagerDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee manager = employeeRepository.findByEmail(username).get();
        if (manager == null) {
            throw new UsernameNotFoundException("Manager not found");
        }

        return User.builder()
                .username(manager.getEmail())
                .password(manager.getPassword())
                .roles("MANAGER") 
                .build();
    }
}