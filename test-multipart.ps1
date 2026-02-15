#!/usr/bin/env pwsh

# Test script for University POST endpoint with multipart form data
# This script sends a proper multipart request to test the fix

$apiUrl = "http://localhost:8080/api/v1/universities"
$authToken = "YOUR_JWT_TOKEN_HERE"  # Replace with actual token

# Create a temporary test image file
$tempImagePath = "$env:TEMP\test-image.jpg"
$imageBytes = @(255, 216, 255, 224, 0, 16, 74, 70, 73, 70, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0)
[System.IO.File]::WriteAllBytes($tempImagePath, $imageBytes)

try {
    # Create multipart form
    $body = @{
        name = "Test University"
        slug = "test-university"
        description = "This is a test university"
        country = "USA"
        city = "New York"
        officialWebsite = "https://example.com"
        status = "ACTIVE"
        label = "Contact"
        email = "contact@example.com"
        phone = "+1234567890"
        websiteUrl = "https://contact.example.com"
        logoUrl = Get-Item -Path $tempImagePath
        coverImageUrl = Get-Item -Path $tempImagePath
    }

    $response = Invoke-WebRequest `
        -Uri $apiUrl `
        -Method POST `
        -Form $body `
        -Headers @{
            "Authorization" = "Bearer $authToken"
        } `
        -Verbose

    Write-Host "Response Status: $($response.StatusCode)"
    Write-Host "Response Body:" $response.Content
}
catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Response Content:" $_.Exception.Response.Content.ReadAsStream()
}
finally {
    # Clean up
    if (Test-Path $tempImagePath) {
        Remove-Item $tempImagePath
    }
}

