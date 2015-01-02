package entities;

import game.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Obstacle extends Polygon {

	private final static float maxRotationSpeed = 5;

	private PolygonSprite polygonSprite;
	private TextureRegion textureRegion;
	private short[] triangles;

	public Vector2 displacement, velocity;
	public float rotation, rotationSpeed;
	public Color color;

	// // rectangle constructor
	// public Obstacle() {
	// float x = MathUtils.random(Game.screenDimension.x);
	// float y = Game.screenDimension.y;
	//
	// float width = MathUtils.random(0.01f * Game.screenDimension.x,
	// 0.2f * Game.screenDimension.x);
	// float height = MathUtils.random(0.01f * Game.screenDimension.x,
	// 0.2f * Game.screenDimension.x);
	//
	// float[] vertices = new float[] { x, y, x + width, y, x + width,
	// y + height, x, y + height };
	// setVertices(vertices);
	// setOrigin(x + width / 2, y + height / 2);
	//
	// rotation = 0;
	// rotationSpeed = MathUtils.random(-maxRotationSpeed, maxRotationSpeed);
	//
	// displacement = new Vector2();
	// velocity = new Vector2(0, -0.5f * Math.abs(rotationSpeed));
	//
	// Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
	// pix.setColor(Game.obstacleColors.get(MathUtils
	// .random(Game.obstacleColors.size - 1)));
	// pix.fill();
	//
	// Texture textureSolid = new Texture(pix);
	// textureRegion = new TextureRegion(textureSolid);
	// triangles = new short[] { 0, 1, 2, 0, 2, 3 };
	//
	// PolygonRegion polyReg = new PolygonRegion(textureRegion,
	// getTransformedVertices(), triangles);
	// polygonSprite = new PolygonSprite(polyReg);
	// polygonSprite.setRegion(polyReg);
	// }

	public Obstacle() {
		float x = MathUtils.random(Game.screenDimension.x);
		float y = Game.screenDimension.y;

		float width = MathUtils.random(0.01f * Game.screenDimension.x,
				0.2f * Game.screenDimension.x);
		float height = MathUtils.random(0.01f * Game.screenDimension.x,
				0.2f * Game.screenDimension.x);

		float[] vertices = new float[] { x, y, x + width, y, x + width,
				y + height, x, y + height };
		setVertices(vertices);
		setOrigin(x + width / 2, y + height / 2);

		rotation = 0;
		rotationSpeed = MathUtils.random(-maxRotationSpeed, maxRotationSpeed);

		displacement = new Vector2();
		velocity = new Vector2(0, -0.5f * Math.abs(rotationSpeed));

		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(Game.obstacleColors.get(MathUtils
				.random(Game.obstacleColors.size - 1)));
		pix.fill();

		Texture textureSolid = new Texture(pix);
		textureRegion = new TextureRegion(textureSolid);
		triangles = new short[] { 0, 1, 2, 0, 2, 3 };

		PolygonRegion polyReg = new PolygonRegion(textureRegion,
				getTransformedVertices(), triangles);
		polygonSprite = new PolygonSprite(polyReg);
		polygonSprite.setRegion(polyReg);
	}

	public void update() {
		velocity.y -= 0.1 * Game.GRAVITY;
		displacement.y += velocity.y;

		rotation = (rotation + rotationSpeed) % 360;

		setPosition(0, displacement.y);
		setRotation(rotation);

		PolygonRegion polyReg = new PolygonRegion(textureRegion,
				getTransformedVertices(), triangles);
		polygonSprite = new PolygonSprite(polyReg);
		polygonSprite.setRegion(polyReg);
	}

	public void draw() {
		Game.polygonSpriteBatch.begin();
		polygonSprite.draw(Game.polygonSpriteBatch);
		Game.polygonSpriteBatch.end();

		Game.shapeRenderer.begin(ShapeType.Line);
		Game.shapeRenderer.setColor(Color.BLACK);
		Game.shapeRenderer.polygon(getTransformedVertices());
		Game.shapeRenderer.end();
	}

}
