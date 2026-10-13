@echo off
echo ============================================
echo    MEMS Fixed Automated Test
echo ============================================
echo.

cd /d "C:\Users\user\Desktop\Project\src"

echo Recompiling Java files...
javac *.java

echo.
echo Running test with fixed input file...
echo.
java MEMS < test_input_fixed.txt

echo.
echo ============================================
echo    Fixed Test Completed!
echo ============================================
echo.
pause