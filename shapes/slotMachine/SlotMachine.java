import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Main controller (Slot Machine). It works only with {@link Wheel} and
 * {@link Symbol}; the concrete types are created by {@link WheelFactory} and
 * {@link SymbolFactory}, so new types do not require changes here.
 * <p>
 * Every operation sets {@link #ok()}: true if it was carried out, false if it
 * was rejected (a message is shown when the machine is visible).
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class SlotMachine {
    private static final int WHEEL_SPACE = 65;
    private static final int INIT_X = 55;
    private static final int INIT_Y = 80;
    private static final int STAT_HEIGHT = 350;
    private static final String[] BASE_COLORS =
        {"red", "black", "blue", "yellow", "green", "magenta", "white"};

    private boolean isOk;
    private boolean isVisible;
    private boolean isJackpotActive;
    private final ArrayList<Wheel> wheels;
    private final ArrayList<String> allowedSymbols;

    private final Rectangle rectangExterior;
    private final Rectangle indicadorEstado;
    private final Rectangle adornoVictoria;
    private final Rectangle rectangInterior;

    /**
     * Starts an empty machine (no wheels, no symbols).
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
     * Starts a machine with n wheels and n symbols, initialized randomly.
     * If n is not positive the machine stays empty and {@link #ok()} is false.
     *
     * @param n number of wheels and of symbols
     */
    public SlotMachine(int n) {
        this();
        if (n <= 0) {
            notifyError("La maquina necesita al menos una rueda.");
            return;
        }
        for (int i = 0; i < n; i++) {
            this.allowedSymbols.add(i < BASE_COLORS.length ? BASE_COLORS[i] : "color-" + (i + 1));
        }
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            addWheel(i + 1);
            for (String symbol : this.allowedSymbols) {
                placeSymbol(i + 1, symbol);
            }
            spin(i + 1, random.nextInt(n));
        }
        this.isOk = true;
    }

    /**
     * Makes the machine visible in the canvas.
     */
    public void makeVisible() {
        this.isVisible = true;
        dinamicCanvas();
        this.rectangExterior.makeVisible();
        this.indicadorEstado.makeVisible();
        this.adornoVictoria.makeVisible();
        this.rectangInterior.makeVisible();
        for (Wheel w : this.wheels) {
            w.makeVisible();
        }
        this.isOk = true;
    }

    /**
     * Hides the machine in the canvas.
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
     * Adds a normal wheel next to the last wheel.
     */
    public void addWheel() {
        addWheel("normal", this.wheels.size() + 1);
    }

    /**
     * Adds a normal wheel at a specific position (1 = leftmost).
     *
     * @param pos position of the new wheel (it is clipped to a valid one)
     */
    public void addWheel(int pos) {
        addWheel("normal", pos);
    }

    /**
     * Adds a wheel of the given type at a specific position.
     *
     * @param type type of the wheel ("normal", "lefty", "rebel" or any registered type)
     * @param pos  position of the new wheel (it is clipped to a valid one)
     */
    public void addWheel(String type, int pos) {
        Wheel newWheel;
        try {
            newWheel = WheelFactory.create(type);
        } catch (IllegalArgumentException e) {
            notifyError(e.getMessage());
            return;
        }
        int targetPos = clipPosition(pos, this.wheels.size() + 1);

        newWheel.moveHorizontal(INIT_X + ((targetPos - 1) * WHEEL_SPACE));
        newWheel.moveVertical(INIT_Y);
        for (int i = targetPos - 1; i < this.wheels.size(); i++) {
            this.wheels.get(i).moveHorizontal(WHEEL_SPACE);
        }

        this.wheels.add(targetPos - 1, newWheel);
        if (this.isVisible) {
            newWheel.makeVisible();
        }
        dinamicCanvas();
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Deletes the wheel in a specific position. A wheel that cannot be deleted
     * (rebel) is kept and the operation fails.
     *
     * @param pos position of the wheel
     */
    public void delWheel(int pos) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas instaladas para eliminar.");
            return;
        }
        int targetPos = clipPosition(pos, this.wheels.size());
        Wheel targetWheel = this.wheels.get(targetPos - 1);
        if (!targetWheel.canBeDeleted()) {
            notifyError("No se puede eliminar una rueda de tipo " + targetWheel.getType() + ".");
            return;
        }

        this.wheels.remove(targetPos - 1);
        targetWheel.makeInvisible();
        for (int i = targetPos - 1; i < this.wheels.size(); i++) {
            this.wheels.get(i).moveHorizontal(-WHEEL_SPACE);
        }
        dinamicCanvas();
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Interchanges two wheels. If one of them cannot be swapped (rebel) nothing changes.
     *
     * @param wheel1 position of the first wheel
     * @param wheel2 position of the second wheel
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
        if (!obj1.canSwap() || !obj2.canSwap()) {
            notifyError("Una de las ruedas no se deja intercambiar.");
            return;
        }

        int pixelDistance = (idx2 - idx1) * WHEEL_SPACE;
        obj1.moveHorizontal(pixelDistance);
        obj2.moveHorizontal(-pixelDistance);
        Collections.swap(this.wheels, idx1, idx2);
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Locks a wheel (it cannot be spun). A wheel that refuses to be locked
     * (rebel) stays as it is and the operation fails.
     *
     * @param wheel position of the wheel
     * @return the number of locked wheels after the operation, or -1 if it failed
     */
    public int lock(int wheel) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para fijar.");
            return -1;
        }
        int w = clipPosition(wheel, this.wheels.size());
        if (!this.wheels.get(w - 1).lock()) {
            notifyError("La rueda " + w + " no se deja bloquear.");
            return -1;
        }
        this.isOk = true;
        int locked = 0;
        for (Wheel each : this.wheels) {
            if (each.isLocked()) {
                locked++;
            }
        }
        return locked;
    }

    /**
     * Unlocks a wheel, so it can be spun again.
     *
     * @param wheel position of the wheel
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
     * Adds a normal symbol to a wheel (and registers its color).
     *
     * @param pos   position of the wheel
     * @param color color of the symbol
     */
    public void addSymbol(int pos, String color) {
        addSymbol("normal", pos, color);
    }

    /**
     * Adds a symbol of the given type to a wheel (and registers its color).
     *
     * @param type  type of the symbol ("normal", "ephemeral", "shy", "neon" or any registered type)
     * @param pos   position of the wheel (it must exist)
     * @param color color of the symbol
     */
    public void addSymbol(String type, int pos, String color) {
        if (pos < 1 || pos > this.wheels.size()) {
            notifyError("Posicion de rueda invalida para el simbolo.");
            return;
        }
        Symbol symbol;
        try {
            symbol = SymbolFactory.create(type, color);
        } catch (IllegalArgumentException e) {
            notifyError(e.getMessage());
            return;
        }
        Wheel target = this.wheels.get(pos - 1);
        if (target.hasSymbol(color)) {
            notifyError("La rueda " + pos + " ya tiene el simbolo '" + color + "'.");
            return;
        }
        target.place(symbol);
        if (!this.allowedSymbols.contains(color)) {
            this.allowedSymbols.add(color);
        }
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Deletes a symbol from the machine: it is removed from the registered
     * symbols and from every wheel that has it.
     *
     * @param symbol color of the symbol
     */
    public void delSymbol(String symbol) {
        if (!this.allowedSymbols.remove(symbol)) {
            notifyError("El simbolo '" + symbol + "' no esta registrado.");
            return;
        }
        for (Wheel w : this.wheels) {
            w.removeSymbol(symbol);
        }
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Places a registered symbol (as a normal symbol) in a wheel.
     *
     * @param wheel  position of the wheel
     * @param symbol color of a registered symbol
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
        Wheel target = this.wheels.get(targetWheel - 1);
        if (target.hasSymbol(symbol)) {
            notifyError("La rueda " + targetWheel + " ya tiene el simbolo '" + symbol + "'.");
            return;
        }
        target.place(symbol);
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Spins a wheel one step.
     *
     * @param wheel position of the wheel
     */
    public void spin(int wheel) {
        spin(wheel, 1);
    }

    /**
     * Spins a wheel a number of steps (negative steps go backwards). A lefty
     * wheel copies the wheel at its left; a locked wheel does not move.
     *
     * @param wheel position of the wheel
     * @param steps number of steps
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

        Wheel left = w > 1 ? this.wheels.get(w - 2) : null;
        for (int i = 0; i < Math.abs(steps); i++) {
            if (steps >= 0) {
                target.spin(left);
            } else {
                target.spinBackwards(left);
            }
        }
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Assigns a specific configuration to the machine (one symbol per wheel).
     * Nothing changes if any symbol is not valid or if a locked wheel would
     * have to change.
     *
     * @param setSymbols colors to show, from the leftmost wheel
     */
    public void spin(String[] setSymbols) {
        if (setSymbols == null || setSymbols.length != this.wheels.size()) {
            notifyError("La configuracion no coincide con las ruedas.");
            return;
        }
        for (int i = 0; i < setSymbols.length; i++) {
            String sym = setSymbols[i];
            Wheel w = this.wheels.get(i);
            if (!this.allowedSymbols.contains(sym) || !w.hasSymbol(sym)) {
                notifyError("El simbolo '" + sym + "' no es valido.");
                return;
            }
            if (w.isLocked() && !sym.equals(w.getVisibleSymbol())) {
                notifyError("La rueda " + (i + 1) + " esta bloqueada.");
                return;
            }
        }
        for (int i = 0; i < setSymbols.length; i++) {
            this.wheels.get(i).setVisibleSymbol(setSymbols[i]);
        }
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Spins one step all the wheels that are unlocked, from left to right
     * (so a lefty wheel copies a left wheel that has already turned).
     */
    public void spin() {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para girar.");
            return;
        }
        for (int i = 0; i < this.wheels.size(); i++) {
            Wheel w = this.wheels.get(i);
            if (!w.isLocked()) {
                w.spin(i > 0 ? this.wheels.get(i - 1) : null);
            }
        }
        refreshJackpot();
        this.isOk = true;
    }

    /**
     * Returns the symbols (colors) of the first wheel.
     *
     * @return the colors, or an empty array if there are no wheels
     */
    public String[] symbols() {
        if (this.wheels.isEmpty()) {
            return new String[0];
        }
        return this.wheels.get(0).getSymbols().toArray(new String[0]);
    }

    /**
     * Returns the number of different symbols being shown by the wheels.
     *
     * @return number of distinct visible symbols
     */
    public int distinctSymbols() {
        HashSet<String> distinct = new HashSet<>();
        for (Wheel w : this.wheels) {
            if (w.isShowingSymbol()) {
                distinct.add(w.getVisibleSymbol());
            }
        }
        return distinct.size();
    }

    /**
     * Returns the configuration: the color selected by each wheel.
     *
     * @return one color per wheel (null for an empty wheel)
     */
    public String[] configuration() {
        String[] config = new String[this.wheels.size()];
        for (int i = 0; i < this.wheels.size(); i++) {
            config[i] = this.wheels.get(i).getVisibleSymbol();
        }
        return config;
    }

    /**
     * Checks if the machine hit the jackpot: every wheel shows a visible symbol
     * and all of them have the same color. A hidden (shy) symbol cannot match.
     *
     * @return true if it is a jackpot
     */
    public boolean isJackpot() {
        if (this.wheels.isEmpty()) {
            return false;
        }
        String first = this.wheels.get(0).getVisibleSymbol();
        for (Wheel w : this.wheels) {
            if (!w.isShowingSymbol() || !w.getVisibleSymbol().equals(first)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts the visible symbols to a numeric index (position in the
     * registered symbols, -1 if it is not registered).
     *
     * @return one number per wheel
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
     * Hides the simulator and ends the operation.
     */
    public void exit() {
        makeInvisible();
        this.isOk = true;
    }

    /**
     * Tells whether the last operation was successful.
     *
     * @return true if it was
     */
    public boolean ok() {
        return this.isOk;
    }

    /**
     * Returns the wheel at a position (for tests of the same package).
     *
     * @param pos position, starting at 1
     * @return the wheel
     */
    Wheel wheelAt(int pos) {
        return this.wheels.get(pos - 1);
    }

    /**
     * Returns the number of wheels (for tests of the same package).
     *
     * @return number of wheels
     */
    int wheelCount() {
        return this.wheels.size();
    }

    private int clipPosition(int pos, int max) {
        if (pos < 1) {
            return 1;
        }
        return pos > max ? max : pos;
    }

    /**
     * Adapts the size of the machine (and of the canvas, if visible) to the wheels.
     */
    private void dinamicCanvas() {
        int nWheels = this.wheels.size();
        int anchoRectangulos = (nWheels * WHEEL_SPACE) + 40;
        this.rectangExterior.changeSize(220, anchoRectangulos + 40);
        this.indicadorEstado.changeSize(15, anchoRectangulos + 20);
        this.adornoVictoria.changeSize(130, anchoRectangulos + 20);
        this.rectangInterior.changeSize(110, anchoRectangulos);
        if (this.isVisible) {
            Canvas.getCanvas().resizeCanvas(INIT_X + (nWheels * WHEEL_SPACE) + 100, STAT_HEIGHT);
        }
    }

    /**
     * Keeps the jackpot indicator (red/green) and the decoration (yellow/orange)
     * in agreement with the real state of the machine.
     */
    private void refreshJackpot() {
        boolean jackpot = isJackpot();
        if (jackpot != this.isJackpotActive) {
            this.isJackpotActive = jackpot;
            this.indicadorEstado.changeColor(jackpot ? "green" : "red");
            this.adornoVictoria.changeColor(jackpot ? "orange" : "yellow");
            redrawAll();
        }
    }

    /**
     * Redraws every element of the machine in the proper layer order.
     */
    private void redrawAll() {
        if (!this.isVisible) {
            return;
        }
        this.rectangExterior.makeInvisible();
        this.rectangExterior.makeVisible();
        
        this.indicadorEstado.makeInvisible();
        this.indicadorEstado.makeVisible();
        
        this.adornoVictoria.makeInvisible();
        this.adornoVictoria.makeVisible();
        
        this.rectangInterior.makeInvisible();
        this.rectangInterior.makeVisible();

        for (Wheel w : this.wheels) {
            w.makeInvisible();
            w.makeVisible();
        }
    }

    /**
     * If something went wrong sets ok to false and, when the machine is
     * visible, shows a message with JOptionPane.
     */
    private void notifyError(String message) {
        this.isOk = false;
        if (this.isVisible) {
            JOptionPane.showMessageDialog(null, message, "SlotMachine - Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
