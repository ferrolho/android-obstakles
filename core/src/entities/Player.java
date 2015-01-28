package entities;

import game.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Rectangle {

	private static final long serialVersionUID = 1L;

	public Color color;
	public Vector2 velocity;
	public float acceleration, breakSpeed, topSpeed;

	public Player(Color color) {
		this.color = color;

		float size = (int) (0.06 * Game.screenDimension.x);

		x = Game.screenDimension.x / 2 - size / 2;
		y = 0;

		width = size;
		height = size;

		velocity = new Vector2();
		acceleration = 0.001f * Game.screenDimension.x;
		breakSpeed = 1.2f * acceleration;
		topSpeed = 0.02f * Game.screenDimension.x;
	}

	public void update(float deltaTime, boolean movingLeft, boolean movingRight) {
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

		float framesElapsed = deltaTime / Game.SPF;

		// update x
		x += velocity.x * framesElapsed;

		// update y
		velocity.y -= Game.GRAVITY;
		y += velocity.y * framesElapsed;

		updateCollisions(deltaTime);
	}

	private void updateCollisions(float deltaTime) {
		float framesElapsed = deltaTime / Game.SPF;

		// ground collision
		if (y < 0) {
			velocity.y = 0;
			y = 0;
		}

		// side borders collision
		if (x < 0) {
			velocity.x = 2 * topSpeed;
			velocity.y = 10 * Game.GRAVITY;

			x += velocity.x * framesElapsed;
			y += velocity.y * framesElapsed;

			Game.bumpSound.play();
		} else if (x + width > Game.screenDimension.x) {
			velocity.x = -2 * topSpeed;
			velocity.y = 10 * Game.GRAVITY;

			x += velocity.x * framesElapsed;
			y += velocity.y * framesElapsed;

			Game.bumpSound.play();
		}
	}

	public void draw() {
		Game.shapeRenderer.begin(ShapeType.Filled);

		Game.shapeRenderer.setColor(color);
		Game.shapeRenderer.rect(x, y, width, height);

		Game.shapeRenderer.end();
	}

	public boolean overlaps(Obstacle obstacle) {
		if (obstacle.contains(x, y))
			return true;
		else if (obstacle.contains(x + width, y))
			return true;
		else if (obstacle.contains(x + width, y + height))
			return true;
		else if (obstacle.contains(x, y + height))
			return true;

		for (int i = 0; i < obstacle.getTransformedVertices().length / 2; i += 2)
			if (this.contains(obstacle.getTransformedVertices()[i],
					obstacle.getTransformedVertices()[i + 1]))
				return true;

		return false;
	}

}
