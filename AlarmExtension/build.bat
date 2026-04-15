@echo off
REM Build script for AIX files using PowerShell

cd /d "%~dp0"

echo Creating AIX files...

REM Clean up
if exist AlarmExtension.aix del AlarmExtension.aix
if exist AlarmExtension-sources.aix del AlarmExtension-sources.aix
if exist build_aix rd /s /q build_aix

REM Format 1: Source-based AIX (for Niotron/Kodular)
echo Creating source-based AIX...
powershell -Command "Compress-Archive -Path 'src\com\arthur\alarm\AlarmExtension.java','src\com\arthur\alarm\AlarmReceiver.java','extension.yml' -DestinationPath 'AlarmExtension-sources.aix' -Force"

REM Format 2: YoungAndroid format with aiwebfile
echo Creating YoungAndroid format AIX...
mkdir build_aix
xcopy /E /I src build_aix\src
xcopy /E /I assets build_aix\assets
copy aiwebfile.xml build_aix\
copy components.json build_aix\
cd build_aix
powershell -Command "Compress-Archive -Path '*' -DestinationPath '../AlarmExtension.aix' -Force"
cd ..
rd /s /q build_aix

echo.
echo Created:
echo   - AlarmExtension.aix (YoungAndroid format)
echo   - AlarmExtension-sources.aix (Source format)
echo.
echo Try importing AlarmExtension.aix first.
echo If that fails, use the sources AIX with Niotron/Kodular.
echo.
pause
