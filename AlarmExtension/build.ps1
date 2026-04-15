# Build script for AIX files - MIT App Inventor Format

$ErrorActionPreference = "Stop"

Write-Host "Creating AIX files..."

# Clean up
if (Test-Path "AlarmExtension.aix") { Remove-Item AlarmExtension.aix }
if (Test-Path "AlarmExtension-sources.aix") { Remove-Item AlarmExtension-sources.aix }
if (Test-Path "build_aix") { Remove-Item -Recurse build_aix }

# Format 1: Source-based AIX (for Niotron/Kodular)
Write-Host "Creating source-based AIX..."
Compress-Archive -Path "src\com\arthur\alarm\AlarmExtension.java","src\com\arthur\alarm\AlarmReceiver.java","extension.yml" -DestinationPath "AlarmExtension-sources.zip" -Force
Move-Item -Force "AlarmExtension-sources.zip" "AlarmExtension-sources.aix"

# Format 2: YoungAndroid format - MIT App Inventor standard
Write-Host "Creating YoungAndroid format AIX..."
New-Item -ItemType Directory -Path "build_aix" -Force | Out-Null

# Copy all necessary files
Copy-Item -Recurse src build_aix\src
Copy-Item -Recurse assets build_aix\assets
Copy-Item aiwebfile.xml build_aix\
Copy-Item component.json build_aix\
Copy-Item components.json build_aix\
Copy-Item extension.yml build_aix\

# Create AIX
Get-ChildItem build_aix | Compress-Archive -DestinationPath "AlarmExtension.zip" -Force
Move-Item -Force "AlarmExtension.zip" "AlarmExtension.aix"
Remove-Item -Recurse build_aix

# Format 3: Simple format with just component.json at root
Write-Host "Creating simple format AIX..."
New-Item -ItemType Directory -Path "build_simple" -Force | Out-Null
Copy-Item component.json build_simple\component.json
Copy-Item -Recurse src build_simple\src
Copy-Item -Recurse assets build_simple\assets

Get-ChildItem build_simple | Compress-Archive -DestinationPath "AlarmExtension-simple.zip" -Force
Move-Item -Force "AlarmExtension-simple.zip" "AlarmExtension-simple.aix"
Remove-Item -Recurse build_simple

Write-Host ""
Write-Host "Created:"
Write-Host "  - AlarmExtension.aix (YoungAndroid format)"
Write-Host "  - AlarmExtension-sources.aix (Source format)"
Write-Host "  - AlarmExtension-simple.aix (Simple format)"
Write-Host ""
Write-Host "Try importing in this order:"
Write-Host "  1. AlarmExtension-simple.aix"
Write-Host "  2. AlarmExtension.aix"
Write-Host ""
Write-Host "NOTE: These contain SOURCE CODE. For a working extension, use:"
Write-Host "  - Niotron: https://community.niotron.com/"
Write-Host "  - Kodular: https://www.kodular.io/"
