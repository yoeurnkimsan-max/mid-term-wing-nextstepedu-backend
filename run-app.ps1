# PowerShell script to run the application with proper Tomcat multipart limits
# This sets the JVM argument BEFORE the application starts

$env:JAVA_OPTS = "-Dorg.apache.tomcat.util.http.fileupload.impl.FileUploadBase.maxFileCount=100000"
Write-Host "Starting NextStepEdu with JAVA_OPTS: $env:JAVA_OPTS" -ForegroundColor Green
mvn spring-boot:run

