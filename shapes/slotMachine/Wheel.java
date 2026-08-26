import java.util.*;

/**
 * Wheels of the slot machine simulator.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1.1
 */
public class Wheel
{
    private ArrayList<String> symbols;
    private int currentVisibleIndex;
    private Rectangle body;
    private Circle symbolShape;
    private boolean isVisible;
    
    /**
     * Constructor for objects of class Wheel.
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
     * Makes the wheel casing and its current symbol visible.
     */
    public void makeVisible(){
        this.body.makeVisible();
        
        if (!symbols.isEmpty()) {
            this.symbolShape.makeVisible();
        }
        
        this.isVisible = true;
    }
    
    /**
     * Hides the wheel and its symbol from the screen.
     */
    public void makeInvisible(){
        this.body.makeInvisible();
        this.symbolShape.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * Mueve visualmente la rueda y su símbolo horizontalmente al mismo tiempo.
     */
    public void moveHorizontal(int distance) {
        this.body.moveHorizontal(distance);
        this.symbolShape.moveHorizontal(distance);
    }
    
    /**
     * Mueve visualmente la rueda y su símbolo verticalmente al mismo tiempo.
     */
    public void moveVertical(int distance) {
        this.body.moveVertical(distance);
        this.symbolShape.moveVertical(distance);
    }
    
    /**
     * Places a new symbol on the wheel and prepares it for visual representation.
     * 
     * @param color The color of the symbol being placed.
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
     * Gira la rueda para mostrar el siguiente simbolo.
     * Si llega al final de la lista, vuelve a empezar desde el principio.
     */
    public void spin() {
        if (symbols.size() > 1) {
            currentVisibleIndex = currentVisibleIndex + 1;
    
            if (currentVisibleIndex >= symbols.size()) {
                currentVisibleIndex = 0;
            }
            
            String nuevoColor = symbols.get(currentVisibleIndex);
            symbolShape.changeColor(nuevoColor);
        }
    }
    
    /**
     * Returns the list of symbols currently configured in this wheel.
     * @return An ArrayList containing the color strings.
     */
    public ArrayList<String> getSymbols() {
        return this.symbols;
    }
    
    /**
     * Returns the currently visible symbol on this wheel.
     * @return The color string of the visible symbol, or null if empty.
     */
    public String getVisibleSymbol() {
        if (symbols.isEmpty()) {
            return null;
        }
        return symbols.get(currentVisibleIndex);
    }
}