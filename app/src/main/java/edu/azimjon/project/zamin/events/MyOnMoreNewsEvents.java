package edu.azimjon.project.zamin.events;

public class MyOnMoreNewsEvents {

    public static class MyOnMoreNewsEvent {
        int position;

        public MyOnMoreNewsEvent(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }
}