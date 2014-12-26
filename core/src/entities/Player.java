package entities;

import states.GamePlayState;
import game.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Player {

	public Vector2 position, velocity;
	public int size;
	public float acceleration, breakSpeed, topSpeed;

	public Color color;

	public Player(Color color) {
		this.color = color;

		size = (int) (0.06 * Game.screenDimension.x);

		position = new Vector2(Game.screenDimension.x / 2 - size / 2, 0);
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
		position.x += velocity.x;
		// update y
		velocity.y -= Game.GRAVITY;
		position.y += velocity.y;

		updateCollisions();
	}

	private void updateCollisions() {
		// ground collision
		if (position.y < 0) {
			velocity.y = 0;
			position.y = 0;
		}

		// side borders collision
		if (position.x < 0) {
			velocity.x = 2 * topSpeed;
			velocity.y = 10 * Game.GRAVITY;

			position.x += velocity.x;
			position.y += velocity.y;

			GamePlayState.bumpSound.play();
		} else if (position.x + size > Game.screenDimension.x) {
			velocity.x = -2 * topSpeed;
			velocity.y = 10 * Game.GRAVITY;

			position.x += velocity.x;
			position.y += velocity.y;

			GamePlayState.bumpSound.play();
		}
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(position.x, position.y, size, size);
		shapeRenderer.end();
	}

}
