package com.wipro.entity;

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


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "lb")
public class LeaveBalance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_balance_seq")
    @SequenceGenerator(name = "leave_balance_seq", sequenceName = "leave_balance_sequence", allocationSize = 1)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    private Integer usedLeaves;
    private Integer pendingLeaves;

  
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;
}
