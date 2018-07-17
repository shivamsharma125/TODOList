package in.co.softwaresolution.list;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.FontsContract;
import android.provider.SearchRecentSuggestions;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String TITLE_KEY="title_key";
    public static final String DESCRIPTION_KEY="description_key";
    public static final String DATE_DAY_KEY="date_day_key";
    public static final String DATE_MONTH_KEY="date_month_key";
    public static final String DATE_YEAR_KEY="date_year_key";
    public static final String TIME_HOUR_KEY="time_hour_key";
    public static final String TIME_MIN_KEY="time_min_key";
    public static final String TIME_SECOND_KEY="time_second_key";
    public static final String ID_KEY="id_key";

    public static final int REQUEST_CODE_ADD_MENU=1;
    public static final int REQUEST_CODE_ALARM=3;
    public static final int REQUEST_CODE_EDIT_MENU=2;
    public static final String POSITION="position";
    String string="";
    ArrayList<Info> items=new ArrayList<>();
    DialogAdapter dialogAdapter;
    ListView listView;
    String my_title="";
    String my_description="";
    Intent my_intent;
    FloatingActionButton floatingActionButton;

    public static int ON_RECEIVE=0;
    public static final int REQUEST_CODE_SMS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MyActivity","Log from onCreate() in MainActivity");
//        new MyReceiver().referenceMainActivity(this);

        floatingActionButton = findViewById(R.id.fabID);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,InfoAddActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD_MENU);

            }
        });

        InfoOpenHelper openHelper=InfoOpenHelper.getInstance(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.Info.TABLE_NAME,null,null,null,null,null,null);
        Log.d("MyActivityBeforeCursor","Hello");
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Info.CLOUMN_ID));
            String title=cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_TITLE));
            String description=cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_DESCRIPTION));
            int day=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_DAY));
            int month=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MONTH));
            int year=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_YEAR));
            int min=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MIN));
            int hour=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_HOUR));
            int second=cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_SECOND));
            Info info=new Info(title,description,day,month,year,min,hour,second);
            info.setId(id);
            items.add(info);

        }
        listView=findViewById(R.id.listviewID);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);



        dialogAdapter=new DialogAdapter(MainActivity.this,items);
        listView.setAdapter(dialogAdapter);

       setPermissions();

        my_intent=getIntent();
        if(MyReceiver.isSMSReceived == true)
        {
            MyReceiver.isSMSReceived=false;
            my_title=my_intent.getStringExtra(TITLE_KEY);
            my_description=my_intent.getStringExtra(DESCRIPTION_KEY);
        }
        else {
            my_title = my_intent.getStringExtra(Intent.EXTRA_TEXT);
            my_description=my_intent.getStringExtra(Intent.EXTRA_TEXT);;
        }
        if(my_title!=null)
        {
            ON_RECEIVE=1;
            my_intent=new Intent(this,InfoAddActivity.class);
            my_intent.putExtra(TITLE_KEY,my_title);
            my_intent.putExtra(DESCRIPTION_KEY,my_description);
            startActivityForResult(my_intent,REQUEST_CODE_ADD_MENU);
        }

    }

    public void readSMS(Intent intent)
    {
        ON_RECEIVE=1;
        startActivityForResult(intent, REQUEST_CODE_ADD_MENU);
    }

    public void setPermissions()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            String[] permissions={Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS};
            ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE_SMS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_SMS) {
            if ((grantResults[0] != PackageManager.PERMISSION_GRANTED)|(grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Grant SMS Receive And Read Permission", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id == R.id.settingsID)
        {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD_MENU)
        {
            if(resultCode == InfoAddActivity.RESULT_CODE_ADD_MENU)
            {
                Bundle bundle=data.getExtras();
                String title=bundle.getString(TITLE_KEY);
                String description=bundle.getString(DESCRIPTION_KEY);
                int dateDay=bundle.getInt(DATE_DAY_KEY);
                int dateMonth=bundle.getInt(DATE_MONTH_KEY);
                int dateYear=bundle.getInt(DATE_YEAR_KEY);
                int timeMin=bundle.getInt(TIME_MIN_KEY);
                int timeHour=bundle.getInt(TIME_HOUR_KEY);
                int timeSecond=bundle.getInt(TIME_SECOND_KEY);
                if((!title.isEmpty()) && (!description.isEmpty()))
                {
                    Info info=new Info(title,description,dateDay,dateMonth,dateYear,timeMin,timeHour,timeSecond);
                    items.add(info);
                    InfoOpenHelper openHelper=InfoOpenHelper.getInstance(this);
                    SQLiteDatabase database=openHelper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(Contract.Info.COLUMN_TITLE,title);
                    contentValues.put(Contract.Info.COLUMN_DESCRIPTION,description);
                    contentValues.put(Contract.Info.COLUMN_DAY,dateDay);
                    contentValues.put(Contract.Info.COLUMN_MONTH,dateMonth);
                    contentValues.put(Contract.Info.COLUMN_YEAR,dateYear);
                    contentValues.put(Contract.Info.COLUMN_MIN,timeMin);
                    contentValues.put(Contract.Info.COLUMN_HOUR,timeHour);
                    contentValues.put(Contract.Info.COLUMN_SECOND,timeSecond);
                    contentValues.put(Contract.Info.IS_ALARMED,false);
                    long id = database.insert(Contract.Info.TABLE_NAME,null,contentValues);
                    if(id > -1)
                    {
                        info.setId(id);
                    }
                    dialogAdapter.notifyDataSetChanged();

                    AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

                    Intent intent=new Intent(this,MyReceiver.class);
                    intent.putExtra(MainActivity.ID_KEY,(int)id);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(this,(int)id+new Random().nextInt(),intent,0);

                    Calendar calendar=Calendar.getInstance();
                    calendar.set(dateYear,dateMonth,dateDay,timeHour,timeMin,timeSecond);
                    long currentTime=calendar.getTimeInMillis();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTime, pendingIntent);
                    }
                    ContentValues contentValues1=new ContentValues();
                    contentValues1.put(Contract.Info.IS_ALARMED,true);
                    String[] whereArguments={id+""};
                    database.update(Contract.Info.TABLE_NAME,contentValues1,"id = ?",whereArguments);

                }
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Info info=items.get(i);
        int id=(int)info.getId();

        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtra(ID_KEY,id);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final int position=i;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Conformation");
        builder.setMessage("Do you really want to delete "+ items.get(i).getTitle());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                InfoOpenHelper openHelper=InfoOpenHelper.getInstance(MainActivity.this);
                SQLiteDatabase database=openHelper.getWritableDatabase();
                String[] selectionArgs={items.get(position).getId()+""};
                database.delete(Contract.Info.TABLE_NAME,"id = ?",selectionArgs);
                items.remove(position);
                dialogAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MyActivity","Log from onStart() in MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyActivity","Log from onResume() in MainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MyActivity","Log from onPause() in MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MyActivity","Log from onStop() in MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MyActivity","Log from onDestroy() in MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MyActivity","Log from onRestart() in MainActivity");
    }
}
