package com.wipro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wipro.entity.Employee;
import com.wipro.entity.Role;
import com.wipro.repo.EmployeeRepository;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class LeaveView{
	 @Autowired
	 private EmployeeRepository employeeRepository;
	 
	public static void main(String[] args) {
		SpringApplication.run(LeaveView.class, args);
	}
}

