@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d "C:\Users\CHORY Chanrady\Desktop\Midterm Project\mid-term-wing-nextstepedu-backend"
mvn test -DskipTests=false -q
echo.
echo =====================================
echo BUILD COMPLETED
echo =====================================
pause

