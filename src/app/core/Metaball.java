package app.core;

public class Metaball {

    public static float MIN_STRENGTH = 1;
    public static float MAX_STRENGTH = 100;
    private Vector2D _position;
    private float _strength;
    boolean tracked;
    Vector2D edge;
    Vector2D direction;

    public Metaball(Vector2D position, float strength) {
        _position = position.clone();
        _strength = strength;
        tracked = false;
        edge = position.clone();
        direction = new Vector2D((float) Math.random() * 2 - 1, (float) Math.random() * 2 - 1);
    }

    public float strengthAt(Vector2D v, float c) {
        float div = (float) Math.pow(Vector2D.subtract(_position, v).getLengthSq(), c * 0.5f);
        return (div != 0) ? (_strength / div) : 10000;
    }

    public Vector2D getPosition() {
        return _position;
    }

    public void setPosition(Vector2D value) {
        _position.copy(value);
    }

    public float getStrength() {
        return _strength;
    }

    public void setStrength(float value) {
        _strength = MathUtil.clamp(value, MIN_STRENGTH, MAX_STRENGTH);
    }

    public String toString() {
        return "a string";
        //return "[object app.core.Metaball][position=" + getPosition() + "][size=" + getStrength().toFixed(4) + "]";
    }
}
