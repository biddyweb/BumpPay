package com.bump.apitest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bump.api.BumpAPIIntents;
import com.bump.api.IBumpAPI;

public class BumpTest extends Activity
{
    private IBumpAPI api;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            //Toast("BumpTest", "onServiceConnected");
            //Toast.makeText(getApplicationContext(), "onServiceConnected", Toast.LENGTH_SHORT).show();
        	
            api = IBumpAPI.Stub.asInterface(binder);
            try {
                api.configure("fdf5fc568ce94798b5155e3da617f8b2", "CY");
            } catch (RemoteException e) {
                //Log.w("BumpTest", e);
            	//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            //Toast("Bump Test", "Service connected");
           // Toast.makeText(getApplicationContext(), "Service connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            //Toast("Bump Test", "Service disconnected");
            //Toast.makeText(getApplicationContext(), "Service disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            try {
                if (action.equals(BumpAPIIntents.DATA_RECEIVED)) {
                    //Toast("Bump Test", "Received data from: " + api.userIDForChannelID(intent.getLongExtra("channelID", 0))); 
                    //Toast("Bump Test", "Data: " + new String(intent.getByteArrayExtra("data")));
                    Toast.makeText(getApplicationContext(), "Received data from: " + api.userIDForChannelID(intent.getLongExtra("channelID", 0)), Toast.LENGTH_SHORT).show();
                    
                    TextView tv = (TextView) findViewById(R.id.textView1);
                    tv.setText(new String(intent.getByteArrayExtra("data")));
                    //Toast.makeText(getApplicationContext(), "Data: " + ), Toast.LENGTH_SHORT).show();
                    
                } else if (action.equals(BumpAPIIntents.MATCHED)) {
                    long channelID = intent.getLongExtra("proposedChannelID", 0); 
                    //Toast("Bump Test", "Matched with: " + api.userIDForChannelID(channelID));
                    Toast.makeText(getApplicationContext(), "Matched with: " + api.userIDForChannelID(channelID), Toast.LENGTH_SHORT).show();
                    api.confirm(channelID, true);
                    //Toast("Bump Test", "Confirm sent");
                    //Toast.makeText(getApplicationContext(), "Confirm sent", Toast.LENGTH_SHORT).show();
                } else if (action.equals(BumpAPIIntents.CHANNEL_CONFIRMED)) {
                    final long channelID = intent.getLongExtra("channelID", 0);
                    //Toast("Bump Test", "Channel confirmed with " + api.userIDForChannelID(channelID));
                    //Toast.makeText(getApplicationContext(), "Channel confirmed with " + api.userIDForChannelID(channelID), Toast.LENGTH_SHORT).show();
                    
                    
                    
                    
                    
                    
                     Button button = (Button) findViewById(R.id.button2);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                        	
							
                        
							            	EditText txt = (EditText) findViewById(R.id.editText1);
											String message = txt.getText().toString();
							            	
												try {
													api.send(channelID, message.getBytes());
												} catch (RemoteException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
                   
                        }
                    });
                } else if (action.equals(BumpAPIIntents.NOT_MATCHED)) {
                    //Toast("Bump Test", "Not matched.");
                    Toast.makeText(getApplicationContext(), "Not matched.", Toast.LENGTH_SHORT).show();
                } else if (action.equals(BumpAPIIntents.CONNECTED)) {
                    //Toast("Bump Test", "Connected to Bump...");
                    Toast.makeText(getApplicationContext(), "Connected to Bump...", Toast.LENGTH_SHORT).show();
                    api.enableBumping();
                }
            } catch (RemoteException e) {}
        }


    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context context = this;
       
        
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 bindService(new Intent(IBumpAPI.class.getName()), connection, Context.BIND_AUTO_CREATE);
            	 
            	 IntentFilter filter = new IntentFilter();
                 filter.addAction(BumpAPIIntents.CHANNEL_CONFIRMED);
                 filter.addAction(BumpAPIIntents.DATA_RECEIVED);
                 filter.addAction(BumpAPIIntents.NOT_MATCHED);
                 filter.addAction(BumpAPIIntents.MATCHED);
                 filter.addAction(BumpAPIIntents.CONNECTED);
                 registerReceiver(receiver, filter);
            }
        });
        //Toast.makeText(getApplicationContext(), "boot", Toast.LENGTH_SHORT).show();
        //Toast("BumpTest", "boot");

        
    }

     public void onStart() {
    	// Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
        //Toast("BumpTest", "onStart");
        super.onStart();
     }
     
     public void onRestart() {
    	 //Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_SHORT).show();
       // Toast("BumpTest", "onRestart");
        super.onRestart();
     }

     public void onResume() {
    	 //Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
        //Toast("BumpTest", "onResume");
        super.onResume();
     }

     public void onPause() {
    	 //Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
        //Toast("BumpTest", "onPause");
        super.onPause();
     }

     public void onStop() {
    	 //Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
        //Toast("BumpTest", "onStop");
        super.onStop();
     }

     public void onDestroy() {
    	 //Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();
        //Toast("BumpTest", "onDestroy");
        unbindService(connection);
        unregisterReceiver(receiver);
        super.onDestroy();
     }

}
