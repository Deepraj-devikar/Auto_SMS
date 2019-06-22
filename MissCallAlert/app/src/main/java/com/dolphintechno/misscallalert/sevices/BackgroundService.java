package com.dolphintechno.misscallalert.sevices;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.dolphintechno.misscallalert.R;
import com.dolphintechno.misscallalert.data.DatabaseManager;
import com.dolphintechno.misscallalert.data.MySharedPreferences;
import com.dolphintechno.misscallalert.helper.SharedPrefKeys;
import com.dolphintechno.misscallalert.model.MissCallInfo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
//                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                String[] strFields = {android.provider.CallLog.Calls.CACHED_NAME, android.provider.CallLog.Calls.NUMBER,android.provider.CallLog.Calls.DATE, android.provider.CallLog.Calls.TYPE
                };
                String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

                DatabaseManager mDatabase = new DatabaseManager(context);
                Cursor cursor = mDatabase.getAllMissCallInfo();
                List<MissCallInfo> missCallInfoList = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        missCallInfoList.add(new MissCallInfo(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4)
                        ));
                    } while (cursor.moveToNext());
                }
                int listSize = missCallInfoList.size();
                int tenthLastIndex = 0;
                if (listSize > 15) {
                    tenthLastIndex = listSize - 10;
                }
                for (int k = 0; k < missCallInfoList.size(); k++){
                    Toast.makeText(context, "id : "+missCallInfoList.get(k).getId()+" Date : "+missCallInfoList.get(k).getDate_time(), Toast.LENGTH_LONG).show();
                }


                @SuppressLint("MissingPermission") Cursor mCallCursor = getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,strFields, null, null, strOrder);
                int i = 0;
                if (mCallCursor.moveToFirst()) {
                    do {
                        boolean missed = mCallCursor.getInt(mCallCursor.getColumnIndex(CallLog.Calls.TYPE)) == CallLog.Calls.MISSED_TYPE;
                        if (missed) {
                            String name = mCallCursor.getString(mCallCursor
                                    .getColumnIndex(CallLog.Calls.CACHED_NAME));
                            String number = mCallCursor.getString(mCallCursor
                                    .getColumnIndex(CallLog.Calls.NUMBER));
                            String ContactNumber = number;
                            String time2 = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.LONG)
                                    .format(mCallCursor.getLong(mCallCursor
                                            .getColumnIndex(CallLog.Calls.DATE)));
                            Log.d("PhoneLog",  "You have a missed call " + i++ + " from " + name + " on " + number
                                    + " at " + time2);

//                            String t = DateFormat.getDateTimeInstance(DateFormat.DATE_FIELD, DateFormat.LONG)
//                                    .format(mCallCursor.getLong(mCallCursor
//                                            .getColumnIndex(strDate + strTime)));

//                            if (time2.compareTo("7/5/19 1:32:53 pm IST")>0){
                                int j;
                                for (j = tenthLastIndex; j < listSize; j++){
                                    if (missCallInfoList.get(j).getDate_time().equals(time2) && missCallInfoList.get(j).getNumber().equals(number))
                                        break;
                                }
                                if (j == listSize){
                                    try{
                                        MySharedPreferences dataProccessor = new MySharedPreferences(context);
                                        String m = dataProccessor.getStr(SharedPrefKeys.msg);
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(ContactNumber, null, m, null, null);
                                        Toast.makeText(context, "method to send sms", Toast.LENGTH_LONG).show();
                                        if (mDatabase.addMissCallInfo(name, ContactNumber, time2, "message send")){
                                            Toast.makeText(getApplicationContext(), "MissCallInfo Added", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Could not add MissCallInfo", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (IllegalArgumentException e){
                                        Log.e("sens sms", "IllegalArgumentException");
                                    }
                                }
//                            }
                        }
                    } while (mCallCursor.moveToNext());
                }
                handler.postDelayed(runnable, 10000);
            }
        };
        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
//        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onStart(Intent intent, int startId) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        strDate = intent.getStringExtra("StrDate");
//        strTime = intent.getStringExtra("StrTime");
        return START_STICKY;
    }
}
