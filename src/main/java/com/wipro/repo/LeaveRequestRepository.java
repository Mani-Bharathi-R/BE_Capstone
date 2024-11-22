package com.wipro.repo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wipro.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByEmployeeId(int employeeId);
    List<LeaveRequest> findAllByStatus(String status);
    List<LeaveRequest> findByStatus(String status);
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId AND ((:startDate BETWEEN lr.startDate AND lr.endDate) OR (:endDate BETWEEN lr.startDate AND lr.endDate) OR (lr.startDate BETWEEN :startDate AND :endDate))")
    List<LeaveRequest> findOverlappingLeaveRequests(@Param("employeeId") int employeeId, @Param("startDate") Date date, @Param("endDate") Date date2);
    
    List<LeaveRequest> findByEmployeeIdAndStatus(int employeeId, String status);

}
