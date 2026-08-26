import java.util.*;

/**
 * Wheels of the slotMachine.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1
 */
public class Wheel
{
    private ArrayList<String> symbols;
    private int currentVisibleIndex;
    private Rectangle body;
    private boolean isVisible;
    
    /**
     * Constructor for objects of class Wheel
     */
    public Wheel()
    {
        this.symbols = new ArrayList<>();
        this.currentVisibleIndex = 0;
        this.isVisible = false;
        this.body = new Rectangle();
        this.body.changeColor("magenta");
        this.body.changeSize(50, 200);
    }
    
    /**
     * Hace visible el cuerpo de la rueda en la pantalla.
     */
    public void makeVisible(){
        body.makeVisible();
        this.isVisible = true;
    }
    
    /**
     * Hace invisible el cuerpo de la rueda en la pantalla.
     */
    public void makeInvisible(){
        body.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * Method that permits to turn the wheel and try again slotMachine
     */
    public void turnAround()
    {
        //wheel functionalities
    }
}