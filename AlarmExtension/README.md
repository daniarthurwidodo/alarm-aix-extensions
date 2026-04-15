# Alarm Extension for MIT App Inventor

> **IMPORTANT:** This extension requires compilation before it can be imported into MIT App Inventor.
> The `.aix` file included in this repository contains **source code** and must be built using one of the methods below.

## Quick Start - Use Pre-Built Extension (Recommended)

If you just want to use the extension without building it yourself, you have a few options:

### Option 1: Use Niotron (Easiest - No Code Required)
1. Go to [Niotron Community](https://community.niotron.com/)
2. Search for "Alarm Extension" or create one using their template
3. Download the ready-to-use `.aix` file

### Option 2: Build This Extension

#### Step 1: Copy the Java Code

**AlarmExtension.java** (from `src/com/arthur/alarm/`):
```java
package com.arthur.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.ApiLevelUtil;

@DesignerComponent(
    version = 1,
    description = "Extension to set alarms by date and time",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    androidMinSdk = 10)
@SimpleObject(exclude = true)
public class AlarmExtension extends AndroidNonvisibleComponent implements Component {

    private Context context;
    private AlarmManager alarmManager;

    public AlarmExtension(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @SimpleFunction(description = "Set an alarm for a specific date and time")
    public void SetAlarm(int year, int month, int day, int hour, int minute, String message) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        long alarmTime = calendar.getTimeInMillis();

        if (alarmTime <= System.currentTimeMillis()) {
            EventDispatcher.dispatchEvent(this, "AlarmError", "The selected time is in the past!");
            return;
        }

        if (ApiLevelUtil.isAtLeast23(context)) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else if (ApiLevelUtil.isAtLeast19(context)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }

        EventDispatcher.dispatchEvent(this, "AlarmSet", "Alarm set successfully!");
    }

    @SimpleFunction(description = "Cancel all alarms")
    public void CancelAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        EventDispatcher.dispatchEvent(this, "AlarmCancelled", "Alarm cancelled");
    }
}
```

**AlarmReceiver.java**:
```java
package com.arthur.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Toast.makeText(context, message != null ? message : "Alarm!", Toast.LENGTH_LONG).show();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        if (ringtone != null) ringtone.play();
    }
}
```

#### Step 2: Use an Online Extension Builder

**A. Using AIX Generator**
1. Go to https://aixgenerator.xyz/
2. Click "Create Extension"
3. Paste the Java code above
4. Download the `.aix` file

**B. Using Kodular**
1. Go to https://www.kodular.io/
2. Create a new project
3. Go to Extensions > Create Extension
4. Paste the Java code
5. Build and download

**C. Using Niotron Blaze**
1. Go to https://community.niotron.com/
2. Use Blaze AI to create the extension
3. Download the `.aix` file

## Installation (After Building)

Once you have a compiled `.aix` file:

1. Open [MIT App Inventor](https://appinventor.mit.edu/)
2. Click **Extensions** > **Import extension (.aix file)**
3. Select your `.aix` file
4. The extension appears under **Extensions** in the Palette

## Usage with DatePicker

```
when DatePicker1.AfterDateSet do:
    call AlarmExtension1.SetAlarm(
        DatePicker1.Year,
        DatePicker1.Month,
        DatePicker1.Day,
        TimePicker1.Hour,
        TimePicker1.Minute,
        "Wake up!"
    )

when AlarmExtension1.AlarmSet do (message):
    show message "Alarm set!"

when AlarmExtension1.AlarmError do (message):
    show message "Error: " + message
```

## API Reference

| Method | Description |
|--------|-------------|
| `SetAlarm(year, month, day, hour, minute, message)` | Set alarm for specific date/time |
| `CancelAlarm()` | Cancel the set alarm |

| Event | Description |
|-------|-------------|
| `AlarmSet(message)` | Triggered when alarm is set |
| `AlarmError(message)` | Triggered on error |
| `AlarmCancelled(message)` | Triggered when cancelled |

## Files in This Repository

```
AlarmExtension/
├── src/com/arthur/alarm/
│   ├── AlarmExtension.java    # Main extension (copy this)
│   └── AlarmReceiver.java     # Alarm receiver (copy this)
├── extension.yml               # Build config for MIT AI build server
├── aiwebfile.xml              # Extension manifest
└── README.md                  # This file
```

## License

Apache 2.0
