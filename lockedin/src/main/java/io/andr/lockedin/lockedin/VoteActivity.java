package io.andr.lockedin.lockedin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class VoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Bundle extras = getIntent().getExtras();
        String json = "";

        if (extras != null) {
            json = extras.getString("json");
        }

        try {

            JSONObject vote = new JSONObject(json);

            String title = vote.getString("title");
            Log.i("DEBUG", "title: " + title);
            TextView titleView = (TextView) findViewById(R.id.vote_title);
            titleView.setText(title);

            String description = vote.getString("description");
            Log.i("DEBUG", "description: " + description);
            TextView descriptionView = (TextView) findViewById(R.id.vote_description);
            descriptionView.setText(description);

            JSONArray options = vote.getJSONArray("options");
            Log.i("DEBUG", "options: " + options.toString());

            JSONObject option1 = options.getJSONObject(0);
            final Button button1 = (Button) findViewById(R.id.button_one);
            button1.setText(option1.getString("title"));


            new DownloadImageTask((ImageView) findViewById(R.id.image_one)).execute(option1.getString("image_url"));

            JSONObject option2 = options.getJSONObject(1);
            final Button button2 = (Button) findViewById(R.id.button_two);
            button2.setText(option2.getString("title"));

            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Thanks for your vote!", Toast.LENGTH_LONG).show();
                    button1.setEnabled(false);
                    button1.setBackgroundResource(R.drawable.disabled_vote_button);
                    button2.setEnabled(false);
                    button2.setBackgroundResource(R.drawable.disabled_vote_button);
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Thanks for your vote!", Toast.LENGTH_LONG).show();
                    button1.setEnabled(false);
                    button1.setBackgroundResource(R.drawable.disabled_vote_button);
                    button2.setEnabled(false);
                    button2.setBackgroundResource(R.drawable.disabled_vote_button);
                }
            });

            new DownloadImageTask((ImageView) findViewById(R.id.image_two)).execute(option2.getString("image_url"));


        } catch (JSONException je) {
            Log.i("DEBUG", "Exception: " + je.getMessage());
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vote, menu);
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
