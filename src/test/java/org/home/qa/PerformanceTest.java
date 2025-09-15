package org.home.qa;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;
import static us.abstracta.jmeter.javadsl.dashboard.DashboardVisualizer.*;

import java.io.IOException;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import us.abstracta.jmeter.javadsl.core.TestPlanStats;

public class PerformanceTest {

	@Test
	public void testPerformance() throws IOException {
		TestPlanStats stats = 
				testPlan(
						threadGroup(2, 10, 
								httpSampler("home","https://the-internet.herokuapp.com/")
						),
						threadGroup(2, 10,		
								httpSampler("login","https://the-internet.herokuapp.com/login")
						),
						threadGroup(2, 10,		
								httpSampler("forgotPassword","https://the-internet.herokuapp.com/forgot_password")
						)
						, dashboardVisualizer(),
				// this is just to log details of each request stats
						jtlWriter("target/jtls")
				).run();
		assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
	}

}
