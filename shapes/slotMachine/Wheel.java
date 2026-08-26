import java.util.*;

/**
 * Represents a single spinning wheel within the slot machine simulator.
 * It manages its own collection of symbols, tracks which one is currently visible, 
 * and handles its own graphical representation on the screen.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1.0
 */
public class Wheel
{
    private ArrayList<String> symbols;
    private int currentVisibleIndex;
    private Rectangle body;
    private Circle symbolShape;
    private boolean isVisible;
    
    /**
     * Creates a new, empty wheel. 
     */
    public Wheel()
    {
        this.symbols = new ArrayList<>();
        this.currentVisibleIndex = 0;
        this.isVisible = false;
        
        this.body = new Rectangle();
        this.body.changeColor("magenta");
        this.body.changeSize(200, 50);
        
        this.symbolShape = new Circle();
        this.symbolShape.changeSize(30);
        this.symbolShape.moveHorizontal(50);
        this.symbolShape.moveVertical(75);
    }
    
    /**
     * Displays the wheel and active symbol on the screen.
     */
    public void makeVisible(){
        this.body.makeVisible();
        
        if (!symbols.isEmpty()) {
            this.symbolShape.makeVisible();
        }
        
        this.isVisible = true;
    }
    
    /**
     * Hides the wheel and active symbol from the screen.
     */
    public void makeInvisible(){
        this.body.makeInvisible();
        this.symbolShape.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * Moves both the wheel's casing and its symbol horizontally across the screen.
     * 
     * @param distance The exact number of pixels to move the wheel horizontally.
     */
    public void moveHorizontal(int distance) {
        this.body.moveHorizontal(distance);
        this.symbolShape.moveHorizontal(distance);
    }
    
    /**
     * Moves both the wheel's casing and its symbol vertically across the screen.
     * 
     * @param distance The exact number of pixels to move the wheel vertically.
     */
    public void moveVertical(int distance) {
        this.body.moveVertical(distance);
        this.symbolShape.moveVertical(distance);
    }
    
    /**
     * Adds a new symbol color to the wheel's sequence. 
     * If it is the first symbol being added, it automatically displays it on the wheel.
     * 
     * @param color The exact name of the color to register as a symbol.
     */
    public void place(String color) {
        this.symbols.add(color);
    
        if (this.symbols.size() == 1) {
            this.symbolShape.changeColor(color);
            if (this.isVisible) {
                this.symbolShape.makeVisible();
            }
        }
    }
    
    /**
     * Spins the wheel to reveal the next symbol in the sequence.
     * If the wheel reaches the end of its symbol list, it seamlessly loops back to the beginning.
     */
    public void spin() {
        if (symbols.size() > 1) {
            currentVisibleIndex = currentVisibleIndex + 1;
    
            if (currentVisibleIndex >= symbols.size()) {
                currentVisibleIndex = 0;
            }
            
            String newColor = symbols.get(currentVisibleIndex);
            symbolShape.changeColor(newColor);
        }
    }
    
    /**
     * Retrieves the complete sequence of symbols configured on this specific wheel.
     * 
     * @return An ArrayList containing the color strings of the symbols.
     */
    public ArrayList<String> getSymbols() {
        return this.symbols;
    }
    
    /**
     * Identifies which symbol is currently being displayed on the wheel.
     * 
     * @return The color string of the visible symbol, or null if the wheel is empty.
     */
    public String getVisibleSymbol() {
        if (symbols.isEmpty()) {
            return null;
        }
        return symbols.get(currentVisibleIndex);
    }
}