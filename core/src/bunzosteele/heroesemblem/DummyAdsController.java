package bunzosteele.heroesemblem;

public class DummyAdsController implements AdsController
{
	@Override
	public void showInterstitialAd(){		
	}

	@Override
	public boolean isWifiConnected(){
		return false;
	}
}
