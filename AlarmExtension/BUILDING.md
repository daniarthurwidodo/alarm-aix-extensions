# Quick Build Guide for AlarmExtension
\n
## Option 1: Use Niotron Extension Builder (Easiest)\n
1. Go to https://community.niotron.com/\n
2. Create a new extension project\n
3. Copy the contents of `src/com/arthur/alarm/` into the project\n
4. Build and download the `.aix` file\n
\n
## Option 2: Use Kodular\n
1. Go to https://www.kodular.io/\n
2. Create a new extension\n
3. Paste the Java code from `AlarmExtension.java` and `AlarmReceiver.java`\n
4. Build the extension\n
\n
## Option 3: Use MIT App Inventor Build Server\n
If you have access to the MIT App Inventor build server:\n
```bash\n
java_build_extension -i extension.yml -o AlarmExtension.aix\n
```\n
\n
## Option 4: Use AIX Generator\n
1. Go to https://aixgenerator.xyz/\n
2. Enter the extension details\n
3. Paste the Java code\n
4. Generate and download\n
\n
## Option 5: MIT App Inventor Extension Template (Official)
1. Clone the template:
   ```bash
   git clone https://github.com/mit-cml/extension-template
   ```
2. Copy the source files:
   ```bash
   cp src/com/arthur/alarm/*.java extension-template/src/com/arthur/alarm/
   ```
3. Add the package to `extension-template/src/MANIFEST`:
   ```
   com.arthur.alarm.AlarmExtension
   ```
4. Build:
   ```bash
   cd extension-template
   ant extensions
   ```
5. Output is in `extension-template/out/com/arthur/alarm/AlarmExtension.aix`

> Prerequisites: Java 8+, Apache Ant, Android SDK.

## Source Files\n
- `src/com/arthur/alarm/AlarmExtension.java` - Main extension class\n
- `src/com/arthur/alarm/AlarmReceiver.java` - Alarm broadcast receiver\n
- `extension.yml` - Build configuration\n
\n
## Extension API\n
\n
### Methods:\n
- `SetAlarm(year, month, day, hour, minute, message)` - Set an alarm\n
- `CancelAlarm()` - Cancel the alarm\n
\n
### Events:\n
- `AlarmSet(message)` - When alarm is set\n
- `AlarmError(message)` - On error\n
- `AlarmCancelled(message)` - When alarm is cancelled
