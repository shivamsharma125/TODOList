package in.co.softwaresolution.list;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Random;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int RESULT_CODE_EDIT_MENU=1;
    int id;
    EditText title;
    EditText description;
    Button date;
    Button time;
    Button secondsIncrease;
    Bundle bundle;
    int mDay,mMonth,mYear,mHour,mMinutes,mSeconds;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title=findViewById(R.id.editactivityTitleID);
        description=findViewById(R.id.editactivityDescriptionID);
        date=findViewById(R.id.editactivityDateID);
        time=findViewById(R.id.editactivityTimeID);
        secondsIncrease=findViewById(R.id.editactivitysecondIncreaseID);

        date.setOnClickListener(this);
        time.setOnClickListener(this);
        secondsIncrease.setOnClickListener(this);

        Intent intent=getIntent();
        id=intent.getIntExtra(MainActivity.ID_KEY,0);

        InfoOpenHelper openHelper=InfoOpenHelper.getInstance(this);
        database=openHelper.getWritableDatabase();
        String[] selectionArguments={id+""};

        Cursor cursor=database.query(Contract.Info.TABLE_NAME,null,"id = ?",selectionArguments,null,null,null);

        if(cursor.moveToNext()) {
            mDay = cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_DAY));
            mMonth = cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MONTH));
            mYear = cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_YEAR));
            mHour = cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_HOUR));
            mMinutes = cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MIN));
            mSeconds = cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_SECOND));


            title.setText(cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_TITLE)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_DESCRIPTION)));

        }

            date.setText(mDay + "/" + mMonth + "/" + mYear);
            time.setText(mHour + ":" + mMinutes + ":" + mSeconds);

        if(MyReceiver.isNotified == true)
        {
            MyReceiver.isNotified=false;

        }

    }


    public void EditInformation(View view) {

        String editedTitle=title.getText().toString();
        String editedDescription=description.getText().toString();


            ContentValues contentValues=new ContentValues();
            contentValues.put(Contract.Info.COLUMN_TITLE,editedTitle);
            contentValues.put(Contract.Info.COLUMN_DESCRIPTION,editedDescription);
            contentValues.put(Contract.Info.COLUMN_DAY,mDay);
            contentValues.put(Contract.Info.COLUMN_MONTH,mMonth);
            contentValues.put(Contract.Info.COLUMN_YEAR,mYear);
            contentValues.put(Contract.Info.COLUMN_HOUR,mHour);
            contentValues.put(Contract.Info.COLUMN_MIN,mMinutes);
            contentValues.put(Contract.Info.COLUMN_SECOND,mSeconds);
            contentValues.put(Contract.Info.IS_ALARMED,false);

            String[] whereArguments={id+""};
            database.update(Contract.Info.TABLE_NAME,contentValues,"id = ?",whereArguments);

            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

            Intent intent=new Intent(this,MyReceiver.class);
            intent.putExtra(MainActivity.ID_KEY,(int)id);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,(int)id+ new Random().nextInt(),intent,0);

            Calendar calendar=Calendar.getInstance();
            calendar.set(mYear,mMonth,mDay,mHour,mMinutes,mSeconds);
            long currentTime=calendar.getTimeInMillis();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                 alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTime, pendingIntent);
             }

        ContentValues contentValues1=new ContentValues();
        contentValues1.put(Contract.Info.IS_ALARMED,true);
        String[] whereArgument={id+""};
        database.update(Contract.Info.TABLE_NAME,contentValues1,"id = ?",whereArgument);

            Intent intent1=new Intent(this,DetailsActivity.class);
            intent1.putExtra(MainActivity.ID_KEY,id);
            startActivity(intent1);
            finish();

    }


    @Override
    public void onClick(View v) {

        Button button=(Button)v;
        if(button == date)
        {
//            mYear=bundle.getInt(MainActivity.DATE_YEAR_KEY);
//            mMonth=bundle.getInt(MainActivity.DATE_MONTH_KEY);
//            mDay=bundle.getInt(MainActivity.DATE_DAY_KEY);



            DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mDay=dayOfMonth;
                    mMonth=month;
                    mYear=year;
                    date.setText(mDay+"/"+mMonth+"/"+mYear);
                }
            }, mYear, mMonth, mDay);
            dialog.show();
        }
        else if(button == time)
        {
//            mMinutes=bundle.getInt(MainActivity.TIME_MIN_KEY);
//            mHour=bundle.getInt(MainActivity.TIME_HOUR_KEY);

            TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour=hourOfDay;
                    mMinutes=minute;
                    time.setText(mHour+":"+mMinutes);
                }
            }, mHour, mMinutes, true);
            dialog.show();
        }
        else if(button == secondsIncrease)
        {
            if(mSeconds < 30)
            {
                mSeconds=mSeconds+30;
                time.setText(mHour+":"+mMinutes+":"+mSeconds);
            }
            else if(mMinutes < 60)
            {
                mMinutes=mMinutes+1;
                mSeconds=30-(60-mSeconds);
                time.setText(mHour+":"+mMinutes+":"+mSeconds);
            }
            else
            {
                mHour=mHour+1;
                mMinutes=0;
                mSeconds=30-(60-mSeconds);
                time.setText(mHour+":"+mMinutes+":"+mSeconds);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtra(MainActivity.ID_KEY,id);
        startActivity(intent);
        finish();

    }
}
