package bunzosteele.heroesemblem.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import bunzosteele.heroesemblem.AdsController;
import bunzosteele.heroesemblem.AnalyticsController;
import bunzosteele.heroesemblem.HeroesEmblem;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AndroidLauncher extends AndroidApplication implements AdsController, AnalyticsController {
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-9270769703022419/8818024885";
	private static final String PROPERTY_ID = "UA-69183424-2";
	Tracker analytics;
	AdView bannerAd;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new HeroesEmblem(this, this), config);
		setupAds();
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(bannerAd, params);
		setContentView(layout);
	    GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
	    analytics = googleAnalytics.newTracker(PROPERTY_ID);
	    analytics.enableAdvertisingIdCollection(true);
	}
	
	@Override
	public void RecordEvent(String category, String action, String label, long value){		
		this.analytics.send(new HitBuilders.EventBuilder()
			.setCategory(category)
			.setAction(action)
			.setLabel(label)
			.setValue(value)
			.build());
	}
	
	@Override
	public void onDestroy()
	{
	        super.onDestroy();
	        this.finish();
	        android.os.Process.killProcess(android.os.Process.myPid()); 
	}

	
	@Override
	public void showBannerAd(){
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	bannerAd.setVisibility(View.VISIBLE);
	            AdRequest.Builder builder = new AdRequest.Builder();
	            AdRequest ad = builder.build();
	            bannerAd.loadAd(ad);
	        }
	    });	
	}
	
	@Override
	public void hideBannerAd() {
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            bannerAd.setVisibility(View.INVISIBLE);
	        }
	    });
	}
	
	@Override
	public boolean isWifiConnected(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
 
		return (ni != null && ni.isConnected());
	}
	
	private void setupAds(){
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(0xff000000);
		bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
		bannerAd.setAdSize(AdSize.SMART_BANNER);
	}
}
