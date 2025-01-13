package com.employee.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(nullable = false)
    private Double salary;
    @Column(nullable = false)
    private Double taxRate;
    @Column(nullable = false)
    private String paymentMethod;
    @Column(nullable = false)
    private String designation;
    @Column(nullable = false)
    private String department;
   

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payroll> payrolls;  // Reference to Payroll entity

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

   
    public List<Payroll> getPayrolls() {
        return payrolls;
    }

    public void setPayrolls(List<Payroll> payrolls) {
        this.payrolls = payrolls;
    }

    
    
	public void setEmpId(int numericCellValue) {
		// TODO Auto-generated method stub
		
	}

	public void setPayroll(Double netSalary) {
		// TODO Auto-generated method stub
		
	}

	public Payroll getPayroll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPayroll(Payroll payroll) {
		// TODO Auto-generated method stub
		
	}
}
