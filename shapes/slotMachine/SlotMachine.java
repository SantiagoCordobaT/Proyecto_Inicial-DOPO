import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Controlador principal del simulador de Máquina Tragamonedas (Slot Machine).
 * Cumple los requisitos de ambos ciclos, gestionando la presentación visual en canvas,
 * la animación de giros, el bloqueo y la evaluación numérica del Jackpot.
 * 
 * @author Santiago Cordoba - Camilo Rivas
 * @version 5.0 (Corrección de Parámetros BlueJ)
 */
public class SlotMachine {
    private boolean isOk;
    private boolean isVisible;
    private ArrayList<Wheel> wheels;
    private ArrayList<String> allowedSymbols;

    private Rectangle mainCabinet;
    private Rectangle marqueeLamp;
    private Rectangle screenBezel;
    private Rectangle screenArea;

    // Espaciado matemático exacto para evitar desbordamientos
    private final int WHEEL_SPACING = 65; // Ancho 50 + 15 de margen
    private final int BASE_X = 55;        // Margen izquierdo dentro de la pantalla verde
    private final int BASE_Y = 80;        // Margen superior dentro de la pantalla verde

    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.allowedSymbols = new ArrayList<>();
        this.isOk = true;
        this.isVisible = false;

        // 1. Gabinete (Fondo general azul)
        this.mainCabinet = new Rectangle();
        this.mainCabinet.changeColor("blue");
        // ATENCIÓN BLUEJ: changeSize(alto, ancho) -> Alto: 220, Ancho: 260
        this.mainCabinet.changeSize(220, 260); 
        this.mainCabinet.moveHorizontal(-60 + 20); // Posición X = 20
        this.mainCabinet.moveVertical(-50 + 20);   // Posición Y = 20

        // 2. Marquesina (Luz superior indicadora)
        this.marqueeLamp = new Rectangle();
        this.marqueeLamp.changeColor("red");
        this.marqueeLamp.changeSize(15, 220); // Alto 15, Ancho 220
        this.marqueeLamp.moveHorizontal(-60 + 40); // Posición X = 40
        this.marqueeLamp.moveVertical(-50 + 30);   // Posición Y = 30

        // 3. Marco de la pantalla
        this.screenBezel = new Rectangle();
        this.screenBezel.changeColor("yellow");
        this.screenBezel.changeSize(130, 240); // Alto 130, Ancho 240
        this.screenBezel.moveHorizontal(-60 + 30); // Posición X = 30
        this.screenBezel.moveVertical(-50 + 60);   // Posición Y = 60

        // 4. Zona interior (Pantalla Verde)
        this.screenArea = new Rectangle();
        this.screenArea.changeColor("green");
        this.screenArea.changeSize(110, 220); // Alto 110, Ancho 220
        this.screenArea.moveHorizontal(-60 + 40); // Posición X = 40
        this.screenArea.moveVertical(-50 + 70);   // Posición Y = 70
    }

    public void makeVisible() {
        this.mainCabinet.makeVisible();
        this.marqueeLamp.makeVisible();
        this.screenBezel.makeVisible();
        this.screenArea.makeVisible();
        for (Wheel w : this.wheels) {
            w.makeVisible();
        }
        this.isVisible = true;
        this.isOk = true;
    }

    public void makeInvisible() {
        this.mainCabinet.makeInvisible();
        this.marqueeLamp.makeInvisible();
        this.screenBezel.makeInvisible();
        this.screenArea.makeInvisible();
        for (Wheel w : this.wheels) {
            w.makeInvisible();
        }
        this.isVisible = false;
        this.isOk = true;
    }

    public boolean addWheel() {
        return addWheel(this.wheels.size() + 1);
    }

    public boolean addWheel(int pos) {
        int targetPos = pos;
        if (targetPos < 1) targetPos = 1;
        if (targetPos > this.wheels.size() + 1) targetPos = this.wheels.size() + 1;

        Wheel newWheel = new Wheel();
        
        // Colocación exacta dentro de la pantalla verde
        int displacementX = BASE_X + ((targetPos - 1) * WHEEL_SPACING);
        newWheel.moveHorizontal(displacementX);
        newWheel.moveVertical(BASE_Y);

        for (int i = targetPos - 1; i < this.wheels.size(); i++) {
            this.wheels.get(i).moveHorizontal(WHEEL_SPACING);
        }

        this.wheels.add(targetPos - 1, newWheel);
        if (this.isVisible) {
            newWheel.makeVisible();
        }
        this.isOk = true;
        return true;
    }

    public void delWheel(int pos) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas instaladas para eliminar.");
            return;
        }

        int targetPos = clipPosition(pos, this.wheels.size());
        Wheel removed = this.wheels.remove(targetPos - 1);
        removed.makeInvisible();

        for (int i = targetPos - 1; i < this.wheels.size(); i++) {
            this.wheels.get(i).moveHorizontal(-WHEEL_SPACING);
        }
        this.isOk = true;
    }

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

        int pixelDistance = (idx2 - idx1) * WHEEL_SPACING;
        obj1.moveHorizontal(pixelDistance);
        obj2.moveHorizontal(-pixelDistance);

        Collections.swap(this.wheels, idx1, idx2);
        this.isOk = true;
    }

    public void lock(int wheel) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para fijar.");
            return;
        }
        int w = clipPosition(wheel, this.wheels.size());
        this.wheels.get(w - 1).lock();
        this.isOk = true;
    }

    public void unlock(int wheel) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para soltar.");
            return;
        }
        int w = clipPosition(wheel, this.wheels.size());
        this.wheels.get(w - 1).unlock();
        this.isOk = true;
    }

    public void addSymbol(int pos, String color) {
        if (color == null || color.trim().isEmpty()) {
            notifyError("El nombre del símbolo no puede ser vacío.");
            return;
        }
        if (this.allowedSymbols.contains(color)) {
            notifyError("El símbolo '" + color + "' ya existe.");
            return;
        }

        int targetPos = clipPosition(pos, this.allowedSymbols.size() + 1);
        this.allowedSymbols.add(targetPos - 1, color);
        this.isOk = true;
    }

    public void delSymbol(String symbol) {
        boolean wasRemoved = this.allowedSymbols.remove(symbol);
        if (wasRemoved) {
            this.isOk = true;
        } else {
            notifyError("El símbolo '" + symbol + "' no está registrado.");
        }
    }

    public void placeSymbol(int wheel, String symbol) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas instaladas.");
            return;
        }
        if (!this.allowedSymbols.contains(symbol)) {
            notifyError("El símbolo no está autorizado.");
            return;
        }

        int targetWheel = clipPosition(wheel, this.wheels.size());
        this.wheels.get(targetWheel - 1).place(symbol);
        this.isOk = true;
    }

    public void spin(int wheel) {
        spin(wheel, 1);
    }

    public void spin(int wheel, int steps) {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para girar.");
            return;
        }

        int w = clipPosition(wheel, this.wheels.size());
        Wheel target = this.wheels.get(w - 1);

        if (target.isLocked()) {
            notifyError("La rueda " + w + " está bloqueada.");
            return;
        }

        if (target.getSymbols().isEmpty()) {
            notifyError("La rueda no posee símbolos.");
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

            if (this.isVisible) {
                pause(90);
            }
        }

        resetJackpotVisuals();
        this.isOk = true;
    }

    public void spin(String[] setSymbols) {
        if (setSymbols == null || setSymbols.length != this.wheels.size()) {
            notifyError("La configuración no coincide con las ruedas.");
            return;
        }

        for (int i = 0; i < setSymbols.length; i++) {
            String sym = setSymbols[i];
            if (!this.allowedSymbols.contains(sym) || !this.wheels.get(i).getSymbols().contains(sym)) {
                notifyError("El símbolo '" + sym + "' no es válido.");
                return;
            }
        }

        for (int i = 0; i < setSymbols.length; i++) {
            this.wheels.get(i).setVisibleSymbol(setSymbols[i]);
        }

        resetJackpotVisuals();
        this.isOk = true;
    }

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

    public String[] symbols() {
        if (this.wheels.isEmpty()) return new String[0];
        return this.wheels.get(0).getSymbols().toArray(new String[0]);
    }

    public int distinctSymbols() {
        HashSet<String> distinct = new HashSet<>();
        for (Wheel w : this.wheels) {
            distinct.addAll(w.getSymbols());
        }
        return distinct.size();
    }

    public String[] configuration() {
        String[] config = new String[this.wheels.size()];
        for (int i = 0; i < this.wheels.size(); i++) {
            config[i] = this.wheels.get(i).getVisibleSymbol();
        }
        return config;
    }

    public boolean isJackpot() {
        if (this.wheels.isEmpty()) {
            notifyError("No hay ruedas para evaluar.");
            return false;
        }

        int[] numericConfig = getNumericConfiguration();
        int targetId = numericConfig[0];

        if (targetId == -1) {
            notifyError("Ruedas sin símbolos definidos.");
            return false;
        }

        int matchCount = 0;
        for (int id : numericConfig) {
            if (id == targetId) matchCount++;
        }

        boolean isWin = (matchCount == this.wheels.size());

        if (isWin) {
            // Jackpot Visual (Req 6)
            this.marqueeLamp.changeColor("yellow");
            this.screenBezel.changeColor("green");
        } else {
            resetJackpotVisuals();
        }

        if (this.isVisible) {
            for (Wheel w : this.wheels) {
                w.makeInvisible();
                w.makeVisible();
            }
        }

        this.isOk = true;
        return isWin;
    }

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

    public void exit() {
        makeInvisible();
        this.isOk = true;
    }

    public boolean ok() {
        return this.isOk;
    }

    private int clipPosition(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    private void resetJackpotVisuals() {
        this.marqueeLamp.changeColor("red");
        this.screenBezel.changeColor("yellow");
    }

    private void notifyError(String message) {
        this.isOk = false;
        if (this.isVisible) {
            JOptionPane.showMessageDialog(null, message, "SlotMachine - Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}