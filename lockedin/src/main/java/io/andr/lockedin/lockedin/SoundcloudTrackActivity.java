package io.andr.lockedin.lockedin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;


public class SoundcloudTrackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundcloud_track);

        Bundle extras = getIntent().getExtras();
        String json = "";

        if (extras != null) {
            json = extras.getString("json");
        }

        try {
            JSONObject vote = new JSONObject(json);
            String soundcloudId = vote.getString("soundcloud_id");

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("soundcloud://tracks:" + soundcloudId));
            startActivity(intent);

        } catch (JSONException je) {
            Log.i("DEBUG", "Exception: " + je.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.soundcloud_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
