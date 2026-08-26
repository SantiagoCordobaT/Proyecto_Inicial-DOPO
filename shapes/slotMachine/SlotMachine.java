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
     * Add wheels with their background in slotMachine.
     */
    public void addWheel(int pos){
        if (pos < 1){
            pos = 1;
        }
        
        if (pos > wheels.size() + 1){
            pos = wheels.size() + 1;
        }
        Wheel nuevaRueda = new Wheel();        
        wheels.add(pos - 1, nuevaRueda);
        if (this.isVisible) {
            nuevaRueda.makeVisible();
        }
        this.isOk = true;
    }
    
    /**
     * Deletes a wheel with a background who is attach. 
     */
    public void delWheel(int pos){
        if (wheels.isEmpty()){
            this.isOk = false;
            return;
        }
        
        if (pos < 1){
            pos = 1;
        }
        
        if (pos > wheels.size()){
            pos = wheels.size();
        }
        
        wheels.remove(pos - 1);
        
        this.isOk = true;
    }
    
    /**
     * Adds a symbol (color) to the list with the position.
     */
    public void addSymbol(int pos, String color){
        if (pos < 1){
            pos = 1;
        }
        
        if (pos > allowedSymbols.size() + 1){
            pos = allowedSymbols.size() + 1;
        }
        allowedSymbols.add(pos - 1, color);
        this.isOk = true;
    }
    
    /**
     * Deletes a symbol by the color in the list 
     */
    public void delSymbol(String symbol){
        boolean pudoBorrar = allowedSymbols.remove(symbol);
        if (pudoBorrar) {
            this.isOk = true;
        } else {
            this.isOk = false;
        }
    }
    
    /**
     * @return true if the last operation was successful, false if not.
     */
    public boolean ok(){
        return isOk;
    }
}