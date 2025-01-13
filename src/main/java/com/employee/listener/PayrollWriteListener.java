package com.employee.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import com.employee.entity.Payroll;

@Component
public class PayrollWriteListener implements ItemWriteListener<Payroll> {
	
    public void onWriteError(Exception exception, List<? extends Payroll> items) {
        System.err.println("Error writing items: " + items);
    }

	public void beforeWrite(List<? extends Payroll> items) {
		// TODO Auto-generated method stub
		
	}

	public void afterWrite(List<? extends Payroll> items) {
		// TODO Auto-generated method stub
		
	}
}
