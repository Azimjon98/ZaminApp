package edu.azimjon.project.zamin.events;

public class MyNetworkEvents {

    public static class NetworkStateChangedEvent {
        int state;

        public NetworkStateChangedEvent(int state) {
            this.state = state;
        }

        public int isState() {
            return state;
        }
    }
}
