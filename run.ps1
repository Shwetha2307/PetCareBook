$env:JAVA_HOME = "C:\java\OpenJDK21U-jdk_x64_windows_hotspot_21.0.12.1_1\jdk-21.0.12.1+1"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host " Starting PetCareBook - Pet Vaccination Reminder System" -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan
& "$env:JAVA_HOME\bin\java.exe" -version
Write-Host "`nLaunching Spring Boot application on http://localhost:8080 ...`n" -ForegroundColor Yellow

.\mvnw.cmd spring-boot:run
