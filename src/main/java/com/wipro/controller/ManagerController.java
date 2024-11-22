package com.wipro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.entity.Employee;
import com.wipro.entity.LeaveRequest;
import com.wipro.payload.CommentsPayload;
import com.wipro.service.EmployeeService;
import com.wipro.service.ManagerService;

@RestController
@RequestMapping("/managers")
@CrossOrigin
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    
    @Autowired
    private EmployeeService employeeService;
    
//    To displayy all the eployee with their request----->http://localhost:8081/managers/allUsers
    @GetMapping("/allUsers")
    public List<Employee> getAllEmployees(){
    	return managerService.getAllEmployees();
    }


//for manager to approve the request----------->>http://localhost:8081/managers//leave-requests/{leaveRequestId}/approve
    @PostMapping("/leave-requests/{leaveRequestId}/approve")
    public String approveLeaveRequest(@PathVariable int leaveRequestId) {
        try{
        	return managerService.approveLeaveRequest(leaveRequestId);
        	
        }catch(Exception e) {
        	return e.getMessage();
        }
    }
    
    
  //for manager to approve the request----------->>http://localhost:8081/managers//leave-requests/{leaveRequestId}/approve
    @PostMapping("/leave-requests/{leaveRequestId}/reject")
    public String rejectLeaveRequest(@PathVariable int leaveRequestId, @RequestBody CommentsPayload comments) {
       try {
    	   managerService.rejectLeaveRequest(leaveRequestId, comments);
    	   return "Leave rejected by Manager";
       }catch(Exception e) {
    	   return e.getMessage();
       }
    }
    
//  -------http://localhost:8081/managers/allUsers/{status}
  @GetMapping("/allUsers/{status}")
  public List<Employee> getAllEmployeesByStatus(@PathVariable String status){
  	return managerService.getAllEmployeesByStatus(status);
  }
  
  @DeleteMapping("/delete-employee/{id}")
	public String deleteLeave(@PathVariable Integer id) {
		return employeeService.deleteEmployee(id);
	}
  @GetMapping("/employee/{employeeId}/pendingLeaveRequests")
  public List<LeaveRequest> getPendingLeaveRequestsForEmployee(@PathVariable int employeeId) {
      return managerService.getPendingLeaveRequestsForEmployee(employeeId);
  }

} 
