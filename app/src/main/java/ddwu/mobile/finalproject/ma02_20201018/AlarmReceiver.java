package ddwu.mobile.finalproject.ma02_20201018;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title;
        title = intent.getStringExtra("title");
        Intent BRIntent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, BRIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_CHANNEL")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 100;
        notificationManager.notify(notificationId, builder.build());
    }
}
