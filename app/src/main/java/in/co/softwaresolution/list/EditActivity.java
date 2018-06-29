package in.co.softwaresolution.list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    public static final int RESULT_CODE_EDIT_MENU=1;
    EditText title;
    EditText description;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title=findViewById(R.id.editactivityTitleID);
        description=findViewById(R.id.editactivityDescriptionID);

        Intent intent=getIntent();
        bundle=intent.getExtras();

        title.setText(bundle.getString(MainActivity.TITLE_KEY));
        description.setText(bundle.getString(MainActivity.DESCRIPTION_KEY));

    }

    public void EditInformation(View view) {

        String editedTitle=title.getText().toString();
        String editedDescription=description.getText().toString();

        if((!editedTitle.isEmpty()) && (!editedDescription.isEmpty()))
        {
            bundle.putString(MainActivity.TITLE_KEY,editedTitle);
            bundle.putString(MainActivity.DESCRIPTION_KEY,editedDescription);
            Intent intent=new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_CODE_EDIT_MENU,intent);
            finish();
        }

    }
}
