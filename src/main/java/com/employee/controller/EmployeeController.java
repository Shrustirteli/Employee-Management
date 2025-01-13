package com.employee.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.employee.entity.Employee;
import com.employee.entity.Payroll;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.PayrollRepository;
import com.employee.service.EmployeeService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService service;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PayrollRepository payrollRepository;

    
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return service.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
    }
    
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            service.saveDataFromExcel(file.getInputStream());
            return ResponseEntity.ok("File uploaded and data saved!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }
    
    @Transactional
    @PostMapping("/calculatePayroll")
    public ResponseEntity<String> calculatePayroll() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            double salary = employee.getSalary();
            double taxRate = employee.getTaxRate();
            double netSalary = salary - (salary * taxRate / 100);

            Payroll payroll = new Payroll(employee, netSalary);
            payrollRepository.save(payroll);  // Save the payroll entry
            
        }
        return ResponseEntity.ok("Payroll calculated and updated successfully.");
    
}
    
    @GetMapping("/downloadPayroll")
    public void downloadPayrollExcel(HttpServletResponse response) throws IOException {
        // 1. Set Response for Excel File Download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Employee_Payroll.xlsx";
        response.setHeader(headerKey, headerValue);

        // 2. Create Excel Workbook and Sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee Payroll");

        // 3. Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Name", "Department", "Designation", "Payment Method", "Salary", "Tax Rate", "Net Salary"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // 4. Fetch All Employees
        List<Employee> employees = employeeRepository.findAll();

        int rowNum = 1;

        // 5. Populate Data Rows
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);

            // Fetch Payroll for each Employee
            Payroll payroll = payrollRepository.findByEmployeeId(employee.getId());

            // Fill Employee Data
            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getName());
            row.createCell(2).setCellValue(employee.getDepartment());
            row.createCell(3).setCellValue(employee.getDesignation());
            row.createCell(4).setCellValue(employee.getPaymentMethod());
            row.createCell(5).setCellValue(employee.getSalary());
            row.createCell(6).setCellValue(employee.getTaxRate());

            // Fill Net Salary (If Payroll Exists)
            if (payroll != null && payroll.getNetSalary() != null) {
                row.createCell(7).setCellValue(payroll.getNetSalary());
            } else {
                row.createCell(7).setCellValue(0.0);  // Default if no payroll
            }
        }

        // 6. Auto-size Columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 7. Write Workbook to Response
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}



























//@GetMapping("/downloadPayroll")
//public void downloadEmployeePayrollExcel(HttpServletResponse response) throws IOException {
//  response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//  response.setHeader("Content-Disposition", "attachment; filename=employee_payroll.xlsx");
//
//  try (ByteArrayInputStream excelStream = service.downloadEmployeePayrollDataAsExcel()) {
//      byte[] buffer = new byte[1024];
//      int bytesRead;
//
//      while ((bytesRead = excelStream.read(buffer)) != -1) {
//          response.getOutputStream().write(buffer, 0, bytesRead);
//      }
//      response.getOutputStream().flush();
//  } catch (IOException e) {
//      throw new RuntimeException("Failed to download Excel file: " + e.getMessage());
//  }
//}


//@PostMapping("/saveWithPayroll")
//public ResponseEntity<Employee> saveEmployeeWithPayroll(@RequestBody Employee employee) {
//  try {
//      Employee savedEmployee = service.saveEmployeeWithPayroll(employee);
//      return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
//  } catch (Exception e) {
//      e.printStackTrace();
//      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//  }
//}

//@PostMapping("/upload")
//public String uploadFile(@RequestPart("file") MultipartFile file) {
//  try {
//      BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//      reader.lines().forEach(line -> {
//          System.out.println("Processing line: " + line);
//          String[] fields = line.split(",");
//          if (fields.length >= 7) {
//              Employee employee = new Employee();
//              employee.setEmpId(Integer.parseInt(fields[0]));
//              employee.setName(fields[1]);
//              employee.setDesignation(fields[2]);
//              employee.setBasicSalary(Double.parseDouble(fields[3]));
//              employee.setDepartment(fields[4]);
//              employee.setPaymentMethod(fields[5]);
//              employee.setTaxRate(Double.parseDouble(fields[6]));
//              System.out.println("Parsed Employee: " + employee);
//              employeeRepository.save(employee);
//          } else {
//              System.out.println("Skipping line due to insufficient fields: " + line);
//          }
//      });
//      return "File uploaded and data saved successfully.";
//  } catch (Exception e) {
//      e.printStackTrace();
//      return "Failed to process file: " + e.getMessage();
//  }
//}
//

