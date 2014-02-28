package app.core;

import toxi.geom.Polygon2D;

import java.util.ArrayList;

public class MetaballManager {

	private static MetaballManager instance;
	private static boolean lock = false;
	public static float viscosity = 2.0f;
	public static float threshold = 0.0006f;
	public static float resolution = 10.0f;
	public static int maxSteps = 400;
	private ArrayList<Metaball> _metaballs;
	private Polygon2D _outline;
	private float minStrength;

	public MetaballManager() {
		if (lock) {
			throw new Error("Error: Instantiation failed. Use app.core.MetaballManager.getInstance() instead of new.");
		} else {
			_metaballs = new ArrayList<>();
			_outline = new Polygon2D();
			minStrength = Metaball.MIN_STRENGTH;
		}
	}

	public static MetaballManager getInstance() {
		if (instance == null) {
			instance = new MetaballManager();
			lock = true;
		} return instance;
	}

	public void draw() {
		_outline = new Polygon2D();
		Vector2D seeker = new Vector2D();
		int i;
		for (Metaball metaball : _metaballs) {
			metaball.tracked = false;
			seeker.copy(metaball.getPosition());
			i = 0;
			while ((stepToEdge(seeker) > threshold) && (++i < 50)) {}
				metaball.edge.copy(seeker);
		}
		int edgeSteps = 0;
		Metaball current = untrackedMetaball();
		seeker.copy(current.edge);
		_outline.add(seeker.x, seeker.y);
		while (current != null && edgeSteps < maxSteps) {
			rk2(seeker, resolution);
			_outline.add(seeker.x, seeker.y);
			for (Metaball metaball : _metaballs) {
				if (seeker.dist(metaball.edge) < (resolution * 0.9)) {
					seeker.copy(metaball.edge);
					_outline.add(seeker.x, seeker.y);
					current.tracked = true;
					if (metaball.tracked) {
						current = untrackedMetaball();
						if (current == metaball) {
							seeker.copy(current.edge);
							_outline.add(seeker.x, seeker.y);
						}
					} else { current = metaball; } break;
				}
			} ++edgeSteps;
		}
		_outline.reduceVertices(50);
	}

	private Metaball untrackedMetaball() {
		for (Metaball metaball : _metaballs) {
			if (!metaball.tracked) { return metaball; }
		} return null;
	}
	private float stepToEdge(Vector2D seeker) {
		float force = fieldStrength(seeker);
		float stepsize;
		stepsize = (float) Math.pow(minStrength / threshold, 1 / viscosity) - (float) Math.pow(minStrength / force, 1 / viscosity) + 0.01f;
		seeker.add(fieldNormal(seeker).multiply(stepsize));
		return force;
	}
	private float fieldStrength(Vector2D v) {
		float force = 0.0f;
		for (Metaball metaball : _metaballs) {
			force += metaball.strengthAt(v, viscosity);
		} return force;
	}
	private Vector2D fieldNormal(Vector2D v) {
		Vector2D force = new Vector2D();
		Vector2D radius;
		for (Metaball metaball : _metaballs) {
			radius = Vector2D.subtract(metaball.getPosition(), v);
			if (radius.getLengthSq() == 0) { continue; }
			radius.multiply(-viscosity * metaball.getStrength() * (1 / (float) Math.pow(radius.getLengthSq(), (2 + viscosity) * 0.5f)));
			force.add(radius);
		} return force.norm();
	}
	private void rk2(Vector2D v, float h) {
		Vector2D t1 = fieldNormal(v).getPerpLeft();
		t1.multiply(h * 0.5f);
		Vector2D t2 = fieldNormal(Vector2D.add(v, t1)).getPerpLeft();
		t2.multiply(h);
		v.add(t2);
	}

	public void addMetaball(Metaball metaball) { minStrength = Math.min(metaball.getStrength(), minStrength); _metaballs.add(metaball); }
	public void removeMetaball(Metaball metaball) { _metaballs.remove(metaball); }
	public Polygon2D getOutline() { return _outline; }
	public int getSize() { return _metaballs.size(); }
}
/*
	public void removeMetaball(Metaball metaball) {
				int index = _metaballs.indexOf(metaball);
				if (index < 0) { throw new Error("app.core.Metaball not found."); }
		      _metaballs.splice(index, 1);
	}*/
/*
* 	public void draw() {
		_outline = new Polygon2D();
		app.beginShape();
		Vector2D seeker = new Vector2D();
		int i;
		for (Metaball metaball : _metaballs) {
			metaball.tracked = false;
			seeker.copy(metaball.getPosition());
			i = 0;
			while ((stepToEdge(seeker) > threshold) && (++i < 50)) { }
			metaball.edge.copy(seeker);
		}
		int edgeSteps = 0;
		Metaball current = untrackedMetaball();
		seeker.copy(current.edge);
		app.ellipse(seeker.x, seeker.y, 5, 5);
		app.vertex(seeker.x, seeker.y);
		_outline.add(seeker.x, seeker.y);
		while (current != null && edgeSteps < maxSteps) {
			rk2(seeker, resolution);
			app.ellipse(seeker.x, seeker.y, 5, 5);
			app.vertex(seeker.x, seeker.y);
			_outline.add(seeker.x, seeker.y);
			for (Metaball metaball : _metaballs) {
				if (seeker.dist(metaball.edge) < (resolution * 0.9)) {
					seeker.copy(metaball.edge);
					app.ellipse(seeker.x, seeker.y, 5, 5);
					app.vertex(seeker.x, seeker.y);
					_outline.add(seeker.x, seeker.y);
					current.tracked = true;
					if (metaball.tracked) {
						current = untrackedMetaball();
						if (current == metaball) {
							seeker.copy(current.edge);
							app.ellipse(seeker.x, seeker.y, 5, 5);
							app.point(seeker.x, seeker.y);
							_outline.add(seeker.x, seeker.y);
						}
					} else { current = metaball; } break;
				}
			} ++edgeSteps;
		} app.endShape(PApplet.OPEN);
	}

* */