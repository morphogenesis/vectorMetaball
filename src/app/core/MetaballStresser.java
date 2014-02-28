package app.core;

import java.util.ArrayList;

public class MetaballStresser {

    App app;
    MetaballManager manager;
    ArrayList<Metaball> metaballs;
    ArrayList<Vector2D> velocities;

    public MetaballStresser() {
        manager = new MetaballManager();
        metaballs = new ArrayList<>();
        velocities = new ArrayList<>();
    }
}
