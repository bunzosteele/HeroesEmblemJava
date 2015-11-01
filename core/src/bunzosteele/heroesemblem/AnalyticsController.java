package bunzosteele.heroesemblem;

public interface AnalyticsController
{
	public void RecordEvent(String category, String action, String label, long value);
}
