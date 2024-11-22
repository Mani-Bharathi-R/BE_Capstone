package com.wipro.service;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.entity.LeaveTypeDetails;
import com.wipro.entity.LeaveType;
import com.wipro.repo.LeavePolicyRepository;

import jakarta.annotation.PostConstruct;

@Service
public class LeavePolicyService {

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @PostConstruct
    public void initializeLeavePolicies() {
        LeaveTypeDetails vacationPolicy = new LeaveTypeDetails(LeaveType.VACATION_LEAVE, 12);
        LeaveTypeDetails sickPolicy = new LeaveTypeDetails(LeaveType.SICK_LEAVE, 10);
        LeaveTypeDetails casualPolicy = new LeaveTypeDetails(LeaveType.CASUAL_LEAVE, 6);

        leavePolicyRepository.saveAll(Arrays.asList(vacationPolicy, sickPolicy, casualPolicy));

    }
}
