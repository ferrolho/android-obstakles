package game;

public interface ActionResolver {

	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(float lastScore, int lastWallCollisions,
			int totalWallCollisions);

	public void unlockAchievementGPGS(String achievementId);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();

}
