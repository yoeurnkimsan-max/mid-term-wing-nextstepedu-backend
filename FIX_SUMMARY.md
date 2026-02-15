# Multipart Request Fix Summary

## Problem
The application was returning "Failed to parse multipart servlet request" when trying to POST to `/api/v1/universities` with file uploads (multipart form data).

## Root Cause
- Missing multipart servlet configuration in `application-prod.yaml`
- No explicit multipart resolver bean defined
- Inadequate exception handling for multipart errors

## Solution Implemented

### 1. Configuration Changes
**File**: `src/main/resources/application-prod.yaml`

Added the following configuration:
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 20MB
      enabled: true
      location: ${java.io.tmpdir}
  
  tomcat:
    max-http-form-post-size: 20MB
    max-connections: 200
    threads:
      max: 200

logging:
  level:
    org.springframework.web.multipart: DEBUG
    org.apache.commons.fileupload: DEBUG
```

**What it does**:
- Enables multipart request handling
- Sets file size limits (10MB per file, 20MB per request)
- Configures temp directory for file uploads
- Adds debug logging for troubleshooting

### 2. New Multipart Resolver Bean
**File**: `src/main/java/com/NextStepEdu/security/MultipartConfig.java` (NEW)

```java
@Configuration
public class MultipartConfig {
    
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
```

**Purpose**: Explicitly registers Spring's multipart resolver for automatic file upload handling.

### 3. Enhanced Exception Handling
**File**: `src/main/java/com/NextStepEdu/dto/responses/error_response/GlobalExceptionHandler.java`

Added specific handler for `MultipartException`:
```java
@ExceptionHandler(MultipartException.class)
public ResponseEntity<CustomErrorResponse> handleMultipartException(
        MultipartException ex,
        WebRequest request) {
    // Logs full error details for debugging
    // Returns proper error response
}
```

**What it does**:
- Catches multipart parsing errors
- Logs detailed error information for debugging
- Returns proper HTTP 400 response with error message

## Files Modified

1. ✅ `src/main/resources/application-prod.yaml`
   - Added multipart servlet configuration
   - Added Tomcat form post size limits
   - Added debug logging settings

2. ✅ `src/main/java/com/NextStepEdu/security/MultipartConfig.java` (CREATED)
   - New configuration class for multipart resolver

3. ✅ `src/main/java/com/NextStepEdu/dto/responses/error_response/GlobalExceptionHandler.java`
   - Added MultipartException handler
   - Enhanced error logging and reporting

4. ✅ `src/main/resources/application-dev.yaml` (ALREADY HAD multipart config)
   - No changes needed (already correct)

## How to Test

### Option 1: Postman
1. Create new POST request to `http://localhost:8080/api/v1/universities`
2. Go to Body tab, select **form-data**
3. Add fields:
   - name: "Test University"
   - slug: "test-university"
   - description: "Test Description"
   - country: "USA"
   - city: "New York"
   - officialWebsite: "https://example.com"
   - status: "ACTIVE"
   - logoUrl: (select an image file)
   - coverImageUrl: (select an image file)
   - label: "Contact"
   - email: "contact@example.com"
   - phone: "+1234567890"
   - websiteUrl: "https://contact.example.com"
4. Add Authorization header with Bearer token
5. Send the request

### Option 2: Command Line
```bash
curl -X POST http://localhost:8080/api/v1/universities \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "name=Test University" \
  -F "slug=test-university" \
  -F "description=Test description" \
  -F "country=USA" \
  -F "city=New York" \
  -F "officialWebsite=https://example.com" \
  -F "status=ACTIVE" \
  -F "logoUrl=@/path/to/image.jpg" \
  -F "coverImageUrl=@/path/to/image.jpg" \
  -F "label=Contact" \
  -F "email=contact@example.com" \
  -F "phone=+1234567890" \
  -F "websiteUrl=https://contact.example.com"
```

## Build & Run

```bash
# Set Java home
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/NextStepEdu-0.0.1-SNAPSHOT.jar
```

## Verification

- The CloudinaryImageUploadController (`/api/v1/cloud/upload`) should work as a baseline test
- All endpoints using `@RequestParam(value = "...", required = false) MultipartFile` should now work
- The `/api/v1/universities` POST endpoint should accept multipart file uploads
- Images should be uploaded to Cloudinary automatically

## Important Notes

⚠️ **DO NOT** manually set the `Content-Type` header in Postman
- When using form-data, Postman automatically sets the correct Content-Type header with boundary
- Manually setting it will likely cause the multipart parsing error

✅ **Always** use `form-data` in Postman, NOT raw JSON

✅ **File size limits** are set to:
- 10MB per file
- 20MB per request
- Adjust in config if needed

✅ **Debug logging** is enabled for `org.springframework.web.multipart` and `org.apache.commons.fileupload`
- Check application logs for detailed error information if issues persist

## Additional Resources

See `MULTIPART_FIX_GUIDE.md` for more detailed troubleshooting guide.

