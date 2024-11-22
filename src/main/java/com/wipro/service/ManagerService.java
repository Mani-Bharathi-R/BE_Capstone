package com.wipro.service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.entity.Employee;
import com.wipro.entity.LeaveBalance;
import com.wipro.entity.LeaveRequest;
import com.wipro.entity.LeaveType;
//import com.wipro.entity.Manager;
import com.wipro.payload.CommentsPayload;
import com.wipro.repo.EmployeeRepository;
import com.wipro.repo.LeaveBalanceRepository;
import com.wipro.repo.LeavePolicyRepository;
import com.wipro.repo.LeaveRequestRepository;


@Service
public class ManagerService {
	@Autowired
	private LeaveRequestRepository leaveRequestRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private LeaveBalanceRepository leaveBalanceRepository;


	public String approveLeaveRequest(int leaveRequestId) {
		try {
			LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow();

			if ("pending".equals(leaveRequest.getStatus())) {
				leaveRequest.setStatus("approved");
				leaveRequest.setComments("Leave request accepted by Manager");
				Employee employee = employeeRepository.findById(leaveRequest.getEmployee().getId()).orElseThrow();
				long daysRequested = ChronoUnit.DAYS.between(leaveRequest.getStartDate().toLocalDate(), leaveRequest.getEndDate().toLocalDate()) + 1;

				List<LeaveBalance> leaveBalances = employee.getLeaveBalances();


				LeaveType leaveType = leaveRequest.getLeaveType();

				            LeaveBalance leaveBalance = null;
				for (LeaveBalance lb : leaveBalances) {
					if (lb.getLeaveType().equals(leaveType)) {
						leaveBalance = lb;
						break;
					}
				}

				if (leaveBalance == null) {
					throw new RuntimeException("No leave balance found for leave type: " + leaveType);
				}

				if (leaveBalance.getPendingLeaves() < daysRequested) {
					throw new RuntimeException("Insufficient leave balance for " + leaveType + ". Available: " +
							(leaveBalance.getPendingLeaves()) + ", Requested: " + daysRequested);
				}

				leaveBalance.setUsedLeaves(leaveBalance.getUsedLeaves() + (int) daysRequested);

				leaveBalance.setPendingLeaves(leaveBalance.getPendingLeaves() - (int)daysRequested);
				
				leaveBalanceRepository.save(leaveBalance);

				
				leaveRequestRepository.save(leaveRequest);
				return "Leave request is approved";
			} else {
				throw new RuntimeException("Leave request is not in 'pending' status");
			}
		}catch(Exception e) {
			return e.getMessage();
		}
	}

	public String rejectLeaveRequest(int leaveRequestId, CommentsPayload comments) {
		try {
			LeaveRequest request = leaveRequestRepository.findById(leaveRequestId).orElseThrow();
			request.setStatus("rejected");
			request.setComments(comments.getComments());
			leaveRequestRepository.save(request);
			
			return "Leave request rejected by Manager";
		}catch(Exception e) {
			return e.getMessage();
		}
	}



	public List<Employee> getAllEmployees() {

		List<Employee> employees = employeeRepository.findAll();
		return employees;
	}


	public List<Employee>getAllEmployeesByStatus(String status) {


		// to avoid duplicate i use map
		Map<Integer, Employee> employeeMap = new HashMap<>();
		List<LeaveRequest> pendingLeaveRequests = leaveRequestRepository.findByStatus(status);

		for (LeaveRequest leaveRequest : pendingLeaveRequests) {
			Employee emp = leaveRequest.getEmployee();


			if (employeeMap.containsKey(emp.getId())) {

				employeeMap.get(emp.getId()).getLeaveRequests().add(leaveRequest);
			} else {

				emp.setLeaveRequests(new ArrayList<>());  
				emp.getLeaveRequests().add(leaveRequest); 
				employeeMap.put(emp.getId(), emp);
			}
		}


		return new ArrayList<>(employeeMap.values());

	}
	
	public List<LeaveRequest> getPendingLeaveRequestsForEmployee(int employeeId) {
		
	  	employeeRepository.findById(employeeId).orElseThrow(
        () -> new RuntimeException("Employee with ID " + employeeId + " not found"));

	    
	    List<LeaveRequest> pendingLeaveRequests = leaveRequestRepository.findByEmployeeIdAndStatus(employeeId, "pending");
	
//	    if (pendingLeaveRequests.isEmpty()) {
//	        throw new RuntimeException("No pending leave requests found for employee with ID " + employeeId);
//	    }

    return pendingLeaveRequests;
}

}
