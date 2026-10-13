@echo off
chcp 65001 >nul
title Music Ensembles Management System (MEMS)

echo ========================================================
echo    Music Ensembles Management System (MEMS)
echo    Hong Kong Institute of Information Technology
echo ========================================================
echo.

echo Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java JDK and try again.
    pause
    exit /b 1
)

echo Java found! Starting compilation...
echo Cleaning up previous class files...
del *.class >nul 2>&1

echo Compiling all Java source files...
javac -encoding UTF-8 *.java

if errorlevel 1 (
    echo.
    echo ========================================
    echo        COMPILATION FAILED!
    echo ========================================
    echo Please fix the compilation errors above.
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo        COMPILATION SUCCESSFUL!
echo ========================================
echo.
echo Starting Music Ensembles Management System...
echo.

java MEMS

if errorlevel 1 (
    echo.
    echo ========================================
    echo    SYSTEM EXECUTION FAILED!
    echo ========================================
    echo The program terminated with an error.
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo    PROGRAM EXITED SUCCESSFULLY
echo ========================================
pause