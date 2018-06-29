package in.co.softwaresolution.list;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class InfoAddActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_MENU=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add);


    }


    public void updateActivity(View view)
    {
        EditText titleedittext=findViewById(R.id.infoaddactivityTitleID);
        EditText descriptionedittext=findViewById(R.id.infoaddactivityDescriptionID);

        String title=titleedittext.getText().toString();
        String description=descriptionedittext.getText().toString();

        Intent intent=new Intent();
        intent.putExtra(MainActivity.TITLE_KEY,title);
        intent.putExtra(MainActivity.DESCRIPTION_KEY,description);
        setResult(2,intent);
        finish();

    }

}
