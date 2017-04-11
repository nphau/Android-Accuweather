package com.mobi.accuweather;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;
import com.mobi.accuweather.adapter.TitleNavigationAdapter;
import com.mobi.accuweather.model.SpinnerNavItem;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class MainActivity extends Activity implements ActionBar.OnNavigationListener {
	
	static int indexClose=0;
	
	WebView wv,wvv;
	String str;
	 // action bar
    private ActionBar actionBar;
 
    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;
     
    // Navigation adapter
    private TitleNavigationAdapter adapter;
	
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        indexClose++;
        if(indexClose%5==0)
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Download new apps?");
            builder.setMessage("Do you want to download some other apps?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                	 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://newad.goodapps.mobi"));
        		     startActivity(intent);
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });
            
            AlertDialog alert = builder.create();
            alert.show();
        }

        wv=(WebView)findViewById(R.id.webViewnews);
		wvv=(WebView)findViewById(R.id.webAdv);
		
	    this.wv.getSettings().setJavaScriptEnabled(true);
	    this.wvv.getSettings().setJavaScriptEnabled(true);
	    this.wvv.setInitialScale(1);
	    this.wvv.getSettings().setUseWideViewPort(true);
	    this.wvv.getSettings().setLoadWithOverviewMode(true);
	    this.wvv.getSettings().setSupportMultipleWindows(true);
	    this.wv.loadUrl("http://m.accuweather.com/");
	    this.wv.setWebViewClient(new WebViewClient()
	    {
	    });
	    wvv.loadUrl("http://goodapps.mobi/ad/accuweatherlocalandinternationallocations.php");
	    this.wvv.setVisibility(8);
	    new Handler().postDelayed(new Runnable()
	    {
	      public void run()
	      {
	        MainActivity.this.wvv.setVisibility(0);
	      }
	    }
	    , 3000L);
        
        actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_launcher);
        // Hide the action bar title
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Enabling Spinner dropdown navigation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
         
        // Spinner title navigation data
        navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerNavItem("Top Page", R.drawable.ic_action_merge));
        navSpinner.add(new SpinnerNavItem("Share Link", R.drawable.ic_action_share));
        navSpinner.add(new SpinnerNavItem("Exit", R.drawable.ic_action_back));
        
         
        OnNavigationListener callback = new OnNavigationListener() {

          

            @Override
            public boolean onNavigationItemSelected(int position, long id) {

                // Do stuff when navigation item is selected
            	if(position==1){
            		Intent share = new Intent(android.content.Intent.ACTION_SEND);
            		share.setType("text/plain");
            		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            		// Add data to the intent, the receiving app will decide
            		// what to do with it.
            		share.putExtra(Intent.EXTRA_SUBJECT, "AccuWeather - Local and International Locations");
            		share.putExtra(Intent.EXTRA_TEXT, "http://m.accuweather.com/");

            		startActivity(Intent.createChooser(share, "Share link!"));
            	}
            	
            	if(position==0){
            		wv.scrollTo(0, 0);
            	}
            	
            	if(position==2){
            		android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1); 
            	}
            	position=0;
            	//Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
                //Log.d("NavigationItemSelected", String.valueOf(position)); // Debug

                return true;

            }

        };
        // title drop down adapter
        adapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
        
        // assigning the spinner navigation     
        actionBar.setListNavigationCallbacks(adapter, callback);
        
       
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
		
		return super.onCreateOptionsMenu(menu);
    }
    
    /**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_search:
			// search action
			return true;
		case R.id.action_location_found:
			// location found
			LocationFound();
			return true;
		case R.id.action_refresh:
			 // refresh
			wv.loadUrl("http://m.accuweather.com/");
            // load the data from server
			return true;
		case R.id.action_help:
			// help action
			return true;
		case R.id.action_add:
			// help action
			 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mad.goodapps.mobi"));
		     startActivity(intent);
			return true;
		case R.id.action_check_updates:
			// check for updates action
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Launching new activity
	 * */
	private void LocationFound() {
		Intent i = new Intent(MainActivity.this, LocationFound.class);
		startActivity(i);
	}


	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	};
    
}