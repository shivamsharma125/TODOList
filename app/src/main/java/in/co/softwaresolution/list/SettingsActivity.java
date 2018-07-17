package in.co.softwaresolution.list;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void setSMSPermission(View view) {

        CheckBox checkBox=(CheckBox)view;
        boolean isChecked=checkBox.isChecked();
        if(isChecked)
        {
           setPermissions();
        }

    }
    public void setPermissions()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            String[] permissions={Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS};
            ActivityCompat.requestPermissions(this,permissions,MainActivity.REQUEST_CODE_SMS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MainActivity.REQUEST_CODE_SMS) {
            if ((grantResults[0] != PackageManager.PERMISSION_GRANTED)|(grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Grant SMS Receive And Read Permission", Toast.LENGTH_LONG).show();
            }
        }

    }
}
