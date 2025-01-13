package com.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @Column(name = "net_salary")
    private Double netSalary;

    public Payroll() {}

    public Payroll(Employee employee, Double netSalary) {
        this.employee = employee;
        this.netSalary = netSalary;
    }

   
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }
    
    
    
    public Payroll(Long id2, double netSalary2) {
		// TODO Auto-generated constructor stub
	}

}
