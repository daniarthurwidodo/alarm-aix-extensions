package com.arthur.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm triggered!");

        String message = intent.getStringExtra("message");
        if (message == null) {
            message = "Alarm!";
        }

        // Show a toast notification
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        // Play alarm sound
        try {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
            if (ringtone != null) {
                ringtone.play();
                // Stop after 30 seconds
                final Ringtone ringtoneRef = ringtone;
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ringtoneRef != null && ringtoneRef.isPlaying()) {
                            ringtoneRef.stop();
                        }
                    }
                }, 30000);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing alarm sound", e);
        }

        // You can also start an activity here if needed
        // Intent activityIntent = new Intent(context, AlarmActivity.class);
        // activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // context.startActivity(activityIntent);
    }
}
