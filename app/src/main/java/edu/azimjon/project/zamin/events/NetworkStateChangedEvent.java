package edu.azimjon.project.zamin.events;

public class NetworkStateChangedEvent {
    public final int state;

    public NetworkStateChangedEvent(int state) {
        this.state = state;
    }
}
