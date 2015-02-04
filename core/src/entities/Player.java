package entities;

import game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public final class Player extends Rectangle {

	private static final long serialVersionUID = 1L;

	public static Color color;

	public Vector2 velocity;
	public float acceleration, breakSpeed, topSpeed;

	public Player() {
		final float size = (int) (0.06 * Game.screenDimension.x);

		x = Game.screenDimension.x / 2 - size / 2;
		y = 0;

		width = size;
		height = size;

		velocity = new Vector2();
		acceleration = 0.001f * Game.screenDimension.x;
		breakSpeed = 1.2f * acceleration;
		topSpeed = 0.02f * Game.screenDimension.x;
	}

	public void update(boolean movingLeft, boolean movingRight) {
		// not touching
		if (!movingLeft && !movingRight && velocity.x != 0) {
			if (velocity.x > 0) {
				velocity.x -= breakSpeed;

				if (velocity.x < 0)
					velocity.x = 0;
			} else if (velocity.x < 0) {
				velocity.x += breakSpeed;

				if (velocity.x > 0)
					velocity.x = 0;
			}
		} else {
			// touching right
			if (movingRight) {
				if (velocity.x < 0)
					velocity.x += breakSpeed;
				else if (velocity.x < topSpeed)
					velocity.x += acceleration;
			}

			// touching left
			if (movingLeft) {
				if (velocity.x > 0)
					velocity.x -= breakSpeed;
				else if (velocity.x > -topSpeed)
					velocity.x -= acceleration;
			}
		}

		// update x
		x += velocity.x;

		// update y
		velocity.y -= Game.gravity;
		y += velocity.y;

		updateCollisions();
	}

	private void updateCollisions() {
		// ground collision
		if (y < 0) {
			velocity.y = 0;
			y = 0;
		}

		// side borders collision
		if (x < 0) {
			velocity.x = 2 * topSpeed;
			velocity.y = 10 * Game.gravity;

			x += velocity.x;
			y += velocity.y;

			Game.bumpSound.play();
			Gdx.input.vibrate(100);
		} else if (x + width > Game.screenDimension.x) {
			velocity.x = -2 * topSpeed;
			velocity.y = 10 * Game.gravity;

			x += velocity.x;
			y += velocity.y;

			Game.bumpSound.play();
			Gdx.input.vibrate(100);
		}
	}

	public void draw() {
		Game.shapeRenderer.begin(ShapeType.Filled);

		Game.shapeRenderer.setColor(color);
		Game.shapeRenderer.rect(x, y, width, height);

		Game.shapeRenderer.end();
	}

	public boolean overlaps(Obstacle obstacle) {
		if (obstacle.contains(x, y + height))
			return true;
		if (obstacle.contains(x + width, y + height))
			return true;
		if (obstacle.contains(x, y))
			return true;
		if (obstacle.contains(x + width, y))
			return true;

		final float[] transformedVertices = obstacle.transformedVertices;
		final int numVertices = transformedVertices.length / 2;

		for (int i = 0; i < numVertices; i += 2)
			if (contains(transformedVertices[i], transformedVertices[i + 1]))
				return true;

		return false;
	}

}
