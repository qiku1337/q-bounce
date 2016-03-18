package main;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import static main.Game.CONFIG_FILENAME;
/**
 *
 * @author Qiku
 */
public class App {
    static public final String WINDOW_TITLE = "Q-Bounce";
    static public final int WINDOW_WIDTH = 800;
    static public final int WINDOW_HEIGHT = 600;
     
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.vSyncEnabled = false;   // disable vertical sync with the monitor
        cfg.resizable = false;      // disable client space resizing
        cfg.forceExit = false;      // disable exit force
        cfg.title = WINDOW_TITLE;   // window title on titlebar
        cfg.width = WINDOW_WIDTH;   // canvas width in pixels
        cfg.height = WINDOW_HEIGHT; // canvas height in pixels
        cfg.x = -1;                 // place window at the center of the screen
        cfg.y = -1;
         
         // create and run application listeners
        LwjglApplication app = new LwjglApplication(Game.app, cfg);
    }
}
