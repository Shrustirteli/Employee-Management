package com.employee.skip;

import org.hibernate.cfg.RecoverableException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Component;

@Component
public class CustomSkipPolicy implements SkipPolicy {
	
    public boolean shouldSkip(Throwable t, int skipCount) {
        return t instanceof RecoverableException && skipCount <= 3;
    }

	@Override
	public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
		// TODO Auto-generated method stub
		return false;
	}
}
