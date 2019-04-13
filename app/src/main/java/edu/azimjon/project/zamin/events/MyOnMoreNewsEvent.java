package edu.azimjon.project.zamin.events;

public class MyOnMoreNewsEvent {

    public final int position;

    public MyOnMoreNewsEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}