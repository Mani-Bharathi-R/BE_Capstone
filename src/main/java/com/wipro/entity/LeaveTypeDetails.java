package com.wipro.entity;
//this entity is only for stroing initial leave values


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "lp")
public class LeaveTypeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_policy_seq")
    @SequenceGenerator(name = "leave_policy_seq", sequenceName = "leave_policy_sequence", allocationSize = 1)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    
    private Integer totalLeaves;
   
    public LeaveTypeDetails(){
    	
    }

    public LeaveTypeDetails(LeaveType leaveType, Integer totalLeaves) {
        this.leaveType = leaveType;
        this.totalLeaves = totalLeaves;
    }
}
