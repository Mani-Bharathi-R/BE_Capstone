package com.wipro.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.entity.LeaveTypeDetails;
import com.wipro.entity.LeaveType;

@Repository
public interface LeavePolicyRepository extends JpaRepository<LeaveTypeDetails, Integer> {

	LeaveTypeDetails findByLeaveType(LeaveType leaveType);
}
