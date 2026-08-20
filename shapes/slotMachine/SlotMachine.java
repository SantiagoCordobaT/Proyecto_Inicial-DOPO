import java.util.ArrayList;

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
     * Constructor for objects of class SlotMachine
     */
    public SlotMachine()
    {
        // initialise instance variable
        
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int sampleMethod(int y)
    {
        // put your code here
        return y;
    }
}