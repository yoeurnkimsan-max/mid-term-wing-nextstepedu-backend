$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
Set-Location "C:\Users\CHORY Chanrady\Desktop\Midterm Project\mid-term-wing-nextstepedu-backend"

Write-Host "Starting Maven test..." -ForegroundColor Green
Write-Host "Java Home: $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host "Current Dir: $(Get-Location)" -ForegroundColor Cyan
Write-Host ""

$output = & mvn test 2>&1
$output | Out-File -FilePath "test_result.txt" -Encoding UTF8

# Show last 50 lines
Write-Host "=== TEST OUTPUT (Last 50 lines) ===" -ForegroundColor Yellow
$output | Select-Object -Last 50

if ($LASTEXITCODE -eq 0) {
    Write-Host "BUILD SUCCESS" -ForegroundColor Green
} else {
    Write-Host "BUILD FAILED with exit code: $LASTEXITCODE" -ForegroundColor Red
}

