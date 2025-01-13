package com.employee.reader;

import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.employee.entity.Employee;
import com.employee.repository.EmployeeRepository;

@Component
public class EmployeeReader implements ItemReader<Employee> {
    @Autowired
    private EmployeeRepository repository;

    private Iterator<Employee> employeeIterator;

    @Override
    public Employee read() throws Exception {
        if (employeeIterator == null || !employeeIterator.hasNext()) {
            employeeIterator = repository.findAll().iterator();
        }
        return employeeIterator.hasNext() ? employeeIterator.next() : null;
    }
}
