@echo off
set "JAVA_HOME=C:\java\OpenJDK21U-jdk_x64_windows_hotspot_21.0.12.1_1\jdk-21.0.12.1+1"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo ========================================================
echo Starting PetCareBook - Pet Vaccination Reminder System
echo ========================================================
echo Java Version:
java -version
echo.
echo Launching Spring Boot server on http://localhost:8080 ...
echo ========================================================

call mvnw.cmd spring-boot:run
pause
