package in.co.softwaresolution.list;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    public static final int DETAILS_ACTIVITY_RESULT_CODE=3;
    Bundle bundle;
    TextView title;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        bundle = intent.getExtras();

        title = findViewById(R.id.detailsactivitytitleID);
        description = findViewById(R.id.detailsactivitydescriptionID);

        title.setText(bundle.getString(MainActivity.TITLE_KEY));
        description.setText(bundle.getString(MainActivity.DESCRIPTION_KEY));

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
            intent.putExtras(bundle);
            startActivityForResult(intent,MainActivity.REQUEST_CODE_EDIT_MENU);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MainActivity.REQUEST_CODE_EDIT_MENU)
        {
            if(resultCode == EditActivity.RESULT_CODE_EDIT_MENU)
            {
                bundle=data.getExtras();
                title.setText(bundle.getString(MainActivity.TITLE_KEY));
                description.setText(bundle.getString(MainActivity.DESCRIPTION_KEY));
                Intent intent=new Intent();
                intent.putExtras(bundle);
                setResult(DETAILS_ACTIVITY_RESULT_CODE,intent);
            }
        }

    }
}
