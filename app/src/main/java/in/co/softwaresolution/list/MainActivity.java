package in.co.softwaresolution.list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        listView=findViewById(R.id.listviewID);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        sharedPreferences=getSharedPreferences(PREFERENCE,MODE_PRIVATE);
        String str=sharedPreferences.getString(STRING_KEY,null);
        if(str!=null)
        {
            setString(str);
        }

        editor=sharedPreferences.edit();

        dialogAdapter=new DialogAdapter(MainActivity.this,items);
        listView.setAdapter(dialogAdapter);

    }

    public void setString(String str)
    {
        String title;
        String description;
        for(int i=0;i<str.length();)
        {

                int n=str.indexOf(":",i);
                title=str.substring(i,n);

                i=n+1;

                n=str.indexOf("/",i);
                description=str.substring(i,n);
                i=n+1;
                Info info=new Info(title,description);
                items.add(info);
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
                    string=string.concat(title).concat(":").concat(description).concat("/");
                    editor.putString(STRING_KEY,string);
                    editor.commit();
                    dialogAdapter=new DialogAdapter(MainActivity.this,items);
                   listView.setAdapter(dialogAdapter);

                }
            }
        }

        if(requestCode == REQUEST_CODE_EDIT_MENU)
        {
            if(resultCode == DetailsActivity.DETAILS_ACTIVITY_RESULT_CODE)
            {
                Bundle bundle=data.getExtras();
                Info info=items.get(bundle.getInt(POSITION));
                String title=info.getTitle();
                String description=info.getDescription();
                String oldstr="";
                oldstr=oldstr.concat(title).concat(":").concat(description).concat("/");
                info.setTitle(bundle.getString(TITLE_KEY));
                info.setDescription(bundle.getString(DESCRIPTION_KEY));
                String str="";
                str=str.concat(info.getTitle()).concat(":").concat(info.getDescription()).concat("/");
                string=string.replace(oldstr,str);
                editor.putString(STRING_KEY,string);
                editor.commit();
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

                String str="";
                Info info=items.get(position);
                str=str.concat(info.getTitle()).concat(":").concat(info.getDescription()).concat("/");
                string=string.replace(str,"");
                editor.putString(STRING_KEY,string);
                editor.commit();
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
