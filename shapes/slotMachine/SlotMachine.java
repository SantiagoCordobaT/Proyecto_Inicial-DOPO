import java.util.*;
import javax.swing.JOptionPane;

/**
 * Main controler (Slot Machine).
 * 
 * @author Santiago Cordoba - Camilo Rivas
 * @version 3.0
 */
public class SlotMachine {
    private boolean isOk;
    private boolean isVisible;
    private boolean isJackpotActive;
    private ArrayList<Wheel> wheels;
    private ArrayList<String> allowedSymbols;

    private Rectangle rectangExterior;
    private Rectangle indicadorEstado;
    private Rectangle adornoVictoria;
    private Rectangle rectangInterior;

    private final int ESPACIO_RUEDAS = 65; 
    private final int INICIAL_X = 55;        
    private final int INICIAL_Y = 80;        

    /**
     * Starts the machine.
     */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.allowedSymbols = new ArrayList<>();
        this.isOk = true;
        this.isVisible = false;
        this.isJackpotActive = false;

        this.rectangExterior = new Rectangle();
        this.rectangExterior.changeColor("blue");
        this.rectangExterior.changeSize(220, 260); 
        this.rectangExterior.moveHorizontal(-60 + 20);
        this.rectangExterior.moveVertical(-50 + 20); 

        this.indicadorEstado = new Rectangle();
        this.indicadorEstado.changeColor("red");
        this.indicadorEstado.changeSize(15, 220); 
        this.indicadorEstado.moveHorizontal(-60 + 40);
        this.indicadorEstado.moveVertical(-50 + 30);

        this.adornoVictoria = new Rectangle();
        this.adornoVictoria.changeColor("yellow");
        this.adornoVictoria.changeSize(130, 240);
        this.adornoVictoria.moveHorizontal(-60 + 30);
        this.adornoVictoria.moveVertical(-50 + 60);

        this.rectangInterior = new Rectangle();
        this.rectangInterior.changeColor("green");
        this.rectangInterior.changeSize(110, 220);
        this.rectangInterior.moveHorizontal(-60 + 40);
        this.rectangInterior.moveVertical(-50 + 70);
    }
    
    /**
     *  Start the machine with n symbols and wheels.
     */
    public SlotMachine(int n){
        this();
        if (n <= 0) n = 7;
        this.allowedSymbols.clear();
        this.wheels.clear();
        String[] colores = {"red","black","blue","yellow","green","magenta","white"};
        for (int i = 0; i < n; i++) {
            if (i<colores.length) {
                this.allowedSymbols.add(colores[i]);
            } else {
                this.allowedSymbols.add("color-" + (i + 1));
            }
        }
        
        Random aleatorios = new Random();
        for (int i = 0; i < n; i++) {
            this.addWheel();
            for (String simbolo : this.allowedSymbols) {
                this.placeSymbol(i + 1, simbolo);
            }
            int giros = aleatorios.nextInt(n);
            this.spin(i+1, giros);
        }
    }

    /**
     * Make visible the machine in the canvas.
     */
    public void makeVisible() {
        this.rectangExterior.makeVisible();
        this.indicadorEstado.makeVisible();
        this.adornoVictoria.makeVisible();
        this.rectangInterior.makeVisible();
        for (Wheel w : this.wheels) {
            w.makeVisible();
        }
        this.isVisible = true;
        this.isOk = true;
    }

    /**
     * Hide the machine in the canvas.
     */
    public void makeInvisible() {
        this.rectangExterior.makeInvisible();
        this.indicadorEstado.makeInvisible();
        this.adornoVictoria.makeInvisible();
        this.rectangInterior.makeInvisible();
        for (Wheel w : this.wheels) {
            w.makeInvisible();
        }
        this.isVisible = false;
        this.isOk = true;
    }

    /**
     * Adds a new wheel next to the last wheel.
     */
    public boolean addWheel() {
        return addWheel(this.wheels.size() + 1);
    }

    /**
     * Adds a new wheel at specific position.
     */
    public boolean addWheel(int pos) {
        int targetPos = pos;
        if (targetPos < 1) targetPos = 1;
        if (targetPos > this.wheels.size() + 1) targetPos = this.wheels.size() + 1;

        Wheel newWheel = new Wheel();
        
        int displacementX = INICIAL_X + ((targetPos - 1) * ESPACIO_RUEDAS);
        newWheel.moveHorizontal(displacementX);
        newWheel.moveVertical(INICIAL_Y);

        for (int i = targetPos - 1; i < this.wheels.size(); i++) {
            this.wheels.get(i).moveHorizontal(ESPACIO_RUEDAS);
        }
        
        this.wheels.add(targetPos - 1, newWheel);   
        if (this.isVisible) {
            newWheel.makeVisible();
        }
        this.isOk = true;
        return true;
    }

    /**
     * Delete a wheel in a specific position.
     */
    public void delWheel(int pos) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas instaladas para eliminar.");
            return;
        }

        int targetPos = clipPosition(pos, this.wheels.size());
        Wheel removed = this.wheels.remove(targetPos - 1);
        removed.makeInvisible();

        for (int i = targetPos - 1; i < this.wheels.size(); i++) {
            this.wheels.get(i).moveHorizontal(-ESPACIO_RUEDAS);
        }
        this.isOk = true;
    }

    /**
     * Interchange one wheel with other wheel (by position of those wheels).
     */
    public void swap(int wheel1, int wheel2) {
        if (this.wheels.size() < 2) {
            notifyError("Se necesitan al menos dos ruedas para intercambiar.");
            return;
        }

        int w1 = clipPosition(wheel1, this.wheels.size());
        int w2 = clipPosition(wheel2, this.wheels.size());

        if (w1 == w2) {
            notifyError("No se puede intercambiar una rueda consigo misma.");
            return;
        }

        int idx1 = w1 - 1;
        int idx2 = w2 - 1;

        Wheel obj1 = this.wheels.get(idx1);
        Wheel obj2 = this.wheels.get(idx2);

        int pixelDistance = (idx2 - idx1) * ESPACIO_RUEDAS;
        obj1.moveHorizontal(pixelDistance);
        obj2.moveHorizontal(-pixelDistance);

        Collections.swap(this.wheels, idx1, idx2);
        this.isOk = true;
    }

    /**
     * locks a wheel (user cant spin it)
     */
    public void lock(int wheel) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para fijar.");
            return;
        }
        int w = clipPosition(wheel, this.wheels.size());
        this.wheels.get(w - 1).lock();
        this.isOk = true;
    }

    /**
     * unlock a wheel (a locked wheel turn to unlock, so the user can spin it).
     */
    public void unlock(int wheel) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para soltar.");
            return;
        }
        int w = clipPosition(wheel, this.wheels.size());
        this.wheels.get(w - 1).unlock();
        this.isOk = true;
    }

    /**
     * Register a symbol at a specific position with a specific color.
     */
    public void addSymbol(int pos, String color) {
        if (color == null || color.trim().isEmpty()) {
            notifyError("El nombre del simbolo no puede ser vacio.");
            return;
        }
        if (this.allowedSymbols.contains(color)) {
            notifyError("El simbolo '" + color + "' ya existe.");
            return;
        }

        int targetPos = clipPosition(pos, this.allowedSymbols.size() + 1);
        this.allowedSymbols.add(targetPos - 1, color);
        this.isOk = true;
    }

    /**
     * Delets a symbol that is in the allowed symbols list.
     */
    public void delSymbol(String symbol) {
        boolean wasRemoved = this.allowedSymbols.remove(symbol);
        if (wasRemoved) {
            this.isOk = true;
        } else {
            notifyError("El simbolo '" + symbol + "' no esta registrado.");
        }
    }

    /**
     * Assigns a allowed symbol to the list (it can be visual in the canvas).
     */
    public void placeSymbol(int wheel, String symbol) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas instaladas.");
            return;
        }
        if (!this.allowedSymbols.contains(symbol)) {
            notifyError("El simbolo no esta autorizado.");
            return;
        }

        int targetWheel = clipPosition(wheel, this.wheels.size());
        this.wheels.get(targetWheel - 1).place(symbol);
        this.isOk = true;
    }

    /**
     * Spins a wheel with the same method in the class.
     */
    public void spin(int wheel) {
        spin(wheel, 1);
    }

    /**
     * Spins a wheel (change the color(symbol) at specific position and with specific steps(count of changes))
     */
    public void spin(int wheel, int steps) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para girar.");
            return;
        }

        int w = clipPosition(wheel, this.wheels.size());
        Wheel target = this.wheels.get(w - 1);

        if (target.isLocked()) {
            notifyError("La rueda " + w + " esta bloqueada.");
            return;
        }

        if (target.getSymbols().isEmpty()) {
            notifyError("La rueda no posee simbolos.");
            return;
        }

        int totalSteps = Math.abs(steps);
        boolean forward = steps >= 0;

        for (int i = 0; i < totalSteps; i++) {
            if (forward) {
                target.spin();
            } else {
                target.spinBackwards();
            }
        }

        resetJackpotVisuals();
        this.isOk = true;
    }

    /**
     * Assigns a specific configuration to the machine (specific symbols)
     */
    public void spin(String[] setSymbols) {
        if (setSymbols == null || setSymbols.length != this.wheels.size()) {
            notifyError("La configuracion no coincide con las ruedas.");
            return;
        }

        for (int i = 0; i < setSymbols.length; i++) {
            String sym = setSymbols[i];
            if (!this.allowedSymbols.contains(sym) || !this.wheels.get(i).getSymbols().contains(sym)) {
                notifyError("El simbolo '" + sym + "' no es valido.");
                return;
            }
        }

        for (int i = 0; i < setSymbols.length; i++) {
            this.wheels.get(i).setVisibleSymbol(setSymbols[i]);
        }

        resetJackpotVisuals();
        this.isOk = true;
    }

    /**
     * spins one step all the wheels that are unlock.
     */
    public void spin() {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para girar.");
            return;
        }

        for (Wheel w : this.wheels) {
            if (!w.isLocked()) w.spin();
        }

        resetJackpotVisuals();
        this.isOk = true;
    }

    /**
     * Returns the symbols in the first wheels (unique symbols).
     */
    public String[] symbols() {
        if (this.wheels.isEmpty()) return new String[0];
        return this.wheels.get(0).getSymbols().toArray(new String[0]);
    }

    /**
     * Returns the numbers of unique symbols in the machine (all wheels).
     */
    public int distinctSymbols() {
        HashSet<String> distinct = new HashSet<>();
        for (Wheel w : this.wheels) {
            String visible = w.getVisibleSymbol();
            if (visible != null) {
                distinct.add(visible);
            }
        }
        return distinct.size();
    }

    /**
     * Returns the visible configuration of symbols.
     */
    public String[] configuration() {
        String[] config = new String[this.wheels.size()];
        for (int i = 0; i < this.wheels.size(); i++) {
            config[i] = this.wheels.get(i).getVisibleSymbol();
        }
        return config;
    }

    /**
     * Verify if all of the wheels have the same symbols (WIN CONDITION)
     */
    public boolean isJackpot() {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para evaluar.");
            return false;
        }

        int[] numericConfig = getNumericConfiguration();
        int targetId = numericConfig[0];

        if (targetId == -1) {
            notifyError("Ruedas sin simbolos definidos.");
            return false;
        }

        int matchCount = 0;
        for (int id : numericConfig) {
            if (id == targetId) matchCount++;
        }

        boolean isWin = (matchCount == this.wheels.size());

        if (isWin) {
            this.indicadorEstado.changeColor("yellow");
            this.adornoVictoria.changeColor("green");
            this.isJackpotActive = true;
            fixZOrder();
        } else {
            resetJackpotVisuals();
        }

        this.isOk = true;
        return isWin;
    }

    /**
     * Converts the visible symbols to a numeric index.
     */
    public int[] getNumericConfiguration() {
        int[] numConfig = new int[this.wheels.size()];
        Map<String, Integer> symbolDirectory = new HashMap<>();

        for (int i = 0; i < this.allowedSymbols.size(); i++) {
            symbolDirectory.put(this.allowedSymbols.get(i), i);
        }

        for (int i = 0; i < this.wheels.size(); i++) {
            String sym = this.wheels.get(i).getVisibleSymbol();
            numConfig[i] = (sym != null && symbolDirectory.containsKey(sym)) ? symbolDirectory.get(sym) : -1;
        }

        return numConfig;
    }

    /**
     * Hide the simulator and end the operation.
     */
    public void exit() {
        makeInvisible();
        this.isOk = true;
    }

    /**
     * Returns true if the operation was succesfully.
     */
    public boolean ok() {
        return this.isOk;
    }

    /**
     * Verify that a position is valid.
     */
    private int clipPosition(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    /**
     * Restore the normal symbols and implements fixZOrder.
     */
    private void resetJackpotVisuals() {
        if (this.isJackpotActive) {
            this.indicadorEstado.changeColor("red");
            this.adornoVictoria.changeColor("yellow");
            this.isJackpotActive = false;
            fixZOrder();
        }
    }

    /**
     * Restore the order of the caps.
     */
    private void fixZOrder() {
        if (this.isVisible) {
            this.rectangInterior.makeInvisible();
            this.rectangInterior.makeVisible();
            for (Wheel w : this.wheels) {
                w.makeInvisible();
                w.makeVisible();
            }
        }
    }

    /**
     * Is something went wrong make isOk false and show a message with JOptionPane.
     */
    private void notifyError(String message) {
        this.isOk = false;
        if (this.isVisible) {
            JOptionPane.showMessageDialog(null, message, "SlotMachine - Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}