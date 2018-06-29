package in.co.softwaresolution.list;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String TITLE_KEY="title_key";
    public static final String DESCRIPTION_KEY="description_key";
    public static final int REQUEST_CODE_ADD_MENU=1;
    public static final int REQUEST_CODE_EDIT_MENU=2;
    public static final String STRING_KEY="string_key";
    public static final String POSITION="position";
    public static final String PREFERENCE="preference";
    String string="";
    ArrayList<Info> items=new ArrayList<>();
    DialogAdapter dialogAdapter;
    ListView listView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InfoOpenHelper openHelper=new InfoOpenHelper(this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.Info.TABLE_NAME,null,null,null,null,null,null);
        Log.d("MyActivityBeforeCursor","Hello");
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Info.CLOUMN_ID));
            String title=cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_TITLE));
            String description=cursor.getString(cursor.getColumnIndex(Contract.Info.COLUMN_DESCRIPTION));
            Info info=new Info(title,description);
            info.setId(id);
            Log.d("MyActivityAfterCursor","Hello");
            items.add(info);
        }
        listView=findViewById(R.id.listviewID);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);



        dialogAdapter=new DialogAdapter(MainActivity.this,items);
        listView.setAdapter(dialogAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id == R.id.addmenuID)
        {
            Intent intent=new Intent(this,InfoAddActivity.class);
            startActivityForResult(intent,REQUEST_CODE_ADD_MENU);
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
                String title=data.getStringExtra(TITLE_KEY);
                String description=data.getStringExtra(DESCRIPTION_KEY);
                if((!title.isEmpty()) && (!description.isEmpty()))
                {
                    Info info=new Info(title,description);
                    items.add(info);
                    InfoOpenHelper openHelper=new InfoOpenHelper(this);
                    SQLiteDatabase database=openHelper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    Log.d("MyActivityTitle",title);
                    Log.d("MyActivityDescription",description);
                    contentValues.put(Contract.Info.COLUMN_TITLE,title);
                    contentValues.put(Contract.Info.COLUMN_DESCRIPTION,description);
                    long id = database.insert(Contract.Info.TABLE_NAME,null,contentValues);
                    if(id > -1)
                    {
                        info.setId(id);
                    }
                    dialogAdapter.notifyDataSetChanged();

                }
            }
        }

        if(requestCode == REQUEST_CODE_EDIT_MENU)
        {
            if(resultCode == DetailsActivity.DETAILS_ACTIVITY_RESULT_CODE)
            {
                Bundle bundle=data.getExtras();
                Info info=items.get(bundle.getInt(POSITION));
                info.setTitle(bundle.getString(TITLE_KEY));
                info.setDescription(bundle.getString(DESCRIPTION_KEY));
                dialogAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Info info=items.get(i);
        Bundle bundle=new Bundle();
        bundle.putString(TITLE_KEY,info.getTitle());
        bundle.putString(DESCRIPTION_KEY,info.getDescription());
        bundle.putInt(POSITION,i);
        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE_EDIT_MENU);
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

                InfoOpenHelper openHelper=new InfoOpenHelper(MainActivity.this);
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
}
