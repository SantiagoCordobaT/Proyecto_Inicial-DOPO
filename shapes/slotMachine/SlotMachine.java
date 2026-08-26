import java.util.*;

/**
 * Main class for the Slot Machine simulator.
 * Manages the wheels, authorized symbols, and the general state of the machine.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1.0
 */
public class SlotMachine
{
    private boolean isOk;
    private boolean isVisible;
    private ArrayList<Wheel> wheels;
    private ArrayList<String> allowedSymbols;
    private Rectangle background;

    /**
     * Constructor for objects of class SlotMachine.
     * Initializes the empty lists and sets up the machine's visual background.
     */
    public SlotMachine()
    {
        this.wheels = new ArrayList<>();
        this.allowedSymbols = new ArrayList<>();
        this.isOk = true;
        this.isVisible = false;
        this.background = new Rectangle();
        this.background.changeColor("black");
        this.background.moveHorizontal(50);
        this.background.moveVertical(50);
        this.background.changeSize(300, 200);
    }

    /**
     * Makes the slot machine and all its active components visible on the screen.
     */
    public void makeVisible(){
        background.makeVisible();
        this.isVisible = true;
    }
    
    /**
     * Hides the slot machine and all its components from the screen.
     */
    public void makeInvisible(){
        background.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * Adds a new wheel to the slot machine at the specified position.
     * It also calculates the proper spatial coordinates to align the wheel inside the machine.
     * 
     * @param pos The position where the wheel will be inserted (starting from 1).
     */
    public void addWheel(int pos){
        if (pos < 1){
            pos = 1;
        }
        
        if (pos > wheels.size() + 1){
            pos = wheels.size() + 1;
        }
        
        Wheel newWheel = new Wheel(); 
        newWheel.moveVertical(50);
        int displacementX = 60 + ((pos - 1) * 60); 
        newWheel.moveHorizontal(displacementX);
        wheels.add(pos - 1, newWheel);
        
        if (this.isVisible) {
            newWheel.makeVisible();
        }
        
        this.isOk = true;
    }
    
    /**
     * Deletes a wheel from the slot machine at the specified position.
     * 
     * @param pos The position of the wheel to be removed (starting from 1).
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
     * Adds a new authorized symbol (color) to the machine's configuration.
     * 
     * @param pos The position where the symbol will be registered (starting from 1).
     * @param color The color of the symbol to be added.
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
     * Deletes a registered symbol by its color name.
     * 
     * @param symbol The exact color name of the symbol to be removed.
     */
    public void delSymbol(String symbol){
        boolean wasDeleted = allowedSymbols.remove(symbol);
        
        if (wasDeleted) {
            this.isOk = true;
        } else {
            this.isOk = false;
        }
    }
    
    /**
     * Places a specific authorized symbol onto a specific wheel.
     * Delegates the actual placement and drawing action to the targeted Wheel object.
     * 
     * @param wheel The position of the target wheel (starting from 1).
     * @param symbol The color of the symbol to place on the wheel.
     */
    public void placeSymbol(int wheel, String symbol){
        if (wheels.isEmpty()){
            this.isOk = false;
            return;
        }
        
        if (wheel < 1){
            wheel = 1;
        }
        if (wheel > wheels.size()){
            wheel = wheels.size();
        }
        
        if (!allowedSymbols.contains(symbol)) {
            this.isOk = false;
            return;
        }
        
        Wheel targetWheel = wheels.get(wheel - 1);
        targetWheel.place(symbol); 
        
        this.isOk = true;
    }
    
    /**
     * Spins a specific wheel indicated by its position.
     * 
     * @param wheel The position of the wheel to spin (starting from 1).
     */
    public void spin(int wheel){
        if (wheels.isEmpty()){
            this.isOk = false;
            return;
        }
        
        if (wheel < 1){
            wheel = 1;
        }
        if (wheel > wheels.size()){
            wheel = wheels.size();
        }
        
        Wheel targetWheel = wheels.get(wheel - 1);
        targetWheel.spin();
        
        this.background.changeColor("black");
        this.isOk = true;
    }
    
    /**
     * Spins all the wheels present in the slot machine simultaneously.
     */
    public void spin(){
        if (wheels.isEmpty()){
            this.isOk = false;
            return;
        }
        
        for (Wheel w : wheels) {
            w.spin();
        }
        
        this.isOk = true;
    }
    
    /**
     * Returns an array with the names of the colors installed in the first wheel.
     * 
     * @return A String array containing the symbols of the first wheel.
     */
    public String[] symbols() {
        if (wheels.isEmpty()) {
            return new String[0]; 
        }
        
        Wheel firstWheel = wheels.get(0);
        ArrayList<String> firstWheelSymbols = firstWheel.getSymbols();
        
        return firstWheelSymbols.toArray(new String[0]);
    }
    
    /**
     * Returns the number of distinct symbols (colors) currently installed 
     * across all wheels in the slot machine.
     * 
     * @return The count of unique symbols.
     */
    public int distinctSymbols() {
        HashSet<String> uniqueSymbols = new HashSet<>();
        
        for (Wheel w : wheels) {
            ArrayList<String> wheelSymbols = w.getSymbols();
            uniqueSymbols.addAll(wheelSymbols);
        }
        
        return uniqueSymbols.size();
    }
    
    /**
     * Returns the current configuration of the machine, which is the array of 
     * symbols currently visible on each wheel from left to right.
     * 
     * @return A String array with the visible symbols.
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];
        
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).getVisibleSymbol();
        }
        
        return config;
    }
    
    /**
     * Checks if the current machine configuration is a winning jackpot.
     * A jackpot occurs when all currently visible symbols on the wheels are identical.
     * It also triggers a visual celebration by changing the machine's background color.
     * 
     * @return true if the configuration is a jackpot, false otherwise.
     */
    public boolean isJackpot() {
        // A machine with no wheels cannot trigger a jackpot
        if (wheels.isEmpty()) {
            this.isOk = false;
            return false;
        }

        String[] currentConfig = configuration();
        String winningSymbol = currentConfig[0];
        
        // If the first wheel is empty (null), there is no jackpot
        if (winningSymbol == null) {
            this.isOk = false;
            return false;
        }

        // Compare all other symbols against the first one
        boolean isWin = true;
        for (int i = 1; i < currentConfig.length; i++) {
            if (currentConfig[i] == null || !currentConfig[i].equals(winningSymbol)) {
                isWin = false;
                break; // Stop checking, we already know it's not a win
            }
        }

        // Visual celebration mechanics
        if (isWin) {
            this.background.changeColor("yellow"); // Jackpot lights!
        } else {
            this.background.changeColor("black");  // Normal state
        }
        
        this.isOk = true;
        return isWin;
    }
    
    /**
     * Safely terminates the slot machine simulation by hiding all visual 
     * elements and rendering the machine inactive.
     */
    public void exit() {
        this.makeInvisible();
        // Opcional: limpiar las listas si se desea un reinicio total de los datos
        // this.wheels.clear();
        // this.allowedSymbols.clear();
        this.isOk = true;
    }
    
    /**
     * Returns the status of the last executed operation.
     * 
     * @return true if the last operation was successful, false otherwise.
     */
    public boolean ok(){
        return isOk;
    }
}