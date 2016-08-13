package GameState;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;

    public static final int NUMBER_OF_GAME_STATES = 3;
    public static final int MENU_STATE = 0;
    public static final int PLAY_STATE = 1;
    public static final int CONTROLS_STATE = 2;

    public GameStateManager() {

        gameStates = new GameState[NUMBER_OF_GAME_STATES];

        currentState = MENU_STATE;
        loadState(currentState);
    }

    private void loadState(int state) {
        if(state == MENU_STATE)
            gameStates[state] = new MenuState(this);
        else if(state == PLAY_STATE)
            gameStates[state] = new PlayState(this);
        else if(state == CONTROLS_STATE)
            gameStates[state] = new ControlsState(this);
    }

    private void unloadState(int state) {
        gameStates[state] = null;
    }

    public void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void update() {
        try {
            gameStates[currentState].update();
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void draw(java.awt.Graphics2D g) {
        try {
            gameStates[currentState].draw(g);
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void keyPressed(int k) {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased(int k) {
        gameStates[currentState].keyReleased(k);
    }
}
