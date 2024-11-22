package com.wipro.entity;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "lr")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_request_seq")
    @SequenceGenerator(name = "leave_request_seq", sequenceName = "leave_request_sequence", allocationSize = 1)
    private int id;
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType; 
    private Date startDate;
    private Date endDate;
    private String status; // "pending", "approved", "rejected"
    private String reason;
    private String comments;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;
    

    
}
