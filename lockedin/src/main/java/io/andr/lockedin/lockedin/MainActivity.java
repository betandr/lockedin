package io.andr.lockedin.lockedin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectToPusher();
    }

    private void connectToPusher() {
        Pusher pusher = new Pusher("046580237333bd0d50c7");

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("DEBUG", "State changed to " + change.getCurrentState() + " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("DEBUG", "Problem connecting: " + message + " e: " + e.getMessage());
            }
        }, ConnectionState.ALL);

        Log.i("DEBUG", "Subscribing to 'locked_in_channel'");
        Channel channel = pusher.subscribe("locked_in_channel");

        Log.i("DEBUG", "Binding to 'vote' event");
        channel.bind("vote", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.i("DEBUG", "Received event with data: " + data);
                Intent intent = new Intent(getApplicationContext(), VoteActivity.class);
                intent.putExtra("json", data);
                startActivity(intent);
            }
        });

        Log.i("DEBUG", "Binding to 'image' event");
        channel.bind("image", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.i("DEBUG", "Received event with data: " + data);
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra("json", data);
                startActivity(intent);
            }
        });

        Log.i("DEBUG", "Binding to 'video' event");
        channel.bind("video", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.i("DEBUG", "Received event with data: " + data);
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("json", data);
                startActivity(intent);
            }
        });

        Log.i("DEBUG", "Binding to 'default'");
        channel.bind("default", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                Log.i("DEBUG", "Received event with data: " + data);

                try {

                    JSONObject message = new JSONObject(data);
                    String reset = message.getString("reset");

                    if ("true".equals(reset)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException je) {
                    Log.i("DEBUG", "Exception: " + je.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
