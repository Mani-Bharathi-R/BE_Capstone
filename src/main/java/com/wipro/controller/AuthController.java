package com.wipro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.entity.Employee;
import com.wipro.entity.Role;
import com.wipro.payload.AuthRequest;
import com.wipro.repo.EmployeeRepository;
import com.wipro.security.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private EmployeeRepository employeeRepository;

	

	@PostMapping("/login")
	public ResponseEntity<?> loginAdmin(@RequestBody AuthRequest authRequest) {
		Employee employee = employeeRepository.findByEmail(authRequest.getEmail()).get();
		if (employee != null && authRequest.getPassword().equals(employee.getPassword())) {
			String token = jwtUtil.generateToken(authRequest.getEmail());
			Integer employeeId = employee.getId();
			Role role = employee.getRole();
			return ResponseEntity.ok(new LoginResponse(token, employeeId,role));
		} else {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Invalid credentials");
		}
	}

		private static class LoginResponse {
		private String token;
		private Role role;
		private Integer id; 

		public LoginResponse( String token, Integer employeeId, Role role) {		
			this.token = token;
			this.role = role;
			this.id = employeeId; 
		}

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}