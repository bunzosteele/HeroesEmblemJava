package bunzosteele.heroesemblem;

public interface GameServicesController
{
	public void RecordAnalyticsEvent(String category, String action, String label, long value);
	public void SubmitHighScore(int score);
	public void ViewLeaderboard();
	public void DisconnectServices();
}
