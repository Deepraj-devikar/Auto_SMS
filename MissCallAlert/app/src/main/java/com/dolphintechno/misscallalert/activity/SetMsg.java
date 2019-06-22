package com.dolphintechno.misscallalert.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dolphintechno.misscallalert.R;
import com.dolphintechno.misscallalert.data.MySharedPreferences;
import com.dolphintechno.misscallalert.helper.SharedPrefKeys;
import com.dolphintechno.misscallalert.sevices.BackgroundService;

public class SetMsg extends AppCompatActivity implements View.OnClickListener {

    EditText et_msg;

    Button btn_set_msg;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_msg);

        et_msg = (EditText) findViewById(R.id.et_msg);

        btn_set_msg = (Button) findViewById(R.id.btn_set_msg);

        btn_set_msg.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btn_set_msg){
            message = et_msg.getText().toString();
            if (!TextUtils.isEmpty(message)){
                MySharedPreferences dataProccessor = new MySharedPreferences(getApplicationContext());
                dataProccessor.setStr(SharedPrefKeys.msg, message);
                dataProccessor.setBool(SharedPrefKeys.is_set_set_msg, true);
                Intent serviceIntent = new Intent(this, BackgroundService.class);
                startService(serviceIntent);
            }else {
                et_msg.setError("Write Message");
            }
        }
    }
}
