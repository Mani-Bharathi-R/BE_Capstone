package com.wipro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.entity.Employee;
import com.wipro.payload.AuthRequest;
import com.wipro.repo.EmployeeRepository;

@Service
public class AuthService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public String authenticateEmployee(AuthRequest authRequest){
		Optional<Employee> empdetails = employeeRepository.findByEmail(authRequest.getEmail());
		if(empdetails.isPresent()) {
			Boolean value = empdetails.get().getPassword().equals(authRequest.getPassword());
			if(value) {
				String role=empdetails.get().getRole().toString();
				String id=empdetails.get().getId().toString();
				return role+":"+ id;
			}else {
				return "false";
			}
		}
		return "false";
	}
}