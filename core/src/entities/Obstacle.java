package entities;

import game.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle extends Rectangle {

	private static final long serialVersionUID = 1L;

	public Vector2 halfDimension, velocity;
	public float rotation, rotationSpeed;
	public Color color;

	public Obstacle(float x, float y, float width, float height, Color color) {
		super(x, y, width, height);

		halfDimension = new Vector2(width / 2, height / 2);
		velocity = new Vector2();

		this.rotation = 0;
		rotationSpeed = MathUtils.random(-10, 10);

		this.color = color;
	}

	public void update() {
		velocity.y -= 0.1 * Game.GRAVITY;
		y += velocity.y;

		rotation = (rotation + rotationSpeed) % 360;
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(color);
		shapeRenderer.rect(x, y, halfDimension.x, halfDimension.y, width,
				height, 1, 1, rotation);

		shapeRenderer.end();
	}

}
