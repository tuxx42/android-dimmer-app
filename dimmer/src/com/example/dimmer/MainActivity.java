package com.example.dimmer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

class RetrieveFeedTask extends AsyncTask<String, String, String> {

    protected String doInBackground(String... urls) {
    	 try{
	           HttpClient httpclient = new DefaultHttpClient();

	           HttpGet request = new HttpGet();
	           URI website = new URI(urls[0]);
	           request.setURI(website);
	           HttpResponse response = httpclient.execute(request);
	           BufferedReader in = new BufferedReader(new InputStreamReader(
	                   response.getEntity().getContent()));

	           // NEW CODE
	           String line = in.readLine();
	           //textv.append(" First line: " + line);
	           // END OF NEW CODE
	      	 return line;
	           //textv.append(" Connected ");
	       }catch(Exception e){
	           Log.e("dimmer", "Error in http connection "+e.toString());
	      	 return null;
	       }
    }
}

public class MainActivity extends Activity {
	private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
   
        seekBar = (SeekBar) findViewById(R.id.SeekBar0);
        if (seekBar != null)
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//          int progress = 0;

          @Override
          public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
//            progress = progresValue;
            Log.d("dimmer","bar1");
            if (fromUser)
            	new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=0&state=on");
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
        	  Log.d("dimmer","bar");
          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
        	  new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=0&state=on");
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

    public void increaseBrightness(View view) {
    	int i = -1;
    	Switch sw = null;
    	SeekBar sb = null;
    	
    	switch(view.getId()) {
    	case R.id.ButtonBright0:
    		sw = (Switch) findViewById(R.id.ButtonToggle0);
    		sb = (SeekBar) findViewById(R.id.SeekBar0);
    		i = 0;
    		break;
    	case R.id.ButtonBright1:
    		sw = (Switch) findViewById(R.id.ButtonToggle1);
    		i = 1;
    		break;
    	case R.id.ButtonBright2:
    		sw = (Switch) findViewById(R.id.ButtonToggle2);
    		i = 2;
    		break;
    	case R.id.ButtonBright3:
    		sw = (Switch) findViewById(R.id.ButtonToggle3);
    		i = 3;
    		break;
    	default:
			Log.d("increaseBrightness", "Other");
			return;
    	}

    	Log.d("dimmer", String.valueOf(i));
    	new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=" + String.valueOf(i) + "&bright=1");
    	if (sw != null)
    		sw.setChecked(true);
    	if (sb != null)
    		sb.setProgress(sb.getProgress()+5);
}
    
    public void decreaseBrightness(View view) {
    	int i=-1;
    	SeekBar sb = null;
    	
    	switch(view.getId()) {
    	case R.id.ButtonDimm0:
    		sb = (SeekBar) findViewById(R.id.SeekBar0);
    		i = 0;
    		break;
    	case R.id.ButtonDimm1:
    		sb = (SeekBar) findViewById(R.id.DimmBar1);
    		i = 1;
    		break;
    	case R.id.ButtonDimm2:
    		sb = (SeekBar) findViewById(R.id.DimmBar2);
    		i = 2;
    		break;
    	case R.id.ButtonDimm3:
    		sb = (SeekBar) findViewById(R.id.DimmBar3);
    		i = 3;
    		break;
   		default:
   			Log.d("decreaseBrightness", "Other");
    		break;
    	}
    	
    	Log.d("dimmer", String.valueOf(i));
		new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=" + String.valueOf(i) + "&dimm=1");
		if (sb != null)
			sb.setProgress(sb.getProgress()-5);
    }
    
    public void toggleChannel(View view) {
    	int i = -1;
		Switch sw = (Switch) findViewById(view.getId());
    	SeekBar sb = null;

    	switch(view.getId()) {
    	case R.id.ButtonToggle0:
    		sb = (SeekBar) findViewById(R.id.SeekBar0);
    		i = 0;
    		break;
    	case R.id.ButtonToggle1:
    		sb = (SeekBar) findViewById(R.id.DimmBar1);
    		i = 1;
    		break;
    	case R.id.ButtonToggle2:
    		sb = (SeekBar) findViewById(R.id.DimmBar2);
    		i = 2;
    		break;
    	case R.id.ButtonToggle3:
    		sb = (SeekBar) findViewById(R.id.DimmBar3);
    		i = 3;
    		break;
   		default:
   			Log.d("dimmer", "Other");
    		return;
    	}

    	if (sw.isChecked()){
			Log.d("dimmer", "1");
			new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=" + String.valueOf(i) + "&state=on");
	    	if (sb != null)
	    		sb.setProgress(sb.getMax());
		} else {
			Log.d("dimmer", "0");
			new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=" + String.valueOf(i) + "&state=off");
	    	if (sb != null)
	    		sb.setProgress(0);
		}
    }
    
    public void switchAll(View view) {
    	String state;
    	switch(view.getId()) {
    	case R.id.ButtonAllOff:
    		state = "off";
    		break;
    	case R.id.ButtonAllOn:
    		state = "on";
    		break;
   		default:
   			return;
    	}
    	new RetrieveFeedTask().execute("http://192.168.1.228/test.sh?button=all&state="+state);
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
