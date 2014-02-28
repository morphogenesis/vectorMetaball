package app.core;

import processing.core.PApplet;
import toxi.geom.Line2D;
import toxi.geom.Vec2D;

import java.util.ArrayList;

public class App extends PApplet {

	public static void main(String[] args) {
		PApplet.main(new String[]{("app.core.App")});
	}
	Metaball target;
	MetaballManager manager;
	ArrayList<Metaball> metaballs;
	boolean drawMetaball = true;
	public void setup() {
		size(900, 600);

//		target = new Metaball(new Vector2D(mouseX, mouseY), 2);
		manager = MetaballManager.getInstance();
		metaballs = new ArrayList<>();
//		manager.addMetaball(new Metaball(new Vector2D(200, 200), 3));
//		manager.addMetaball(new Metaball(new Vector2D(400, 100), 1));
//		manager.addMetaball(new Metaball(new Vector2D(450, 240), 4));
//		manager.addMetaball(target);
	}

	public void draw() {
		background(250);
		noStroke();
//		update();

		if (metaballs.size() > 0) {
			manager.draw();
			noFill();
			stroke(0xff333333);
			for (Line2D l : manager.getOutline().getEdges()) { line(l.a.x, l.a.y, l.b.x, l.b.y); }
			fill(0xffffffff);
			stroke(0xff222222);
			for (Vec2D v : manager.getOutline().vertices) { ellipse(v.x, v.y, 5, 5); }
		}
	}
	public void mousePressed(){
		addMetaball(new Vector2D(mouseX,mouseY));
	}

	public void keyPressed() {
		if (key == 's') drawMetaball = !drawMetaball;
	}
	void addMetaball(Vector2D pos) {
		Metaball m = new Metaball(pos, random(1, 4));
		metaballs.add(m);
		manager.addMetaball(m);
		MetaballManager.maxSteps = metaballs.size() * 400;
	}
	void update() {
		target.setPosition(new Vector2D(MathUtil.clamp(mouseX, 50, 750), MathUtil.clamp(mouseY, 50, 350)));
	}
}
