# PowerShell script to run the application with Tomcat file count limit increased
# This allows the multipart endpoints to accept many form fields

Write-Host "Starting NextStepEdu with increased Tomcat file count limit..." -ForegroundColor Green
Write-Host "This command increases maxFileCount to 1000 to allow ~14 multipart parts" -ForegroundColor Yellow
Write-Host ""

# Run Maven Spring Boot with JVM argument
mvn spring-boot:run "-Dspring-boot.run.jvmArguments=-Dorg.apache.catalina.fileupload.maxFileCount=1000"

