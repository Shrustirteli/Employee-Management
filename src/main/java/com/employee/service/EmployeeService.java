package com.employee.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employee.entity.Employee;
import com.employee.helper.ExcelHelper;
import com.employee.processor.Payrollprocessor;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.PayrollRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repository;
    
    @Autowired
    private PayrollRepository payrollRepository;
    
    @Autowired
    private Payrollprocessor payrollProcessor;
    
    @Autowired
    private ExcelHelper excelHelper;
    

    
    public void saveDataFromExcel(InputStream is) throws Exception {
        List<Employee> dataList = ExcelHelper.excelToEmployeeData(is);
        repository.saveAll(dataList);
    }

	public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
    
    public ByteArrayInputStream downloadEmployeePayrollDataAsExcel() {
        List<Employee> employees = repository.findAll();  // Fetch employees along with Payroll
        return ExcelHelper.employeePayrollToExcel(employees);
    }
}















//public void calculatePayrollAndSave() throws Exception {
//// Fetch all employees
//List<Employee> employees = repository.findAll();
//
//// Pass employee list to helper class for payroll calculation
//List<Employee> updatedEmployees = excelHelper.calculatePayroll(employees);
//
//// Save updated employees back to the database
//repository.saveAll(updatedEmployees);
//
//// Explicitly flush to ensure changes are committed to the database
//repository.flush();
//}



//    @Transactional
//    public Employee saveEmployeeWithPayroll(Employee employee) {
//        try {
//        	
//            // Save the employee first
//            employee = repository.save(employee);
//
//            // Step 1: Calculate Payroll using PayrollProcessor
//            Payrollprocessor processor = new Payrollprocessor();
//            Payroll payroll = processor.process(employee);
//
//            // Step 2: Set the employee in payroll
//            payroll.setEmployee(employee);
//
//            // Step 3: Save the payroll in the database
//            payroll = payrollRepository.save(payroll);
//
//            // Step 4: Associate payroll with the employee
//            List<Payroll> payrolls = new ArrayList<>();
//            payrolls.add(payroll);
//            employee.setPayrolls(payrolls);
//
//            // Step 5: Return the employee with associated payrolls
//            return employee;
//        } catch (Exception e) {
//            // Log the exception for debugging
//            e.printStackTrace();
//            throw new RuntimeException("Failed to save Employee with Payroll", e);
//        }
//    }



//    public void saveEmployeeData(MultipartFile file) throws Exception {
//        List<Employee> employees = ExcelHelper.parseExcelFile(file.getInputStream());
//
//        for (Employee emp : employees) {
//            // Save employee data
//           repository.save(emp);
//
//            // Calculate payroll
//            double grossSalary = emp.getSalary() + calculateAllowances(emp.getSalary());
//            double tax = grossSalary * 0.05; // tax
//            double netSalary = grossSalary - tax;
//
//            // Save payroll data
//            Payroll payroll = new Payroll(emp.getId(), grossSalary, tax, netSalary);
//            payrollRepository.save(payroll);
//        }
//    }
//
//    private Double calculateAllowances(Double salary) {
//		// TODO Auto-generated method stub
//		return null;
//	}



