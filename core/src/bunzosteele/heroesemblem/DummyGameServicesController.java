package bunzosteele.heroesemblem;

public class DummyGameServicesController implements GameServicesController
{
	@Override
	public void RecordAnalyticsEvent(String category, String action, String label, long value){		
	}
	
	@Override
	public void SubmitHighScore(int score){
	}
	
	@Override
	public void ViewLeaderboard(){
	}

	@Override
	public void DisconnectServices() {	
	}
}
