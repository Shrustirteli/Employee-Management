package com.employee.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.employee.entity.Payroll;

@Component
public class PayrollWriter implements ItemWriter<Payroll> {
	
    public void write(List<? extends Payroll> payrolls) throws Exception {
        payrolls.forEach(System.out::println); // Replace with DB save logic.
    }

	@Override
	public void write(Chunk<? extends Payroll> chunk) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
