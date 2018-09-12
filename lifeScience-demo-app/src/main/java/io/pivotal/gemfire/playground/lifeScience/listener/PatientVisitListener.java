package io.pivotal.gemfire.playground.lifeScience.listener;

import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.ContinuousQueryListener;

public class PatientVisitListener implements ContinuousQueryListener {

	@Override
	public void onEvent(CqEvent cqevent)
	{
		System.out.println("***** Listener "+getClass().getSimpleName()+" KEY "+cqevent.getKey()+" *****");
		
	}
    

}
