package com.dkebaili.slgq;

import com.dkebaili.slgq.display.AnalogSlgqDrawable;
import com.dkebaili.slgq.display.DigitalDisplayBox;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	AnalogSlgqDrawable drawable;
    	DigitalDisplayBox[] digitalDisplayBoxes = new DigitalDisplayBox[4];

        public PlaceholderFragment() {
        }

        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            drawable = new AnalogSlgqDrawable(true, true);
            rootView.findViewById(R.id.analog_slgq).setBackgroundDrawable(drawable);
            for (int i = 0; i < 4; ++i) {
            	digitalDisplayBoxes[i] = (DigitalDisplayBox)((ViewGroup)rootView.findViewById(R.id.digital_boxes)).getChildAt(i); 
            }
            return rootView;
        }
        @Override public void onPause() {
        	super.onPause();
        	drawable.onPause();
            for (int i = 0; i < 4; ++i) {
            	digitalDisplayBoxes[i].onPause(); 
            }
        }
        @Override public void onResume() {
        	super.onResume();
        	drawable.onResume();
            for (int i = 0; i < 4; ++i) {
            	digitalDisplayBoxes[i].onResume(); 
            }
        }
    }

}
