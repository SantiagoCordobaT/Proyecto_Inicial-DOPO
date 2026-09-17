import java.util.ArrayList;

/**
 * Represents a slot machine wheel.
 * 
 * @author Santiago Cordoba - Camilo Rivas
 * @version 2.0
 */
public class Wheel {
    private ArrayList<String> symbols;     
    private int currentVisibleIndex;       
    private boolean isVisible;             
    private boolean isLocked;              

    private Rectangle outerBezel;          
    private Rectangle innerReel;           
    private Circle symbolShape;           

    /**
     * Creates a new wheel at default position (0, 0).
     */
    public Wheel() {
        this.symbols = new ArrayList<>();
        this.currentVisibleIndex = 0;
        this.isVisible = false;
        this.isLocked = false;

        this.outerBezel = new Rectangle();
        this.outerBezel.changeColor("white");
        this.outerBezel.changeSize(90, 50); 
        this.outerBezel.moveHorizontal(-60);
        this.outerBezel.moveVertical(-50); 

        this.innerReel = new Rectangle();
        this.innerReel.changeColor("black");
        this.innerReel.changeSize(80, 40);
        this.innerReel.moveHorizontal(-60 + 5); 
        this.innerReel.moveVertical(-50 + 5);   

        this.symbolShape = new Circle();
        this.symbolShape.changeSize(30);
        this.symbolShape.moveHorizontal(-20 + 10); 
        this.symbolShape.moveVertical(-60 + 30);   
    }

    /**
     * Makes the wheel and its components visible on screen.
     */
    public void makeVisible() {
        this.outerBezel.makeVisible();
        this.innerReel.makeVisible();
        if (!this.symbols.isEmpty()) {
            this.symbolShape.makeVisible();
        }
        this.isVisible = true;
    }

    /**
     * Hides the wheel from the screen.
     */
    public void makeInvisible() {
        this.outerBezel.makeInvisible();
        this.innerReel.makeInvisible();
        this.symbolShape.makeInvisible();
        this.isVisible = false;
    }

    /**
     * Moves the wheel horizontally by a given distance.
     */
    public void moveHorizontal(int distance) {
        this.outerBezel.moveHorizontal(distance);
        this.innerReel.moveHorizontal(distance);
        this.symbolShape.moveHorizontal(distance);
    }

    /**
     * Moves the wheel vertically by a given distance.
     */
    public void moveVertical(int distance) {
        this.outerBezel.moveVertical(distance);
        this.innerReel.moveVertical(distance);
        this.symbolShape.moveVertical(distance);
    }

    /**
     * Adds a symbol to the wheel and shows it if it is the first one.
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
     * Locks the wheel to prevent spinning and turns the border red.
     */
    public void lock() {
        this.isLocked = true;
        this.outerBezel.changeColor("red");
        fixZOrder();
    }

    /**
     * Unlocks the wheel and restores its normal border color.
     */
    public void unlock() {
        this.isLocked = false;
        this.outerBezel.changeColor("white");
        fixZOrder();
    }

    /**
     * Returns true if the wheel is locked.
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Spins the wheel one step forward.
     */
    public void spin() {
        spinOneStep(1);
    }

    /**
     * Spins the wheel one step backward.
     */
    public void spinBackwards() {
        spinOneStep(-1);
    }

    /**
     * Advances the visible index by delta and updates the display.
     */
    private void spinOneStep(int delta) {
        if (this.isLocked || this.symbols.size() <= 1) {
            return;
        }
        int total = this.symbols.size();
        this.currentVisibleIndex = ((this.currentVisibleIndex + delta) % total + total) % total;
        String newColor = this.symbols.get(this.currentVisibleIndex);
        this.symbolShape.changeColor(newColor);
    }

    /**
     * Forces the wheel to show a specific symbol if present.
     */
    public boolean setVisibleSymbol(String color) {
        int idx = this.symbols.indexOf(color);
        if (idx != -1) {
            this.currentVisibleIndex = idx;
            this.symbolShape.changeColor(color);
            return true;
        }
        return false;
    }

    /**
     * Returns all symbols contained in this wheel.
     */
    public ArrayList<String> getSymbols() {
        return this.symbols;
    }

    /**
     * Returns the currently visible symbol, or null if empty.
     */
    public String getVisibleSymbol() {
        if (this.symbols.isEmpty()) {
            return null;
        }
        return this.symbols.get(this.currentVisibleIndex);
    }

    /**
     * Restores the proper shape layering on canvas.
     */
    private void fixZOrder() {
        if (this.isVisible) {
            this.innerReel.makeInvisible();
            this.innerReel.makeVisible();
            if (!this.symbols.isEmpty()) {
                this.symbolShape.makeInvisible();
                this.symbolShape.makeVisible();
            }
        }
    }
}