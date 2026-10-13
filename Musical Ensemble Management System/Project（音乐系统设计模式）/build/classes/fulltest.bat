@echo off
echo ============================================
echo    MEMS Full Automated Test
echo ============================================
echo.

cd /d "C:\Users\user\Desktop\Project\src"

echo Running test with input file...
java MEMS < test_input.txt

echo.
echo ============================================
echo    Full Test Completed!
echo ============================================
echo.
pause