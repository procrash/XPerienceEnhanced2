package com.example.pebblemusiccontrol;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

public class MainActivity extends Activity {
	public static final String ACTION_ID = "com.smp.funwithmusic.ID_ACTION";
	private boolean fromId;
	private String mAlbum;
	private String mArtist;
	private String mImageUrl;
//	private Context context;
	private Intent intent;
	private String mTitle;
	private PendingResult result;

	private UUID PEBBLE_APP_UUID = UUID.fromString("d644f77c-2435-49d6-85f0-b7d6df2a2691");

	private PebbleKit.PebbleDataReceiver dataReceiver;
	private final static int VOLUME_KEY = 0x00;
	private Handler mHandler;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        
        dataReceiver = new PebbleKit.PebbleDataReceiver(PEBBLE_APP_UUID) {
			@Override
			public void receiveData(final Context context, final int transactionId,
					PebbleDictionary data) {
				
//	               final int cmd = data.getUnsignedInteger(VOLUME_KEY).intValue();
				AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
				
				int newVolume = data.getInteger(0).intValue();
				int maxVal = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				
				
				audio.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume*maxVal/100, 0);
	               Log.d("TAG", "Volume:" + newVolume+" MaxVol:"+maxVal);
	              mHandler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        // All data received from the Pebble must be ACK'd, otherwise you'll hit time-outs in the
	                        // watch-app which will cause the watch to feel "laggy" during periods of frequent
	                        // communication.
	                        PebbleKit.sendAckToPebble(context, transactionId);

	                    }
	                });
				
			}
        };
        PebbleKit.registerReceivedDataHandler(this, dataReceiver);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void sendMusicUpdateToPebble() {
        final Intent i = new Intent("com.getpebble.action.NOW_PLAYING");
        i.putExtra("artist", "Carly Rae Jepsen");
        i.putExtra("album", "Kiss");
        i.putExtra("track", "Call Me Maybe");

        sendBroadcast(i);
    }
    
    private void sendNotification(){
        final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");
        
        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("title", "Test Message");
        data.put("body", "Whoever said nothing was impossible never tried to slam a revolving door.");
        final JSONObject jsonData = new JSONObject(data);
        final String notificationData = new JSONArray().put(jsonData).toString();
 
        i.putExtra("messageType", "PEBBLE_ALERT");
        i.putExtra("sender", "MyAndroidApp");
        i.putExtra("notificationData", notificationData);
 
        Log.d("TAG", "About to send a modal alert to Pebble: " + notificationData);
        sendBroadcast(i);
    }
    public void selfDestruct(View view) {
		Log.d("debugger", "AAA");

        // Kabloey
    	/*
    	Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
    	startActivity(intent);
    	
    	Toast toast = Toast.makeText(this, "No viewer on this device",
    	Toast.LENGTH_SHORT);
    	toast.show();*/
    	// right click on a track in Spotify to get the URI, or use the Web API.
    	/*
    	String uri = "spotify:track:600102303b3034c0c9230ff04503404532813ca7"; 
    							
    	Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri) );
    	startActivity(launcher);
    	*/
    	boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
    	Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
    	
		// UUID GOLD_UUID =com.getpebble.android.kit.Constants.SPORTS_UUID;
		
    	PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID );
    	sendMusicUpdateToPebble();
    	
    	
    	sendNotification();



		
    	/*
    	String artistName="ColdPlay";
		String trackName="Life in Technicolor";
		try {
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setAction(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
            intent.setComponent(new ComponentName("com.spotify.mobile.android.ui", "com.spotify.mobile.android.ui.Launcher"));
            intent.putExtra(SearchManager.QUERY, artistName + " " + trackName );
            this.startActivity(intent);
        } catch ( ActivityNotFoundException e ) {
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setAction(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
            intent.setComponent(new ComponentName("com.spotify.mobile.android.ui", "com.spotify.mobile.android.ui.activity.MainActivity"));
            intent.putExtra(SearchManager.QUERY, artistName + " " + trackName );
            this.startActivity(intent);
        }
    	*/
    	
    }
    


    
    
}
