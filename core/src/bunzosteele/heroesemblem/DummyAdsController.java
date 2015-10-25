package bunzosteele.heroesemblem;

public class DummyAdsController implements AdsController
{
	@Override
	public void showBannerAd(){		
	}
	
	@Override
	public void hideBannerAd(){	
	}
	
	@Override
	public boolean isWifiConnected(){
		return false;
	}
}
