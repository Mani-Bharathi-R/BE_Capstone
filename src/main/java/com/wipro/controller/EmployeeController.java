package com.wipro.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wipro.entity.Employee;
import com.wipro.entity.LeaveBalance;
import com.wipro.entity.LeaveRequest;
import com.wipro.entity.Role;
import com.wipro.service.EmployeeService;

@RestController
@RequestMapping
@CrossOrigin
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	
	@PostMapping("/auth/register")
    public ResponseEntity<String> registerEmployee(@RequestBody Employee emp) {
    	try {
    		Employee employee = employeeService.registerEmployee(emp);
    		if(employee.getRole().equals(Role.EMPLOYEE)) {
    			return ResponseEntity.ok("Employee registered successfully");
    		}
    		return ResponseEntity.ok("Manager registered successfully");
    		 
    	}catch(Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage()); 
    	}
    }

//for submitting a leave request ---------------->http://localhost:8081/employees/id/leave-requests
	@PostMapping("/employees/{employeeId}/leave-requests")
	public ResponseEntity<String> submitLeaveRequest(
			@PathVariable int employeeId, 
			@RequestBody LeaveRequest leaveRequest) {
		try {
			String result = employeeService.submitLeaveRequest(employeeId, leaveRequest);
			return ResponseEntity.ok(result); // Return 200 OK 
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage()); 
		}
	}

	//for editing a leave request ---------------->http://localhost:8081/employees/leave-requests/id

	@PutMapping("/employees/leave-requests/{leaveRequestId}")
	public void editLeaveRequest(@PathVariable int leaveRequestId, @RequestBody LeaveRequest updatedRequest) {
		employeeService.editLeaveRequest(leaveRequestId, updatedRequest);
	}
	//for displaying the  employee wit id ------------->http://localhost:8081/employees/id
	@GetMapping("/employees/{id}")
	public Employee viewEmployee(@PathVariable int id) {
		return employeeService.viewEmployee(id);
	}
	//for displaying the leave balance of employee wit id ------------->http://localhost:8081/employees/id
	@GetMapping("/employees/{employeeId}/leave-balances")
	public List<LeaveBalance> viewLeaveBalances(@PathVariable int employeeId) {
		return employeeService.viewLeaveBalances(employeeId);
	}
	
	@DeleteMapping("/employees/leave-requests/{leaveRequestId}")
	public String deleteLeave(@PathVariable Integer leaveRequestId) {
		return employeeService.deleteLeave(leaveRequestId);
	}

}
