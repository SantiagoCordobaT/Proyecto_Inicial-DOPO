import java.util.*;

/**
 * Main controller for the Slot Machine simulator.
 * This class handles the core logic, managing the wheels, the authorized symbols, and the overall visual state of the game.
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
     * Constructs a new Slot Machine.
     * Initializes the empty collections and sets up the default visual background.
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
     * Displays the slot machine and all its components on the screen.
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
     * Adds a new wheel to the machine at the position (pos).
     * It also calculates the appropriate spatial coordinates to ensure the wheel is perfectly aligned within the machine's casing.
     * 
     * @param pos The position where the wheel should be inserted, starting from 1.
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
     * Removes a wheel from the machine.
     * 
     * @param pos The position of the wheel to remove, from 1.
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
     * Registers a new authorized symbol color into the machine's system.
     * 
     * @param pos The position in the registry where the symbol will be saved, starting from 1.
     * @param color The specific color name of the symbol.
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
     * Deletes a registered symbol from the machine's system using its color name.
     * 
     * @param symbol The exact color name of the symbol to delete.
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
     * Places a specific authorized symbol in the wheel.
     * 
     * @param wheel The position of the target wheel, starting from 1.
     * @param symbol The color of the symbol to place.
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
     * Triggers a spin on a single, specific wheel.
     * 
     * @param wheel The position of the wheel to spin, starting from 1.
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
     * Triggers a simultaneous spin on all the wheels currently installed in the machine.
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
     * Retrieves the colors of all symbols currently configured on the first wheel.
     * 
     * @return An array of strings representing the colors on the first wheel.
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
     * Calculates the total amount of unique symbol colors installed across every wheel.
     * 
     * @return The exact count of unique symbols.
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
     * Gathers the current visual state of the machine by checking which symbol is displayed on each wheel from left to right.
     * 
     * @return An array containing the colors of the currently visible symbols.
     */
    public String[] configuration() {
        String[] config = new String[wheels.size()];
        
        for (int i = 0; i < wheels.size(); i++) {
            config[i] = wheels.get(i).getVisibleSymbol();
        }
        
        return config;
    }
    
    /**
     * Evaluates the current configuration to determine if the player has won the jackpot.
     * A jackpot is awarded only if every visible symbol across all wheels matches perfectly.
     * If won, the machine will celebrate by illuminating its background.
     * 
     * @return True if the current configuration is a jackpot, false otherwise.
     */
    public boolean isJackpot() {
        if (wheels.isEmpty()) {
            this.isOk = false;
            return false;
        }
        String[] currentConfig = configuration();
        String winningSymbol = currentConfig[0];
        if (winningSymbol == null) {
            this.isOk = false;
            return false;
        }
        boolean isWin = true;
        for (int i = 1; i < currentConfig.length; i++) {
            if (currentConfig[i] == null || !currentConfig[i].equals(winningSymbol)) {
                isWin = false;
                break; 
            }
        }
        if (isWin) {
            this.background.changeColor("yellow"); 
        } else {
            this.background.changeColor("black");  
        }
        if (this.isVisible) {
            for (Wheel w : wheels) {
                w.makeInvisible();
                w.makeVisible();
            }
        }
        this.isOk = true;
        return isWin;
    }
    
    /**
     * Safely closes the simulation by hiding all components and clearing the screen.
     */
    public void exit() {
        this.makeInvisible();
        this.isOk = true;
    }
    
    /**
     * Checks the success status of the most recently executed method.
     * 
     * @return True if the last operation completed successfully, false if it failed.
     */
    public boolean ok(){
        return isOk;
    }
}