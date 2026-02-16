#!/bin/bash
# Wrapper script to run the application with proper Tomcat multipart limits

export JAVA_OPTS="-Dorg.apache.tomcat.util.http.fileupload.impl.FileUploadBase.maxFileCount=100000"

mvn spring-boot:run

