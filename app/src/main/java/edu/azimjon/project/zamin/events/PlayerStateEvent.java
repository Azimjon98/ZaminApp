package edu.azimjon.project.zamin.events;

public class PlayerStateEvent {
    public static final int PLAYER_PREV = 2001;
    public static final int PLAYER_NEXT = 2002;
    public static final int PLAYER_PLAY = 2003;
    public static final int PLAYER_TITLE = 2004;
    public static final int PLAYER_LAST_TIME = 2005;
    public static final int PLAYER_NEXT_TIME = 2006;
    public static final int PLAYER_PROGRESS = 2007;

    public static final int PLAYER_SHOW = 2101;
    public static final int PLAYER_HIDE = 2102;

    public final int state;
    public final String value;

    public PlayerStateEvent(int state, String value) {
        this.state = state;
        this.value = value;
    }
}