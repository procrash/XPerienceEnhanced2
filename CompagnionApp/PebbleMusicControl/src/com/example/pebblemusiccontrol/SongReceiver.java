package com.example.pebblemusiccontrol;

import java.util.Iterator;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.content.ContextWrapper;

/*
import com.smp.funwithmusic.R;
import com.smp.funwithmusic.activities.FlowActivity;
import com.smp.funwithmusic.dataobjects.Song;
*/
//import static com.smp.funwithmusic.global.Constants.*;
//import static com.smp.funwithmusic.global.UtilityMethods.*;

public class SongReceiver extends BroadcastReceiver
{

	private static final Object ACTION_ID = null;
	private boolean fromId;
	private String mAlbum;
	private String mArtist;
	private String mImageUrl;
	private Context context;
	private Intent intent;
	private String mTitle;
	private PendingResult result;

	private class SongReceiverAsyncTask extends AsyncTask<Void, Void, Void>
	{
		protected Void doInBackground(Void... blah)
		{
			/*
			setSongInfo(context, intent);

			Song song = new Song(mTitle, mArtist, mAlbum);
			song.setAlbumUrl(mImageUrl);

			if (song.validate())
			{
				song.removeFeaturing();
				writeNewSong(context, song);

				Intent send = new Intent(context, FlowActivity.class);
				send.setAction(ACTION_ADD_SONG);
				send.putExtra(EXTRA_FROM_ID, fromId);

				LocalBroadcastManager.getInstance(context).sendBroadcast(send);
			}
			// Log.i("SONG", mArtist + " " + mTitle + " " + mAlbum);

			result.finish();
			 */
			return null;
		}
	}
	/*
	private void writeNewSong(Context context, Song song)
	{

		ArrayList<Song> songs = getSongList(context);
		if (song != null &&
				(songs.size() == 0 || !songs.get(songs.size() - 1).equals(song)))
		{
			songs.add(song);
			writeObjectToFile(context, SONG_FILE_NAME, songs);
		}
	}
	*/

    public void sendMusicUpdateToPebble(Context context, String artist, String album, String track ) {
        final Intent i = new Intent("com.getpebble.action.NOW_PLAYING");
        i.putExtra("artist", artist);
        i.putExtra("album", album);
        i.putExtra("track", track);
        context.sendBroadcast(i);
    }
    

	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		fromId = false;
		this.context = context;
		this.intent = intent;
		
		
		Log.d("TEST", intent.getAction());
		
		try{
		
		String[] actions = {
								"com.htc.music.playstatechanged",
								"com.htc.music.metachanged",
								"com.android.music.playstatechanged",
								"com.android.music.metachanged",
								"com.rdio.android.metachanged",
								"com.rdio.android.playstatechanged",
								"com.jrtstudio.music.metachanged",
								"fm.last.android.metachanged",
								"com.real.RealPlayer.playstatechanged",
								"com.real.RealPlayer.metachanged",
								"com.tbig.playerprotrial.playstatechanged",
								"com.tbig.playerprotrial.metachanged",
								"com.tbig.playerpro.playstatechanged",
								"com.tbig.playerpro.metachanged",
								"com.spotify.mobile.android.playbackstatechanged",
								"com.spotify.mobile.android.metadatachanged",
								"com.spotify.mobile.android.queuechanged",
								"com.adam.aslfms.notify.playstatechanged",
								"com.adam.aslfms.notify.metachanged",
								"org.iii.romulus.meridian.metachanged",
								"org.iii.romulus.meridian.playstatechanged",
								"com.mixzing.music.metachanged",
								"com.mixzing.music.playstatechanged"
							};
		for(String action : actions) {
			if (intent.getAction().equals(action)) {
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");
				
				sendMusicUpdateToPebble(context, mArtist, mTitle, mAlbum);
				Log.d("TEST", "Artist:"+ mArtist+ " Title:"+mTitle+" Album:"+mAlbum);
				break;
			}
		}

		
		if (intent.getAction().equals("net.jjc1138.android.scrobbler.action.MUSIC_STATUS"))
		{
			boolean bool5 = intent.getBooleanExtra("playing", false);
			if (intent.hasExtra("id"))
			{
				int k = intent.getIntExtra("id", 1);
				Cursor localCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "_id=" + k, null, null);
				localCursor.moveToFirst();
				mArtist = localCursor.getString(localCursor.getColumnIndexOrThrow("artist"));
				mTitle = localCursor.getString(localCursor.getColumnIndexOrThrow("title"));
				mAlbum = localCursor.getString(localCursor.getColumnIndexOrThrow("album"));
				
				sendMusicUpdateToPebble(context, mArtist, mTitle, mAlbum);

				Log.d("TEST", "Artist:"+ mArtist+ " Title:"+mTitle+" Album:"+mAlbum);

			}

		}
		else if (intent.getAction().equals("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED"))
		{
			mArtist = intent.getStringExtra("ARTIST_NAME");
			mTitle = intent.getStringExtra("TRACK_NAME");
			mAlbum = intent.getStringExtra("ALBUM_NAME");
			
			sendMusicUpdateToPebble(context, mArtist, mTitle, mAlbum);

			Log.d("TEST", "Artist:"+ mArtist+ " Title:"+mTitle+" Album:"+mAlbum);
		}

	}
	catch (Exception e)
	{
		e.printStackTrace();
	}

/*
		String logKey = context.getResources().getString(R.string.pref_key_log_songs);
		SharedPreferences pref = getPref(context);
		if (pref.getBoolean(logKey, true))
		{
			result = goAsync();
			new SongReceiverAsyncTask().execute();
		}
		*/
	}
/*
	private void setSongInfo(Context context, Intent intent)
	{

		Log.d("debugger", "XXX:"+intent.getAction());
		try
		{
			if (intent.getAction().equals(ACTION_ID))
			{
				fromId = true;
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("title");
				mAlbum = intent.getStringExtra("album");
				mImageUrl = intent.getStringExtra("imageUrl");
			}
			else if ((intent.getAction().equals("com.htc.music.playstatechanged")) || (intent.getAction().equals("com.htc.music.metachanged")))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");

			}

			else if (intent.getAction().equals("com.htc.music.playbackcomplete"))
			{

			}
			else if ((intent.getAction().equals("com.android.music.playstatechanged")) || (intent.getAction().equals("com.android.music.metachanged")))
			{
				boolean bool2 = intent.getBooleanExtra("playing", false);
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");

			}
			else if (intent.getAction().equals("com.android.music.playbackcomplete"))
			{

			}

			else if (intent.getAction().equals("com.rdio.android.metachanged") || (intent.getAction().equals("com.rdio.android.playstatechanged")))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");
			}
			else if (intent.getAction().equals("com.jrtstudio.music.metachanged"))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");
			}
			else if (intent.getAction().equals("fm.last.android.metachanged"))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");

			}
			else if ((intent.getAction().equals("fm.last.android.playbackpaused")) || (intent.getAction().equals("fm.last.android.playbackcomplete")))
			{

			}
			else if ((intent.getAction().equals("com.real.RealPlayer.playstatechanged")) || (intent.getAction().equals("com.real.RealPlayer.metachanged")))
			{
				
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");

			}
			else if (intent.getAction().equals("com.real.RealPlayer.playbackcomplete"))
			{

			}
			else if ((intent.getAction().equals("com.tbig.playerprotrial.playstatechanged")) || (intent.getAction().equals("com.tbig.playerprotrial.metachanged"))
					|| (intent.getAction().equals("com.tbig.playerpro.playstatechanged")) || (intent.getAction().equals("com.tbig.playerpro.metachanged")))
			{
				
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");

			}
			else if ((intent.getAction().equals("com.tbig.playerprotrial.playbackcomplete")) || (intent.getAction().equals("com.tbig.playerpro.playbackcomplete")))
			{

			}

			else if (intent.getAction().equals(
					"com.spotify.mobile.android.playbackstatechanged"))
			{
					mArtist = intent.getStringExtra("artist");
					mTitle = intent.getStringExtra("track");
					mAlbum =
							intent.getStringExtra("album");
			}
			else if (intent.getAction
					().equals("com.spotify.mobile.android.metadatachanged"))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum =
						intent.getStringExtra("album");

			}
			else if (intent.getAction
					().equals("com.spotify.mobile.android.queuechanged"))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum =
						intent.getStringExtra("album");

			}

			else if ((intent.getAction().equals("com.adam.aslfms.notify.playstatechanged")) || (intent.getAction().equals("com.adam.aslfms.notify.metachanged")))
			{
				mArtist = intent.getStringExtra("artist");
				mTitle = intent.getStringExtra("track");
				mAlbum = intent.getStringExtra("album");
			}
			else if (intent.getAction().equals("com.adam.aslfms.notify.playbackcomplete"))
			{

			}
			else if (intent.getAction().equals("net.jjc1138.android.scrobbler.action.MUSIC_STATUS"))
			{
				boolean bool5 = intent.getBooleanExtra("playing", false);
				if (intent.hasExtra("id"))
				{
					int k = intent.getIntExtra("id", 1);
					Cursor localCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "_id=" + k, null, null);
					localCursor.moveToFirst();
					mArtist = localCursor.getString(localCursor.getColumnIndexOrThrow("artist"));
					mTitle = localCursor.getString(localCursor.getColumnIndexOrThrow("title"));
					mAlbum = localCursor.getString(localCursor.getColumnIndexOrThrow("album"));
				}

			}
			else if (intent.getAction().equals("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED"))
			{
				mArtist = intent.getStringExtra("ARTIST_NAME");
				mTitle = intent.getStringExtra("TRACK_NAME");
				mAlbum = intent.getStringExtra("ALBUM_NAME");

			}
			else if ((intent.getAction().equals("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED")) || (intent.getAction().equals("com.sonyericsson.music.TRACK_COMPLETED")))
			{

			}

			else if ((intent.getAction().equals("com.real.RealPlayer.playstatechanged")) || (intent.getAction().equals("com.real.RealPlayer.metachanged")))
			{
				this.mArtist = intent.getStringExtra("artist");
				this.mTitle = intent.getStringExtra("track");
				this.mAlbum = intent.getStringExtra("album");
			}
			else if ((intent.getAction().equals("org.iii.romulus.meridian.metachanged")) || (intent.getAction().equals("org.iii.romulus.meridian.playstatechanged")))
			{
				this.mArtist = intent.getStringExtra("artist");
				this.mTitle = intent.getStringExtra("track");
				this.mAlbum = intent.getStringExtra("album");
			}
			
			else if ((intent.getAction().equals("com.mixzing.music.metachanged")) || (intent.getAction().equals("com.mixzing.music.playstatechanged")))
			{
				this.mArtist = intent.getStringExtra("artist");
				this.mTitle = intent.getStringExtra("track");
				this.mAlbum = intent.getStringExtra("album");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//dumpIntent(intent);

	}
*/
	public static void dumpIntent(Intent i)
	{

		Bundle bundle = i.getExtras();
		if (bundle != null)
		{
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();
			//Log.d("INTENT", "Dumping Intent start");
			while (it.hasNext())
			{
				String key = it.next();
				//Log.d("INTENT", "[" + key + "=" + bundle.get(key) + "]");
			}
			//Log.d("INTENT", "Dumping Intent end");
		}
	}
}
