package in.co.softwaresolution.list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class InfoAddActivity extends AppCompatActivity implements View.OnClickListener{

    EditText titleedittext;
    EditText descriptionedittext;
    final Info info=new Info();
    public static final int RESULT_CODE_ADD_MENU=2;
    private int mYear,mMonth,mDay,mHour,mMinutes,mSeconds;
    Button setDate;
    Button setTime;
    Button secondsIncrease;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add);

        Log.d("MyActivity","Log from onCreate() in InfoAddActivity");

        titleedittext=findViewById(R.id.infoaddactivityTitleID);
        descriptionedittext=findViewById(R.id.infoaddactivityDescriptionID);
        setDate=findViewById(R.id.datePickerID);
        setTime=findViewById(R.id.timePickerID);
        secondsIncrease=findViewById(R.id.increaseInSecondsID);

        setDate.setOnClickListener(this);
        setTime.setOnClickListener(this);
        secondsIncrease.setOnClickListener(this);

        mYear=info.getYear();
        mMonth=info.getMonth();
        mDay=info.getDay();

        mMinutes=info.getMin();
        mHour=info.getHour();
        mSeconds=info.getSecond();

        if(MainActivity.ON_RECEIVE == 1)
        {
            Intent intent=getIntent();
            String title=intent.getStringExtra(MainActivity.TITLE_KEY);
            String description=intent.getStringExtra(MainActivity.DESCRIPTION_KEY);
            titleedittext.setText(title);
            descriptionedittext.setText(description);
            MainActivity.ON_RECEIVE=0;
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateActivity(View view)
    {
            String title = titleedittext.getText().toString();
            String description = descriptionedittext.getText().toString();

            Intent intent = new Intent();
            Bundle bundle=new Bundle();
            bundle.putString(MainActivity.TITLE_KEY, title);
            bundle.putString(MainActivity.DESCRIPTION_KEY, description);
            bundle.putInt(MainActivity.DATE_DAY_KEY,info.getDay());
            bundle.putInt(MainActivity.DATE_MONTH_KEY,info.getMonth());
            bundle.putInt(MainActivity.DATE_YEAR_KEY,info.getYear());
            bundle.putInt(MainActivity.TIME_MIN_KEY,info.getMin());
            bundle.putInt(MainActivity.TIME_HOUR_KEY,info.getHour());
            bundle.putInt(MainActivity.TIME_SECOND_KEY,mSeconds);
            intent.putExtras(bundle);
            setResult(RESULT_CODE_ADD_MENU, intent);
            finish();
    }

    @Override
    public void onClick(View v) {

        Button button= (Button) v;
        if(button == setDate)
        {
            DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    info.setDay(dayOfMonth);
                    info.setMonth(month);
                    info.setYear(year);
                    setDate.setText(dayOfMonth+"/"+month+"/"+year);
                }
            }, mYear, mMonth, mDay);
            dialog.show();
        }
        else if(button == setTime)
        {
            TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    info.setHour(hourOfDay);
                    info.setMin(minute);
                    setTime.setText(hourOfDay+":"+minute+":"+mSeconds);
                }
            }, mHour, mMinutes, true);
            dialog.show();
        }
        else if(button == secondsIncrease)
        {
            if(mSeconds < 30)
            {
                mSeconds=mSeconds+30;
                info.setSecond(mSeconds);
                setTime.setText(mHour+":"+mMinutes+":"+mSeconds);
            }
            else if(mMinutes < 60)
            {
                mMinutes=mMinutes+1;
                mSeconds=30-(60-mSeconds);
                info.setMin(mMinutes);
                info.setSecond(mSeconds);
                setTime.setText(mHour+":"+mMinutes+":"+mSeconds);
            }
            else
            {
                mHour=mHour+1;
                mMinutes=0;
                mSeconds=30-(60-mSeconds);
                info.setHour(mHour);
                info.setMin(mMinutes);
                info.setSecond(mSeconds);
                setTime.setText(mHour+":"+mMinutes+":"+mSeconds);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MyActivity","Log from onStart() in InfoAddActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyActivity","Log from onResume() in InfoAddActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MyActivity","Log from onPause() in InfoAddActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MyActivity","Log from onStop() in InfoAddActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MyActivity","Log from onDestroy() in InfoAddActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MyActivity","Log from onRestart() in InfoAddActivity");
    }
}
