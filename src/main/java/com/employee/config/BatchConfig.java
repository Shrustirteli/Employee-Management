package com.employee.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.employee.entity.Employee;
import com.employee.entity.Payroll;
import com.employee.listener.PayrollWriteListener;
import com.employee.skip.CustomSkipPolicy;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job payrollJob(Step payrollStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("payrollJob", jobRepository)
                .start(payrollStep)
                .build();
    }

    @Bean
    public Step payrollStep(ItemReader<Employee> reader,
                            ItemProcessor<Employee, Payroll> processor,
                            ItemWriter<Payroll> writer) {
        return new StepBuilder("payrollStep", jobRepository)
                .<Employee, Payroll>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .listener(new PayrollWriteListener())
                .build();
    }
}
