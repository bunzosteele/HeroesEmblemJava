package bunzosteele.heroesemblem.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import bunzosteele.heroesemblem.AdsController;
import bunzosteele.heroesemblem.GameServicesController;
import bunzosteele.heroesemblem.HeroesEmblem;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.plus.Plus;

public class AndroidLauncher extends AndroidApplication implements AdsController, GameServicesController, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private static final String PROPERTY_ID = "UA-69183424-2";
	private static final String LEADERBOARD_ID = "CgkIi_ntxuUZEAIQAQ";
	Tracker analytics;
	GoogleApiClient apiClient;
	boolean isSubmitClick = false;
	boolean isViewClick = false;
	boolean isSubmitting = false;
	boolean isViewing = false;
	boolean isResolvingConnectionFailure;
	int scoreToSubmit = -1;
	HeroesEmblem game;
	InterstitialAd interstitialAd;
	AdRequest.Builder adRequestBuilder;
	
	private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.game = new HeroesEmblem(this, this);
		View gameView = initializeForView(this.game, config);
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		setContentView(layout);
		setupAds();
	    GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
	    analytics = googleAnalytics.newTracker(PROPERTY_ID);
	    analytics.enableAdvertisingIdCollection(true);
	    apiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
        .addApi(Games.API).addScope(Games.SCOPE_GAMES)
        .build();
	    apiClient.connect();
	    isResolvingConnectionFailure = false;
	}
	
	@Override
	public void RecordAnalyticsEvent(String category, String action, String label, long value){		
		this.analytics.send(new HitBuilders.EventBuilder()
			.setCategory(category)
			.setAction(action)
			.setLabel(label)
			.setValue(value)
			.build());
	}
	
	@Override
	public void SubmitHighScore(int score) {
		isSubmitClick = true;
		isSubmitting = true;
		scoreToSubmit = score;
		if(!apiClient.isConnected()){
			apiClient.connect();
		}else{
			SubmitScore();
		}
	}
	
	@Override
	public void ViewLeaderboard() {
		isViewClick = true;
		isViewing = true;
		if(!apiClient.isConnected()){
			apiClient.connect();	
		}else{
			ShowLeaderboard();
		}
	}
	
	@Override
	public void DisconnectServices() {
		apiClient.disconnect();		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid()); 
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		this.game.SaveData();
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean isWifiConnected(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
 
		return (ni != null && ni.isConnected());
	}
	
	private void setupAds(){	
		MobileAds.initialize(this, "ca-app-pub-9270769703022419~7341291688");
		adRequestBuilder = new AdRequest.Builder();
		adRequestBuilder.addTestDevice("4628d7c3");
		interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-9270769703022419/9896344621");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(adRequestBuilder.build());
                //Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
            	super.onAdFailedToLoad(errorCode);
                interstitialAd.loadAd(adRequestBuilder.build());
                //Toast.makeText(getApplicationContext(), "Failed to load: " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
        interstitialAd.loadAd(adRequestBuilder.build());
	}
	
	@Override
	public void showInterstitialAd() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd.isLoaded()) {
						interstitialAd.show();
						//Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
					}else{
						AdRequest interstitialRequest = adRequestBuilder.build();
						interstitialAd.loadAd(interstitialRequest);
						//Toast.makeText(getApplicationContext(), "Starting new Load", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	    
	}

    @Override
    public void onConnected(Bundle bundle) {     
		Log.d("Leaderboards", "onConnected(): connected");
        if(scoreToSubmit > 0 && isSubmitting){
        	SubmitScore();
        }  else if(isViewing){
        	ShowLeaderboard();
        }
    }
    
    private void SubmitScore(){
		Log.d("Leaderboards", "onConnected(): submitting score");
    	Games.Leaderboards.submitScore(apiClient, LEADERBOARD_ID, scoreToSubmit);
    	scoreToSubmit = -1;
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(apiClient, LEADERBOARD_ID), RC_UNUSED);
        isSubmitting = false;
    }
    
    private void ShowLeaderboard(){
		Log.d("Leaderboards", "onConnected(): viewing leaderboards");
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(apiClient, LEADERBOARD_ID), RC_UNUSED);
        isViewing = false;
    }

    @Override
    public void onConnectionSuspended(int i) {
		Log.d("Leaderboards", "onConnectionSuspended(): connection suspended");
        apiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Leaderboards", "onConnectionFailed(): attempting to resolve");
        if (isResolvingConnectionFailure) {
            Log.d("Leaderboards", "onConnectionFailed(): already resolving");
            return;
        }
        
        if(isSubmitClick || isViewClick){
        	isSubmitClick = false;
        	isViewClick = false;
        	isResolvingConnectionFailure = true;
        	if (!resolveConnectionFailure(this, apiClient, connectionResult, RC_SIGN_IN, "Sign In Other Error")) {
	            isResolvingConnectionFailure = false;
	        	Context context = getApplicationContext();
	        	CharSequence text = "Failed to connect.";
	        	int duration = Toast.LENGTH_SHORT;
	        	Toast.makeText(context, text, duration).show();
        	}
        }    
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            isSubmitClick = false;
            isViewClick = false;
            isResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                apiClient.connect();
            } else {
                showActivityResultError(this, requestCode, resultCode, "Unknown Error Occured");
            }
        }
    }
    
    private static boolean resolveConnectionFailure(Activity activity, GoogleApiClient client, ConnectionResult result, int requestCode, String fallbackErrorMessage) {
    	if (result.hasResolution()) {
            Log.d("Leaderboards", "resolveConnectionFailure(): has resolution");
    		try {
    			result.startResolutionForResult(activity, requestCode);
    			Log.d("Leaderboards", "resolveConnectionFailure(): reolution started");
    			return true;
    		} catch (IntentSender.SendIntentException e) {
    			// The intent was canceled before it was sent.  Return to the default
    			// state and attempt to connect to get an updated ConnectionResult.
    			client.connect();
    			return false;
    		}
    	} else {
    		// not resolvable... so show an error message
    		int errorCode = result.getErrorCode();
    		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
    				activity, requestCode);
    		if (dialog != null) {
    			dialog.show();
    		} else {
    			// no built-in dialog: show the fallback error message
    			showAlert(activity, fallbackErrorMessage);
    		}
    		return false;
    	}
    }
    
    private static void showActivityResultError(Activity activity, int requestCode, int actResp, String errorDescription) {
        if (activity == null) {
            Log.e("Leaderboards", "*** No Activity. Can't show failure dialog!");
            return;
        }
        Dialog errorDialog;

        switch (actResp) {
            case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
                errorDialog = makeSimpleDialog(activity, "App misconfigured.");
                break;
            case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
                errorDialog = makeSimpleDialog(activity, "Sign-in failed.");
                break;
            case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
                errorDialog = makeSimpleDialog(activity,"License Failed.");
                break;
            default:
                // No meaningful Activity response code, so generate default Google
                // Play services dialog
                final int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
                errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
                        activity, requestCode, null);
                if (errorDialog == null) {
                    // get fallback dialog
                    Log.e("BaseGamesUtils",
                            "No standard error dialog available. Making fallback dialog.");
                    errorDialog = makeSimpleDialog(activity, errorDescription);
                }
        }

        errorDialog.show();
    }
    
    private static void showAlert(Activity activity, String message) {
        (new AlertDialog.Builder(activity)).setMessage(message)
                .setNeutralButton(android.R.string.ok, null).create().show();
    }
    
    private static Dialog makeSimpleDialog(Activity activity, String text) {
        return (new AlertDialog.Builder(activity)).setMessage(text)
                .setNeutralButton(android.R.string.ok, null).create();
    }
    
    
}
