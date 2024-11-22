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
public class EmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email).get();
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not found");
        }

        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .roles("EMPLOYEE") 
                .build();
    }
}