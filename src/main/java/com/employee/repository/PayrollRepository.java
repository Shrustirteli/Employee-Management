package com.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.entity.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Long> 
{
	Payroll findByEmployeeId(Long employeeId);

}
