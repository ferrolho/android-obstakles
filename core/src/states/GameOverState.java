package states;

import game.Game;

import java.text.DecimalFormat;

import utilities.Label;
import utilities.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameOverState extends State implements InputProcessor {

	private final Label scoreLbl;
	private final float lastScoreInfoX;
	private final float bestScoreInfoX;

	private Color fontColor;
	private BitmapFont labelsFont, scoresFont, smallLabelsFont;
	private String lastScoreLbl, bestScoreLbl, lastScoreStr, bestScoreStr;
	private int lastLblX, lastScoreX, bestLblX, bestScoreX, labelsY, scoresY;

	private float buttonsY;
	private Rectangle retryBtn, leaderboardBtn, achievementsBtn;
	private Texture retryText, leaderboardText, achievementsText;
	private String retryLbl, leaderboardsLbl, achievementsLbl;

	private Touch touch;

	private Color filterColor;

	public GameOverState() {
		scoreLbl = new Label("",
				(int) Math.round(0.04 * Game.screenDimension.x), 0.85f);
		lastScoreInfoX = Game.screenDimension.x / 3.0f;
		bestScoreInfoX = Game.screenDimension.x * 2.0f / 3.0f;
	}

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		Game.udpateScore();

		fontColor = Color.DARK_GRAY;

		prepareScores();

		buttonsY = 0.2f * Game.screenDimension.y;
		float bigBtnSize = 0.2f;
		float smallBtnSize = bigBtnSize - 0.05f;

		leaderboardBtn = new Rectangle(Game.screenDimension.x / 8.0f, buttonsY,
				Game.screenDimension.x * smallBtnSize, Game.screenDimension.x
						* smallBtnSize);

		achievementsBtn = new Rectangle(leaderboardBtn);
		achievementsBtn.x = Game.screenDimension.x * 7.0f / 8.0f
				- leaderboardBtn.width;

		retryBtn = new Rectangle(Game.screenDimension.x / 2, buttonsY,
				Game.screenDimension.x * bigBtnSize, Game.screenDimension.x
						* bigBtnSize);
		retryBtn.x -= retryBtn.width / 2;

		leaderboardText = new Texture(
				Gdx.files.internal("images/leaderboards-icon.png"));
		achievementsText = new Texture(
				Gdx.files.internal("images/achievements-icon.png"));
		retryText = new Texture(Gdx.files.internal("images/retry-icon.png"));

		touch = new Touch();

		filterColor = new Color(1, 1, 1, 0.7f);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
		Game.clearScreen(255, 255, 255, 1);

		Game.player.draw();

		Game.drawObstacles();

		renderFilter();

		renderScore();

		renderButtons();
	}

	@Override
	public void dispose() {
	}

	private void prepareScores() {
		labelsFont = Game.fontManager.getFont(scoreLbl.size);
		labelsFont.setColor(fontColor);

		scoresFont = Game.fontManager.getFont((int) (2.5 * scoreLbl.size));
		scoresFont.setColor(fontColor);

		smallLabelsFont = Game.fontManager.getFont((int) (0.8 * scoreLbl.size));
		smallLabelsFont.setColor(fontColor);

		lastScoreLbl = "Score";
		bestScoreLbl = "Best";

		DecimalFormat f = new DecimalFormat("##0.00");
		lastScoreStr = f.format(Game.lastScore);
		bestScoreStr = f.format(Game.bestScore);

		lastLblX = (int) (lastScoreInfoX - labelsFont.getBounds(lastScoreLbl).width / 2);
		lastScoreX = (int) (lastScoreInfoX - scoresFont.getBounds(lastScoreStr).width / 2);

		bestLblX = (int) (bestScoreInfoX - labelsFont.getBounds(bestScoreLbl).width / 2);
		bestScoreX = (int) (bestScoreInfoX - scoresFont.getBounds(bestScoreStr).width / 2);

		labelsY = (int) (scoreLbl.position * Game.screenDimension.y + labelsFont
				.getBounds(lastScoreLbl).height / 2);
		scoresY = (int) (scoreLbl.position * Game.screenDimension.y - labelsFont
				.getBounds(lastScoreLbl).height);
	}

	private void renderFilter() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Game.shapeRenderer.begin(ShapeType.Filled);

		Game.shapeRenderer.setColor(filterColor);
		Game.shapeRenderer.rect(0, 0, Game.screenDimension.x,
				Game.screenDimension.y);

		Game.shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void renderScore() {
		Game.spriteBatch.begin();

		if (Game.lastScore == Game.bestScore)
			scoresFont.setColor(0, 153f / 255f, 0, 1); // dark green
		else
			scoresFont.setColor(fontColor);

		labelsFont.draw(Game.spriteBatch, lastScoreLbl, lastLblX, labelsY);
		scoresFont.draw(Game.spriteBatch, lastScoreStr, lastScoreX, scoresY);

		labelsFont.draw(Game.spriteBatch, bestScoreLbl, bestLblX, labelsY);
		scoresFont.draw(Game.spriteBatch, bestScoreStr, bestScoreX, scoresY);

		Game.spriteBatch.end();
	}

	private void renderButtons() {
		retryLbl = "Retry";
		leaderboardsLbl = "Leaderboards";
		achievementsLbl = "Achievements";

		Rectangle rect;
		String lbl;

		Game.shapeRenderer.begin(ShapeType.Filled);
		Game.shapeRenderer.setColor(0.90f, 0.90f, 0.90f, 1);

		rect = retryBtn;
		Game.shapeRenderer.circle(rect.x + rect.width / 2, rect.y + rect.height
				/ 2, rect.width / 2);

		if (Gdx.app.getType() != ApplicationType.Desktop) {
			rect = leaderboardBtn;
			Game.shapeRenderer.circle(rect.x + rect.width / 2, rect.y
					+ rect.height / 2, rect.width / 2);

			rect = achievementsBtn;
			Game.shapeRenderer.circle(rect.x + rect.width / 2, rect.y
					+ rect.height / 2, rect.width / 2);
		}

		Game.shapeRenderer.end();

		Game.spriteBatch.begin();
		int margin = (int) (0.04 * Game.screenDimension.x);

		rect = retryBtn;
		lbl = retryLbl;
		Game.spriteBatch.draw(retryText, rect.x + margin, rect.y + margin,
				rect.width - 2 * margin, rect.height - 2 * margin, 0, 1, 1, 0);
		smallLabelsFont.draw(Game.spriteBatch, lbl, rect.x + rect.width / 2
				- smallLabelsFont.getBounds(lbl).width / 2, rect.y
				- smallLabelsFont.getBounds(lbl).height / 2);

		if (Gdx.app.getType() != ApplicationType.Desktop) {
			margin *= 0.75f;

			rect = leaderboardBtn;
			lbl = leaderboardsLbl;
			Game.spriteBatch.draw(leaderboardText, rect.x + margin, rect.y
					+ margin, rect.width - 2 * margin,
					rect.height - 2 * margin, 0, 1, 1, 0);
			smallLabelsFont.draw(Game.spriteBatch, lbl, rect.x + rect.width / 2
					- smallLabelsFont.getBounds(lbl).width / 2, rect.y
					- smallLabelsFont.getBounds(lbl).height / 2);

			rect = achievementsBtn;
			lbl = achievementsLbl;
			Game.spriteBatch.draw(achievementsText, rect.x + margin, rect.y
					+ margin, rect.width - 2 * margin,
					rect.height - 2 * margin, 0, 1, 1, 0);
			smallLabelsFont.draw(Game.spriteBatch, lbl, rect.x + rect.width / 2
					- smallLabelsFont.getBounds(lbl).width / 2, rect.y
					- smallLabelsFont.getBounds(lbl).height / 2);
		}

		Game.spriteBatch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (Gdx.app.getType()) {
		case Android:
			if (keycode == Keys.BACK)
				StateManager.changeState(new MainMenuState());
			break;

		case Desktop:
			if (keycode == Keys.ESCAPE || keycode == Keys.SPACE)
				StateManager.changeState(new MainMenuState());
			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch.position.x = screenX;
		touch.position.y = screenY;
		touch.touched = true;

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (Math.abs(touch.position.x - screenX) < Game.touchTolerance
				&& Math.abs(touch.position.y - screenY) < Game.touchTolerance) {
			if (retryBtn.contains(screenX, Game.screenDimension.y - screenY)) {
				StateManager.changeState(new GamePlayState());
				Game.clickSound.play();
			}

			if (Gdx.app.getType() != ApplicationType.Desktop) {
				if (leaderboardBtn.contains(screenX, Game.screenDimension.y
						- screenY)) {
					Game.actionResolver.getLeaderboardGPGS();
					Game.clickSound.play();
				} else if (achievementsBtn.contains(screenX,
						Game.screenDimension.y - screenY)) {
					Game.actionResolver.getAchievementsGPGS();
					Game.clickSound.play();
				}
			}
		}

		touch.position.x = 0;
		touch.position.y = 0;
		touch.touched = false;

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
