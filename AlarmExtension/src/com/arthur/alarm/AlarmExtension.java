package com.arthur.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.ApiLevelUtil;

@DesignerComponent(
    version = 1,
    description = "Extension to set alarms by date and time",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    androidMinSdk = 10,
    iconName = "ic_alarm")
@SimpleObject(exclude = true)
public class AlarmExtension extends AndroidNonvisibleComponent implements Component {

    private static final String TAG = "AlarmExtension";
    private Context context;
    private AlarmManager alarmManager;

    public AlarmExtension(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @SimpleFunction(description = "Set an alarm for a specific date and time")
    public void SetAlarm(int year, int month, int day, int hour, int minute, String message) {
        try {
            // Create intent for the alarm
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("message", message);

            // Create PendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Create calendar instance to set the alarm time
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.YEAR, year);
            calendar.set(java.util.Calendar.MONTH, month); // 0-based in Calendar
            calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
            calendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
            calendar.set(java.util.Calendar.MINUTE, minute);
            calendar.set(java.util.Calendar.SECOND, 0);

            long alarmTime = calendar.getTimeInMillis();

            // Check if alarm time is in the past
            if (alarmTime <= System.currentTimeMillis()) {
                EventDispatcher.dispatchEvent(this, "AlarmError",
                    "The selected time is in the past!");
                return;
            }

            // Set the alarm
            if (ApiLevelUtil.isAtLeast23(context)) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                );
            } else if (ApiLevelUtil.isAtLeast19(context)) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                );
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                );
            }

            EventDispatcher.dispatchEvent(this, "AlarmSet",
                    "Alarm set for: " + month + "/" + day + "/" + year + " at " + hour + ":" + minute);

            Log.i(TAG, "Alarm set successfully for: " + alarmTime);

        } catch (Exception e) {
            Log.e(TAG, "Error setting alarm", e);
            EventDispatcher.dispatchEvent(this, "AlarmError",
                    "Error setting alarm: " + e.getMessage());
        }
    }

    @SimpleFunction(description = "Cancel all alarms")
    public void CancelAlarm() {
        try {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(pendingIntent);
            EventDispatcher.dispatchEvent(this, "AlarmCancelled", "Alarm cancelled");
            Log.i(TAG, "Alarm cancelled");
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling alarm", e);
            EventDispatcher.dispatchEvent(this, "AlarmError",
                    "Error cancelling alarm: " + e.getMessage());
        }
    }

    @SimpleEvent(description = "Event triggered when alarm is set successfully")
    public void AlarmSet(String message) {
        EventDispatcher.dispatchEvent(this, "AlarmSet", message);
    }

    @SimpleEvent(description = "Event triggered when there's an error")
    public void AlarmError(String message) {
        EventDispatcher.dispatchEvent(this, "AlarmError", message);
    }

    @SimpleEvent(description = "Event triggered when alarm is cancelled")
    public void AlarmCancelled(String message) {
        EventDispatcher.dispatchEvent(this, "AlarmCancelled", message);
    }
}
