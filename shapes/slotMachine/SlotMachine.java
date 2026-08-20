import java.util.*;

/**
 * Clase principal de la maquina tragamonedas.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1
 */
public class SlotMachine
{
    private boolean isOk;
    private boolean isVisible;
    private ArrayList<Wheel> wheels; //machine's wheels (composition)
    private ArrayList<String> allowedSymbols;
    private Rectangle background;

    /**
     * Constructor for objects of class SlotMachine.
     */
    public SlotMachine()
    {
        this.wheels = new ArrayList<>();
        this.allowedSymbols = new ArrayList<>();
        this.isOk = true;
        this.isVisible = false;
        this.background = new Rectangle();
        background.changeColor("black");
        background.moveHorizontal(50);
        background.moveVertical(50);
        background.changeSize(300, 200);
    }

    /**
     * Make's the background machine visible in the computer.
     */
    public void makeVisible(){
        background.makeVisible();
        this.isVisible = true;
    }
    
    /**
     * Make's the background machine invisible in the computer.
     */
    public void makeInvisible(){
        background.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * @return true if the last operation was successful, false if not.
     */
    public boolean ok(){
        return isOk;
    }
}