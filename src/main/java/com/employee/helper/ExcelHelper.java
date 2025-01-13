package com.employee.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.employee.entity.Employee;
import com.employee.entity.Payroll;

import ch.qos.logback.classic.Logger;

@Component
public class ExcelHelper {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExcelHelper.class);


    public static List<Employee> excelToEmployeeData(InputStream is) throws IOException {
        List<Employee> employeeList = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(is); // Open the Excel workbook
        Sheet sheet = workbook.getSheetAt(0); // Read the first sheet (index 0)

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip the header row (first row)
            
            Employee employee = new Employee();
            
            // Read data from Excel file and map it to the Employee object fields
            employee.setEmpId((int) row.getCell(0).getNumericCellValue()); // Column 0 for emp id (Employee ID)
            employee.setName(row.getCell(1).getStringCellValue()); // Column 1 for name
            employee.setDesignation(row.getCell(2).getStringCellValue()); // Column 2 for designation
            employee.setSalary(row.getCell(3).getNumericCellValue()); // Column 3 for salary
            employee.setDepartment(row.getCell(4).getStringCellValue()); // Column 4 for department
            employee.setPaymentMethod(row.getCell(5).getStringCellValue()); // Column 5 for payment method
            employee.setTaxRate(row.getCell(6).getNumericCellValue()); // Column 6 for tax rate

            employeeList.add(employee); // Add employee to the list
        }
        
        workbook.close(); // Close the workbook
        return employeeList; // Return the list of employee objects
    }
    
 
    // Updated headers to include Payroll details
    private static final String[] HEADERS = {"Employee ID", "Name", "Department", "Designation", "Salary", "Tax Rate", "Payment Method", "Net Salary"};
    private static final String SHEET_NAME = "Employee Payroll Details";

    public static ByteArrayInputStream employeePayrollToExcel(List<Employee> employees) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }

            // Populate data rows
            int rowIdx = 1;
            for (Employee employee : employees) {
                Payroll payroll = employee.getPayroll(); // Fetch Payroll from Employee

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(employee.getId());
                row.createCell(1).setCellValue(employee.getName());
                row.createCell(2).setCellValue(employee.getDepartment());
                row.createCell(3).setCellValue(employee.getDesignation());
                row.createCell(4).setCellValue(employee.getSalary());
                row.createCell(5).setCellValue(employee.getTaxRate());
                row.createCell(6).setCellValue(employee.getPaymentMethod());
                
                if (payroll != null) {
                    System.out.println("Net Salary: " + payroll.getNetSalary());  // Debug print
                } else {
                    System.out.println("Payroll is null");  // Debug print if payroll is null
                }
                // If Payroll exists, fetch Net Salary, else set as 0
                row.createCell(7).setCellValue(payroll != null && payroll.getNetSalary() != null ? payroll.getNetSalary() : 0.0);

            }

            // Auto-size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel file: " + e.getMessage());
        }
    }

}


















//// Process method to calculate payroll for an individual employee
//public Payroll process(Employee employee) throws Exception {
//  // Validate employee data
//  if (employee == null || employee.getSalary() == null || employee.getTaxRate() == null) {
//      throw new IllegalArgumentException("Invalid employee details provided.");
//  }
//
//  // Calculate tax based on salary and tax rate
//  double tax = employee.getSalary() * employee.getTaxRate();
//
//  // Calculate net salary after tax deduction
//  double netSalary = employee.getSalary() - tax;
//
//  // Return the payroll details (id and netSalary)
//  return new Payroll(employee.getId(), netSalary);
//}


//public List<Employee> calculatePayroll(List<Employee> employees) throws Exception {
//List<Employee> updatedEmployees = new ArrayList<>();
//
//for (Employee employee : employees) {
//  // Call the process method for each employee to calculate payroll
//  Payroll payroll = process(employee);
//
//  // Assuming Payroll object contains the final payroll calculation (netSalary)
//  employee.setPayroll(payroll.getNetSalary());
//
//  // Add the updated employee to the list
//  updatedEmployees.add(employee);
//}
//return updatedEmployees;
//}   


//public static List<Employee> parseExcelFile(InputStream is) throws IOException {
//    List<Employee> employees = new ArrayList<>();
//
//    XSSFWorkbook workbook = new XSSFWorkbook(is);
//    Sheet sheet = workbook.getSheetAt(0);
//
//    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header
//        Row row = sheet.getRow(i);
//
//        Employee emp = new Employee();
//        emp.setId((int) row.getCell(0).getNumericCellValue());
//        emp.setName(row.getCell(1).getStringCellValue());
//        emp.setDesignation(row.getCell(2).getStringCellValue());
//        emp.setSalary(row.getCell(3).getNumericCellValue());
//        emp.setDepartment(row.getCell(4).getStringCellValue());
//
//        employees.add(emp);
//    }
//
//    workbook.close();
//    return employees;
//}


// // Method to read employee data from an Excel file and convert it into a list of Employee objects
//    public static List<Employee> saveDataFromExcel(InputStream is) throws IOException {
//        List<Employee> employeeList = new ArrayList<>();
//        
//        // Create a Workbook object from the input stream
//        XSSFWorkbook workbook = new XSSFWorkbook(is);
//        
//        // Get the first sheet in the workbook
//        Sheet sheet = workbook.getSheetAt(0);  // Assuming data is on the first sheet
//        
//        // Iterate over each row in the sheet
//        for (Row row : sheet) {
//            // Skip the header row (assuming the first row contains column names)
//            if (row.getRowNum() == 0) continue;  
//            
//            // Create a new Employee object for each row
//            Employee employee = new Employee();
//            
//            // Populate Employee object with data from the row (adjust indices as per your file structure)
//            employee.setName(row.getCell(0).getStringCellValue());   // Name from the first column
//            employee.setDepartment(row.getCell(1).getStringCellValue());  
//            employee.setDesignation(row.getCell(2).getStringCellValue()); 
//            employee.setPaymentMethod(row.getCell(3).getStringCellValue()); 
//            employee.setSalary(row.getCell(4).getNumericCellValue());   // Salary from the fifth column (numeric)
//            employee.setTaxRate(row.getCell(5).getNumericCellValue());  // Tax rate from the sixth column (numeric)
//            
//            // Add the populated Employee object to the list
//            employeeList.add(employee);
//        }
//        
//        // Close the workbook to free up resources
//        workbook.close();
//        
//        // Return the list of Employee objects
//        return employeeList;
//    }
    

