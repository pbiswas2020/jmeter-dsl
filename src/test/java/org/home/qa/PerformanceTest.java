package org.home.qa;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;
import static us.abstracta.jmeter.javadsl.dashboard.DashboardVisualizer.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;

import us.abstracta.jmeter.javadsl.core.TestPlanStats;

public class PerformanceTest {

	@Test
	public void testPerformance() throws IOException {
		TestPlanStats stats = 
				testPlan(
						// CSV Data Set Config
				        csvDataSet("src/test/resources/users.csv")  // file in resources or path
				            .variableNames("username", "password")
				            .ignoreFirstLine(true),
				            
//						threadGroup(2, 10, 
//								httpSampler("home","https://the-internet.herokuapp.com/")
//						),
//						threadGroup(2, 10,		
//								httpSampler("login","https://the-internet.herokuapp.com/login")
//						),
//						threadGroup(2, 10,		
//								httpSampler("forgotPassword","https://the-internet.herokuapp.com/forgot_password")
//						),
						threadGroup(2, 10, 
								httpSampler("home","https://the-internet.herokuapp.com/authenticate")
								.post("{\"${username}\": \"${password}\"", ContentType.APPLICATION_JSON)

						)
						, dashboardVisualizer()
				// this is just to log details of each request stats
						,jtlWriter("target/jtls")
				).run();
		assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
	}

}
