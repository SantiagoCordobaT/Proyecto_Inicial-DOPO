import java.util.*;

/**
 * Wheels of the slotMachine.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1
 */
public class Wheel
{
    // instance variables - replace the example below with your own
    private ArrayList<String> colors;
    private boolean isVisible;
    private Circle background;
    private int currentColor;
    private ArrayList<String> symbols;
    private int currentVisibleIndex;
    private Rectangle body;
    private boolean isVisible;
>>>>>>> Stashed changes
    
    /**
     * Constructor for Wheel
     */
    public Wheel()
    {
        // initialise instance variables
        colors = new ArrayList<>();
        isVisible = false;
        background = new Circle();
        currentColor = 0;
        
        
=======
        this.symbols = new ArrayList<>();
        this.currentVisibleIndex = 0;
        isVisible = false;
        this.body = new Rectangle();
        body.changeColor("magenta");
        body.changeSize(200, 50);
    }
    
    /**
     * Make's invisible the body (Three rectangles what represents the background of each wheel).
     */
    public void makeInvisible(){
        body.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * Make's visible the body (Three rectangles what represents the background of each wheel).
     */
    public void makeVisible(){
        body.makeVisible();
        this.isVisible = true;
>>>>>>> Stashed changes
    }
    
    /**
     * Method that permits to turn the wheel and try again slotMachine
     */
    public void turnAround()
    {
        //Suma cada posicion para que esta gire mediante colores, donde comienza en la posicion
        // 0
        currentColor = currentColor + 1;
    }
    
    /**
     * Method for make visible the object
     */
    public void makeVisible(){
        //Cambia el estado del objeto de visible a true 
        isVisible = true;
    }
    
    /**
     * Method for make invible the object
     */
    public void makeInvisible(){
        //Cambia el estado del objeto de visible a false
        isVisible = false;
    }
}