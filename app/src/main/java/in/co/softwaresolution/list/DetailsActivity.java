package in.co.softwaresolution.list;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    public static final int DETAILS_ACTIVITY_RESULT_CODE=3;
    Bundle bundle;
    TextView title;
    TextView description;
    TextView date;
    TextView time;
    int id;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Log.d("DetailsActivity","from onCreate method");

            Intent intent = getIntent();
            id=intent.getIntExtra(MainActivity.ID_KEY,0);

            InfoOpenHelper openHelper=InfoOpenHelper.getInstance(this);
            database=openHelper.getReadableDatabase();

        String[] selectionArguments={id+""};
        cursor=database.query(Contract.Info.TABLE_NAME,null,"id = ?",selectionArguments,null,null,null);

        title = findViewById(R.id.detailsactivitytitleID);
        description = findViewById(R.id.detailsactivitydescriptionID);
        date=findViewById(R.id.detailsactivitydateID);
        time=findViewById(R.id.detailsactivitytimeID);

        if(cursor.moveToNext()) {
            title.setText(cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_TITLE)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_DESCRIPTION)));
            date.setText(cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_DAY)) + "/" + cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MONTH)) + "/" + cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_YEAR)));
            time.setText(cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_HOUR)) + ":" + cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_MIN)) + ":" + cursor.getInt(cursor.getColumnIndex(Contract.Info.COLUMN_SECOND)));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id == R.id.detailsmenuID)
        {
            Intent intent=new Intent(this,EditActivity.class);
            intent.putExtra(MainActivity.ID_KEY,this.id);
            startActivity(intent);
            finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
