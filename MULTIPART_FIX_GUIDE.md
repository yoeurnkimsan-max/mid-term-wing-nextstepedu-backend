# Multipart Request Fix - Complete Guide

## What Was Done

### 1. Added Multipart Configuration
- **File**: `src/main/resources/application-prod.yaml`
- **Changes**: 
  - Added `spring.servlet.multipart` settings with max-file-size and max-request-size
  - Added Tomcat configuration for form post size
  - Added debug logging for multipart operations

### 2. Created MultipartConfig Bean
- **File**: `src/main/java/com/NextStepEdu/security/MultipartConfig.java`
- **Purpose**: Explicitly configure StandardServletMultipartResolver for better multipart handling

### 3. Enhanced Exception Handling
- **File**: `src/main/java/com/NextStepEdu/dto/responses/error_response/GlobalExceptionHandler.java`
- **Added**: Specific MultipartException handler to capture and log multipart errors

## Common Issues & Solutions

### Issue: "Failed to parse multipart servlet request" 
**Causes:**
1. **Malformed multipart boundary** - The request body doesn't match the Content-Type boundary
2. **Incorrect Content-Type header** - Missing or wrong boundary
3. **File too large** - Exceeds configured limits
4. **Memory issues** - Insufficient temp space

### How to Fix in Postman

#### Method 1: Using Form-Data (Recommended)
1. Open POST request to `http://localhost:8080/api/v1/universities`
2. Go to **Body** tab
3. Select **form-data** (NOT raw JSON)
4. Add fields:
   - `name` (text): "University Name"
   - `slug` (text): "university-slug"
   - `description` (text): "Description"
   - `country` (text): "USA"
   - `city` (text): "New York"
   - `officialWebsite` (text): "https://example.com"
   - `status` (text): "ACTIVE"
   - `logoUrl` (file): Select actual image file
   - `coverImageUrl` (file): Select actual image file
   - `label` (text): "Contact Info"
   - `email` (text): "contact@example.com"
   - `phone` (text): "+1234567890"
   - `websiteUrl` (text): "https://contact.example.com"

5. **IMPORTANT**: Do NOT set Content-Type header manually
   - Postman will automatically set it to `multipart/form-data` with the correct boundary
   - If you set it manually, you must include the boundary

#### Method 2: Using curl
```bash
curl -X POST http://localhost:8080/api/v1/universities \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "name=Test University" \
  -F "slug=test-uni" \
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

#### Method 3: Using PowerShell
Run the included test script:
```powershell
.\test-multipart.ps1
```

## Configuration Details

### application-prod.yaml
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB        # Max size for a single file
      max-request-size: 20MB     # Max size for entire request
      enabled: true              # Enable multipart handling
      location: ${java.io.tmpdir} # Temp directory for uploads
  
  tomcat:
    max-http-form-post-size: 20MB # Tomcat level limit
```

### Debug Logging
Enabled DEBUG level logging for:
- `org.springframework.web.multipart` - Spring's multipart processing
- `org.apache.commons.fileupload` - Apache commons file upload

Check application logs when requests fail to see detailed error messages.

## Verification Steps

1. **Build the project**:
   ```bash
   mvn clean package -DskipTests
   ```

2. **Start the application**:
   ```bash
   java -jar target/NextStepEdu-0.0.1-SNAPSHOT.jar
   ```

3. **Test with the CloudinaryImageUploadController first**:
   ```bash
   curl -X POST http://localhost:8080/api/v1/cloud/upload \
     -F "image=@/path/to/test-image.jpg"
   ```

4. **If that works, test the University endpoint** using one of the methods above

## Troubleshooting

### If you still get "Failed to parse multipart servlet request":

1. **Check the request Content-Type**:
   - Must include boundary: `multipart/form-data; boundary=----WebKitFormBoundary...`
   - Do NOT manually set this in Postman - let it auto-generate

2. **Check file size**:
   - Ensure files are under 10MB
   - Ensure total request is under 20MB

3. **Check temp directory**:
   - Ensure `java.io.tmpdir` exists and has write permissions
   - On Windows, this is usually `C:\Users\{username}\AppData\Local\Temp`

4. **Enable full debug logging**:
   Add to application-prod.yaml:
   ```yaml
   logging:
     level:
       root: DEBUG
   ```

5. **Check server logs** for the actual exception message - this will tell you the real cause

## Files Modified

1. ✅ `src/main/resources/application-prod.yaml` - Added multipart config
2. ✅ `src/main/java/com/NextStepEdu/security/MultipartConfig.java` - Created
3. ✅ `src/main/java/com/NextStepEdu/dto/responses/error_response/GlobalExceptionHandler.java` - Enhanced

## Additional Notes

- The `/api/v1/cloud/upload` endpoint should work as a baseline test
- All image files are uploaded to Cloudinary automatically
- No local file storage is performed
- The multipart configuration applies to ALL endpoints that accept file uploads


