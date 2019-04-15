package edu.azimjon.project.zamin.events;

public class PlayerStateEvent {
    public static final int PLAYER_STOP = 2000;
    public static final int PLAYER_PREV = 2001;
    public static final int PLAYER_NEXT = 2002;
    public static final int PLAYER_PLAY = 2003;
    public static final int PLAYER_LAST_TIME = 2004;
    public static final int PLAYER_NEXT_TIME = 2005;

    public static final int PLAYER_RESET = 2101;
    public static final int PLAYER_HIDE = 2102;
    public static final int PLAYER_PLAY_ICON = 2103;
    public static final int PLAYER_PAUSE_ICON = 2104;
    public static final int PLAYER_TITLE = 2105;
    public static final int PLAYER_UPDATE = 2106;

    public final int state;
    public final String value;

    public String startTime;
    public String endTime;

    public PlayerStateEvent(int state, String value) {
        this.state = state;
        this.value = value;
    }


}