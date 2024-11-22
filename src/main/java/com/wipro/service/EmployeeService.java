package com.wipro.service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wipro.entity.Employee;
import com.wipro.entity.LeaveBalance;
import com.wipro.entity.LeaveTypeDetails;
import com.wipro.entity.LeaveRequest;
import com.wipro.entity.LeaveType;
import com.wipro.entity.Role;
import com.wipro.repo.EmployeeRepository;
import com.wipro.repo.LeavePolicyRepository;
import com.wipro.repo.LeaveRequestRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;    
    @Autowired
    private LeavePolicyRepository leavePolicyRepository;
    
  
    public Employee registerEmployee(Employee employee) {
    	 Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
         if (existingEmployee.isPresent()) {
    
             throw new IllegalArgumentException("Email already exists. Change mail");
         }
         
    	 Optional<Employee> existingEmployeeRole = employeeRepository.findByRole(Role.MANAGER);
        
         if (existingEmployeeRole.isPresent()) {
        	 employee.setRole(Role.EMPLOYEE);
         }
         
    	// Create initial leave balances for new employee
            List<LeaveBalance> leaveBalances = new ArrayList<>();

            
            LeaveTypeDetails sickLeavePolicy = leavePolicyRepository.findByLeaveType(LeaveType.SICK_LEAVE);
            LeaveTypeDetails vacationLeavePolicy = leavePolicyRepository.findByLeaveType(LeaveType.VACATION_LEAVE);
            LeaveTypeDetails casualLeavePolicy = leavePolicyRepository.findByLeaveType(LeaveType.CASUAL_LEAVE);
            LeaveBalance sickLeave = new LeaveBalance();
            
            //Initializing SICK_LEAVE
            sickLeave.setLeaveType(LeaveType.SICK_LEAVE);
            sickLeave.setUsedLeaves(0);
            sickLeave.setPendingLeaves(sickLeavePolicy.getTotalLeaves());
            sickLeave.setEmployee(employee);

            //Initializing VACATION_LEAVE
            LeaveBalance vacationLeave = new LeaveBalance();
            vacationLeave.setLeaveType(LeaveType.VACATION_LEAVE);
            vacationLeave.setUsedLeaves(0);
            vacationLeave.setPendingLeaves(vacationLeavePolicy.getTotalLeaves());
            vacationLeave.setEmployee(employee);

            //Initializing CASUAL_LEAVE
            LeaveBalance casualLeave = new LeaveBalance();
            casualLeave.setLeaveType(LeaveType.CASUAL_LEAVE);
            casualLeave.setUsedLeaves(0);
            casualLeave.setPendingLeaves(casualLeavePolicy.getTotalLeaves());
            casualLeave.setEmployee(employee);

            //i am adding all to Leaverepository
            leaveBalances.add(sickLeave);
            leaveBalances.add(vacationLeave);
            leaveBalances.add(casualLeave);

            employee.setLeaveBalances(leaveBalances);
           
            Employee savedEmployee = employeeRepository.save(employee); 

            
            return savedEmployee;
       }


    
    public String submitLeaveRequest(int employeeId, LeaveRequest leaveRequest) {
        try {
            // Ensure the end date is not before the start date
            if (leaveRequest.getEndDate().before(leaveRequest.getStartDate())) {
                throw new IllegalArgumentException("Check the date");
            }

            // Check if there are any overlapping leave requests for this employee
            List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingLeaveRequests(employeeId, leaveRequest.getStartDate(), leaveRequest.getEndDate());

            if (!overlapping.isEmpty()) {
                throw new IllegalArgumentException("A leave request for this date has already been submitted.");
            }

            // Fetch the employee using the employeeId (from method argument)
            Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
            
            // Set the employee in the leaveRequest object explicitly
            leaveRequest.setEmployee(employee);
            
            // Calculate the number of days requested
            long daysRequested = ChronoUnit.DAYS.between(leaveRequest.getStartDate().toLocalDate(), leaveRequest.getEndDate().toLocalDate()) + 1;

            // Find the appropriate leave balance for the leave type
            List<LeaveBalance> leaveBalances = employee.getLeaveBalances();
            LeaveType leaveType = leaveRequest.getLeaveType();
            LeaveBalance leaveBalance = null;
            
            for (LeaveBalance lb : leaveBalances) {
                if (lb.getLeaveType().equals(leaveType)) {
                    leaveBalance = lb;
                    break;
                }
            }

            // Check if the employee has enough pending leaves for the requested type
            if (leaveBalance == null || leaveBalance.getPendingLeaves() < daysRequested) {
                throw new RuntimeException("Insufficient leave balance for " + leaveType + ". Available: " +
                                           (leaveBalance != null ? leaveBalance.getPendingLeaves() : 0) + 
                                           ", Requested: " + daysRequested);
            }

            // Set additional properties for the leave request
            leaveRequest.setStatus("pending")     ;
            leaveRequest.setComments("Awaiting Manager Approval");

            // Save the leave request
            leaveRequestRepository.save(leaveRequest);

            return "Your request has been submitted";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
    

    public void editLeaveRequest(int leaveRequestId, LeaveRequest updatedRequest) {
        if (updatedRequest.getEndDate().before(updatedRequest.getStartDate())) {
            throw new IllegalArgumentException("End date must be after the start date");
        }

        LeaveRequest existingRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
        existingRequest.setLeaveType(updatedRequest.getLeaveType());
        existingRequest.setStartDate(updatedRequest.getStartDate());
        existingRequest.setEndDate(updatedRequest.getEndDate());
        existingRequest.setReason(updatedRequest.getReason());

        leaveRequestRepository.save(existingRequest);
    }


    public Employee viewEmployee(int id) {
        Employee emp =  employeeRepository.findById(id).orElseThrow();
        return emp;
    }
    
    public List<LeaveBalance> viewLeaveBalances(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getLeaveBalances();
    }



	public String deleteLeave(Integer leaveRequestId) {
		leaveRequestRepository.deleteById(leaveRequestId);		
		return "Request has been deleted successfully.....";
		
	}



	public String deleteEmployee(Integer id) {
		employeeRepository.deleteById(id);
		return "Employee has been removem by the Manager";
	}
}
