package com.employee.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.employee.entity.Employee;
import com.employee.entity.Payroll;

    @Component
    public class Payrollprocessor implements ItemProcessor<Employee, Payroll> {
        @Override
        public Payroll process(Employee employee) throws Exception {
            if (employee == null || employee.getSalary() == null || employee.getTaxRate() == null) {
                throw new IllegalArgumentException("Invalid employee details provided.");
            }

            double tax = employee.getSalary() * employee.getTaxRate();
            double netSalary = employee.getSalary() - tax;

            return new Payroll(employee.getId(), netSalary);
        }
    }



