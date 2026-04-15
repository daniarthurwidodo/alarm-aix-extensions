#!/bin/bash
# Alternative build script that creates AIX in multiple formats

cd "$(dirname "$0")"

echo "Creating AIX files..."

# Clean up
rm -f AlarmExtension.aix AlarmExtension-sources.aix

# Format 1: Source-based AIX (for Niotron/Kodular)
echo "Creating source-based AIX..."
zip -r AlarmExtension-sources.aix \
    src/com/arthur/alarm/AlarmExtension.java \
    src/com/arthur/alarm/AlarmReceiver.java \
    extension.yml

# Format 2: YoungAndroid format with aiwebfile
echo "Creating YoungAndroid format AIX..."
mkdir -p build_aix
cp -r src build_aix/
cp -r assets build_aix/
cp aiwebfile.xml build_aix/
cp components.json build_aix/
cd build_aix
zip -r ../AlarmExtension.aix .
cd ..
rm -rf build_aix

echo ""
echo "Created:"
echo "  - AlarmExtension.aix (YoungAndroid format)"
echo "  - AlarmExtension-sources.aix (Source format)"
echo ""
echo "Try importing AlarmExtension.aix first."
echo "If that fails, use the sources AIX with Niotron/Kodular."
