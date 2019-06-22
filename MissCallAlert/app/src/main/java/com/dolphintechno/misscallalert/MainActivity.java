package com.dolphintechno.misscallalert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dolphintechno.misscallalert.activity.SetMsg;
import com.dolphintechno.misscallalert.data.MySharedPreferences;
import com.dolphintechno.misscallalert.helper.SharedPrefKeys;
import com.dolphintechno.misscallalert.sevices.BackgroundService;

public class MainActivity extends AppCompatActivity {

    MySharedPreferences dataProccessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.SEND_SMS}, 0);

            return;
        }



        dataProccessor = new MySharedPreferences(this);

        boolean is_previously_set_msg = dataProccessor.getBool(SharedPrefKeys.is_set_set_msg);

        if (is_previously_set_msg){

            Intent serviceIntent = new Intent(this, BackgroundService.class);
            startService(serviceIntent);
//            Toast.makeText(getApplicationContext(), "it is previously loged in", Toast.LENGTH_LONG).show();
        }else {

            startActivity(new Intent(this, SetMsg.class));
            finish();
//            Toast.makeText(getApplicationContext(), "is not previously loged in", Toast.LENGTH_LONG).show();
        }
    }

}
