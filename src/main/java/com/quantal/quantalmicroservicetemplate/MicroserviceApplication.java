package com.quantal.quantalmicroservicetemplate;

import de.invesdwin.instrument.DynamicInstrumentationLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.quantal", scanBasePackageClasses = {com.quantal.quantalmicroservicetemplate.config.shared.SharedConfig.class})
public class MicroserviceApplication {

	static {
		//Starts the aspectj weaver so that we can weave the compile time aspects
		DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
		DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave all classes before they are loaded as beans
	}
	public static void main(String[] args) {

		SpringApplication.run(MicroserviceApplication.class, args);
	}
}
