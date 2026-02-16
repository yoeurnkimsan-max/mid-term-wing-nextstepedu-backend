package com.NextStepEdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NextStepEduApplication {

	// Static initializer to set Tomcat limits at class load time - RUNS BEFORE ANYTHING ELSE
	static {
		System.out.println("⚙️  INITIALIZING TOMCAT LIMITS - Running at JVM startup...");
		try {
			// Set very high limits to avoid FileCountLimitExceededException
			String maxFileCount = "100000";
			String maxPostSize = "52428800"; // 50MB

			// Try all possible property names that Tomcat might use
			System.setProperty("org.apache.tomcat.util.http.fileupload.impl.FileUploadBase.maxFileCount", maxFileCount);
			System.setProperty("org.apache.catalina.fileupload.maxFileCount", maxFileCount);
			System.setProperty("org.apache.catalina.connector.maxPostSize", maxPostSize);
			System.setProperty("org.apache.catalina.connector.maxSavePostSize", maxPostSize);
			System.setProperty("org.apache.catalina.connector.maxHttpHeaderSize", maxPostSize);
			System.setProperty("catalina.connector.maxFileCount", maxFileCount);

			System.out.println("✅ All Tomcat file upload limits set to: " + maxFileCount);
			System.out.println("✅ Post size limits set to: " + maxPostSize + " bytes (50MB)");

		} catch (Exception e) {
			System.err.println("❌ Error setting Tomcat limits: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("\n🚀 Starting NextStepEdu Application...\n");
		SpringApplication.run(NextStepEduApplication.class, args);
	}

}






