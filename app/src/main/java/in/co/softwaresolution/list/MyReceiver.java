package in.co.softwaresolution.list;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import java.util.Calendar;
import java.util.Random;

public class MyReceiver extends BroadcastReceiver {

    public static boolean isSMSReceived=false;
//    static MainActivity my_context;
    Intent my_intent;
    public static final String CHANNEL_ID="my_channel_ID";
    public static final int NOTIFICATION_ID=1;
    public static boolean isNotified=false;
    String title;
    String description;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if((intent.getAction()!=null) && (intent.getAction()== "android.provider.Telephony.SMS_RECEIVED")) {
            String description = "";
            String title = "";
            isSMSReceived = true;
            Bundle data = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus");
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                description = smsMessage.getDisplayMessageBody();
                title = smsMessage.getDisplayOriginatingAddress();
                intent = new Intent(context, MainActivity.class);
                my_intent = intent;
                intent.putExtra(MainActivity.TITLE_KEY, title);
                intent.putExtra(MainActivity.DESCRIPTION_KEY, description);
//            my_context.readSMS(intent);
            }
            context.startActivity(my_intent);
        }
        else if((intent.getAction()!=null) && (intent.getAction() == Intent.ACTION_BOOT_COMPLETED))
        {
            InfoOpenHelper openHelper=InfoOpenHelper.getInstance(context);
            SQLiteDatabase database=openHelper.getReadableDatabase();
            Cursor cursor=database.query(Contract.Info.TABLE_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext())
            {
                boolean isAlarmed=Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Contract.Info.IS_ALARMED)));
                if(isAlarmed == true)
                {
                    int day=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_DAY));
                    int month=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MONTH));
                    int year=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_YEAR));
                    int hour=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_HOUR));
                    int minutes=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MIN));
                    int seconds=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_SECOND));
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(year,month,day,hour,minutes,seconds);
                    long currentTime=calendar.getTimeInMillis();
                    int id=(int)cursor.getLong(cursor.getColumnIndex(Contract.Info.CLOUMN_ID));

                    AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    Intent intent1=new Intent(context,MyReceiver.class);
                    intent1.putExtra(MainActivity.ID_KEY,id);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(context,(int)id+new Random().nextInt(),intent1,0);

                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                    {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,currentTime,pendingIntent);
                    }

                    ContentValues contentValues1=new ContentValues();
                    contentValues1.put(Contract.Info.IS_ALARMED,false);
                    String[] whereArguments={id+""};
                    database.update(Contract.Info.TABLE_NAME,contentValues1,"id = ?",whereArguments);
                }
            }
        }
        else
        {
            NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            {
                NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"My Channel",NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }

            isNotified=true;
            int id=intent.getIntExtra(MainActivity.ID_KEY,0);
            InfoOpenHelper openHelper=InfoOpenHelper.getInstance(context);
            SQLiteDatabase database=openHelper.getReadableDatabase();

            String[] columns={Contract.Info.COLUMN_TITLE,Contract.Info.COLUMN_DESCRIPTION};
            String[] selectionArguments={id+""};
            Cursor cursor=database.query(Contract.Info.TABLE_NAME,columns,"id = ?",selectionArguments,null,null,null);

            if(cursor.moveToNext()) {
                title = cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_TITLE));
                description = cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_DESCRIPTION));

            }

            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"my_channel_ID");
            builder.setContentTitle(title);
            builder.setContentText(description);
            builder.setSmallIcon(R.drawable.ic_launcher_background);

            Intent intent1=new Intent(context,EditActivity.class);
            intent1.putExtra(MainActivity.ID_KEY,id);
            PendingIntent pendingIntent=PendingIntent.getActivity(context, new Random().nextInt(),intent1,0);

            builder.setContentIntent(pendingIntent);
            Notification notification=builder.build();
            manager.notify(NOTIFICATION_ID,notification);
        }
    }

//    public void referenceMainActivity(MainActivity context)
//    {
//        my_context=context;
//    }
}

