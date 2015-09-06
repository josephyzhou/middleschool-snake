import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Window extends Frame implements KeyListener{
    public Window(String wTitle, int wWidth, int wHeight, boolean resizeable, Snake parent){
        this.parent = parent;
        setTitle(wTitle);
        addWindowListener(new WindowCloseListener());
        addKeyListener(this);
        setResizable(resizeable);
        setVisible(true);
        toFront();
        setSize(wWidth, wHeight);
        gfx = getGraphics();
    }
    
    private Graphics gfx;
    private Snake parent;
    
    public void updateGraphics(Image img){
        try{
            gfx.drawImage(img, 0, 0, Color.BLACK, this);
        }catch(Exception e){ 
            e.printStackTrace();
        }
    }
    
    public void  keyPressed(KeyEvent e){
        parent.keyTyped(e.getKeyCode());
//        System.out.println("KeyPressed");
    }
    public void keyReleased(KeyEvent e){
//        System.out.println("KeyReleased");
    }
    public void  keyTyped(KeyEvent e){
//        System.out.println("KeyTyped");
    }
}

class WindowCloseListener extends WindowAdapter{
   public void windowClosing(WindowEvent event){
      System.exit(0);
   }
}