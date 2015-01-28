package entities;

import game.Game;

import java.util.Arrays;

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

	public final static float spawnHeight = Game.screenDimension.y + 0.1f
			* Game.screenDimension.x;

	public final static float minDistToCenter = 0.02f * Game.screenDimension.x;
	public final static float maxDistToCenter = 0.1f * Game.screenDimension.x;

	private final static float maxRotationSpeed = 5;

	private PolygonSprite polygonSprite;
	private TextureRegion textureRegion;
	private short[] triangles;
	private float[] outlineVertices;

	public Vector2 centerSpawn, distanceTraveled, velocity;
	public float rotation, rotationSpeed;
	public Color color;

	public Obstacle() {
		generateGeometry();

		rotation = 0;
		rotationSpeed = MathUtils.random(-maxRotationSpeed, maxRotationSpeed);

		distanceTraveled = new Vector2();
		velocity = new Vector2(0, -0.5f * Math.abs(rotationSpeed));

		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(Game.obstacleColors.get(MathUtils
				.random(Game.obstacleColors.size - 1)));
		pix.fill();

		textureRegion = new TextureRegion(new Texture(pix));

		updatePolyRegAndOutlineVertices();
	}

	private void generateGeometry() {
		// randomly position obstacle center
		centerSpawn = new Vector2(MathUtils.random(Game.screenDimension.x),
				spawnHeight);

		// randomly pick obstacle number of vertices
		int numVertices = MathUtils.random(4, 7);

		// sector angle is the angle in which a vertex can be spawned
		float sectorAngle = 360f / numVertices;

		// allocate memory for the vertices, including the center
		float[] vertices = new float[2 + numVertices * 2];

		// save center to vertices array
		vertices[0] = centerSpawn.x;
		vertices[1] = centerSpawn.y;

		// randomly place each vertex inside its own sector
		for (int i = 0; i < numVertices; i++) {
			float vertexAngle = MathUtils.random(sectorAngle) + i * sectorAngle;

			float distToCenter = MathUtils.random(minDistToCenter,
					maxDistToCenter);

			// build point coordinates
			Vector2 point = new Vector2(centerSpawn.x, centerSpawn.y);
			point.x += MathUtils.cos(MathUtils.degreesToRadians * vertexAngle)
					* distToCenter;
			point.y += MathUtils.sin(MathUtils.degreesToRadians * vertexAngle)
					* distToCenter;

			// save it in vertices array
			vertices[(1 + i) * 2] = point.x;
			vertices[(1 + i) * 2 + 1] = point.y;
		}

		setOrigin(centerSpawn.x, centerSpawn.y);
		setVertices(vertices);

		// computing triangles required to fill shape with color
		triangles = new short[numVertices * 3];
		for (int i = 0; i < numVertices; i++) {
			triangles[i * 3] = 0;
			triangles[i * 3 + 1] = (short) (i + 1);
			triangles[i * 3 + 2] = (i == numVertices - 1) ? 1 : (short) (i + 2);
		}
	}

	public void update() {
		velocity.y -= 0.1 * Game.GRAVITY;
		distanceTraveled.y += velocity.y;

		rotation = (rotation + rotationSpeed) % 360;

		setPosition(0, distanceTraveled.y);
		setRotation(rotation);

		updatePolyRegAndOutlineVertices();
	}

	private void updatePolyRegAndOutlineVertices() {
		PolygonRegion polyReg = new PolygonRegion(textureRegion,
				getTransformedVertices(), triangles);

		polygonSprite = new PolygonSprite(polyReg);
		polygonSprite.setRegion(polyReg);

		outlineVertices = Arrays.copyOfRange(getTransformedVertices(), 2,
				getTransformedVertices().length);
	}

	public void draw() {
		// fill
		Game.polygonSpriteBatch.begin();
		polygonSprite.draw(Game.polygonSpriteBatch);
		Game.polygonSpriteBatch.end();

		// outline
		Game.shapeRenderer.begin(ShapeType.Line);
		Game.shapeRenderer.setColor(Color.BLACK);
		Game.shapeRenderer.polygon(outlineVertices);
		Game.shapeRenderer.end();
	}

	/** Returns whether an x, y pair is contained within the obstacle. */
	@Override
	public boolean contains(float x, float y) {
		final float[] vertices = outlineVertices;
		final int numFloats = vertices.length;
		int intersects = 0;

		for (int i = 0; i < numFloats; i += 2) {
			float x1 = vertices[i];
			float y1 = vertices[i + 1];
			float x2 = vertices[(i + 2) % numFloats];
			float y2 = vertices[(i + 3) % numFloats];

			if (((y1 <= y && y < y2) || (y2 <= y && y < y1))
					&& x < ((x2 - x1) / (y2 - y1) * (y - y1) + x1))
				intersects++;
		}

		return (intersects & 1) == 1;
	}

}
