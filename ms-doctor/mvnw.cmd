@echo off
setlocal

set SCRIPT_DIR=%~dp0
set MAVEN_WRAPPER_PROPERTIES=%SCRIPT_DIR%.mvn\wrapper\maven-wrapper.properties

rem Check if mvn is on PATH
where mvn >nul 2>&1
if %ERRORLEVEL%==0 (
    mvn %*
    exit /b %ERRORLEVEL%
)

set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin
if exist "%MAVEN_HOME%\mvn.cmd" (
    "%MAVEN_HOME%\mvn.cmd" %*
    exit /b %ERRORLEVEL%
)

echo.
echo [ERROR] Maven no encontrado en el PATH.
echo.
echo Para instalar Maven en Windows:
echo   1. Descarga: https://maven.apache.org/download.cgi
echo   2. Extrae en C:\Program Files\Maven
echo   3. Agrega C:\Program Files\Maven\apache-maven-3.9.6\bin al PATH
echo      (Panel de control > Variables de entorno)
echo   4. Reinicia PowerShell y ejecuta: mvn spring-boot:run
echo.
echo Alternativa rapida: instala con winget:
echo   winget install Apache.Maven
echo.
pause
exit /b 1
