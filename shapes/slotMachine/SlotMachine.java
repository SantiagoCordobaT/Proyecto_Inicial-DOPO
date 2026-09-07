import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Controlador principal del simulador de Maquina (Slot Machine).
 * 
 * @author Santiago Cordoba - Camilo Rivas
 * @version 2.0
 */
public class SlotMachine {
    private boolean isOk;
    private boolean isVisible;
    private boolean isJackpotActive;
    private ArrayList<Wheel> wheels;
    private ArrayList<String> allowedSymbols;

    private Rectangle mainCabinet;
    private Rectangle marqueeLamp;
    private Rectangle screenBezel;
    private Rectangle screenArea;

    private final int WHEEL_SPACING = 65; 
    private final int BASE_X = 55;        
    private final int BASE_Y = 80;        

    /**
     * Inicializa la maquina y sus componentes visuales base.
     */
    public SlotMachine() {
        this.wheels = new ArrayList<>();
        this.allowedSymbols = new ArrayList<>();
        this.isOk = true;
        this.isVisible = false;
        this.isJackpotActive = false;

        this.mainCabinet = new Rectangle();
        this.mainCabinet.changeColor("blue");
        this.mainCabinet.changeSize(220, 260); 
        this.mainCabinet.moveHorizontal(-60 + 20);
        this.mainCabinet.moveVertical(-50 + 20); 

        this.marqueeLamp = new Rectangle();
        this.marqueeLamp.changeColor("red");
        this.marqueeLamp.changeSize(15, 220); 
        this.marqueeLamp.moveHorizontal(-60 + 40); 
        this.marqueeLamp.moveVertical(-50 + 30);

        this.screenBezel = new Rectangle();
        this.screenBezel.changeColor("yellow");
        this.screenBezel.changeSize(130, 240);
        this.screenBezel.moveHorizontal(-60 + 30);
        this.screenBezel.moveVertical(-50 + 60);

        this.screenArea = new Rectangle();
        this.screenArea.changeColor("green");
        this.screenArea.changeSize(110, 220);
        this.screenArea.moveHorizontal(-60 + 40);
        this.screenArea.moveVertical(-50 + 70);
    }

    /**
     * Muestra la maquina y sus ruedas en pantalla respetando el orden de capas.
     */
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

    /**
     * Oculta la maquina y sus ruedas de la pantalla.
     */
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

    /**
     * Añade una nueva rueda al final de la maquina.
     */
    public boolean addWheel() {
        return addWheel(this.wheels.size() + 1);
    }

    /**
     * Añade una nueva rueda en una posicion especifica.
     */
    public boolean addWheel(int pos) {
        int targetPos = pos;
        if (targetPos < 1) targetPos = 1;
        if (targetPos > this.wheels.size() + 1) targetPos = this.wheels.size() + 1;

        Wheel newWheel = new Wheel();
        
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

    /**
     * Elimina la rueda de la posicion indicada.
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
            this.wheels.get(i).moveHorizontal(-WHEEL_SPACING);
        }
        this.isOk = true;
    }

    /**
     * Intercambia la posicion de dos ruedas.
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

        int pixelDistance = (idx2 - idx1) * WHEEL_SPACING;
        obj1.moveHorizontal(pixelDistance);
        obj2.moveHorizontal(-pixelDistance);

        Collections.swap(this.wheels, idx1, idx2);
        this.isOk = true;
    }

    /**
     * Bloquea una rueda para que no gire.
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
     * Desbloquea una rueda previamente fijada.
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
     * Registra un simbolo nuevo en la lista permitida.
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
     * Elimina un simbolo de la lista permitida.
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
     * Asigna un simbolo valido a una rueda.
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
     * Gira una rueda un paso hacia adelante.
     */
    public void spin(int wheel) {
        spin(wheel, 1);
    }

    /**
     * Gira una rueda multiples pasos con animacion visible.
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

            if (this.isVisible) {
                pause(90);
            }
        }

        resetJackpotVisuals();
        this.isOk = true;
    }

    /**
     * Asigna una configuracion especifica de simbolos a todas las ruedas.
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
     * Gira un paso todas las ruedas no bloqueadas.
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
     * Retorna los simbolos presentes en la primera rueda.
     */
    public String[] symbols() {
        if (this.wheels.isEmpty()) return new String[0];
        return this.wheels.get(0).getSymbols().toArray(new String[0]);
    }

    /**
     * Retorna la cantidad total de simbolos unicos en toda la maquina.
     */
    public int distinctSymbols() {
        HashSet<String> distinct = new HashSet<>();
        for (Wheel w : this.wheels) {
            distinct.addAll(w.getSymbols());
        }
        return distinct.size();
    }

    /**
     * Retorna la combinacion de simbolos visibles actualmente.
     */
    public String[] configuration() {
        String[] config = new String[this.wheels.size()];
        for (int i = 0; i < this.wheels.size(); i++) {
            config[i] = this.wheels.get(i).getVisibleSymbol();
        }
        return config;
    }

    /**
     * Verifica si todas las ruedas muestran el mismo simbolo.
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
            this.marqueeLamp.changeColor("yellow");
            this.screenBezel.changeColor("green");
            this.isJackpotActive = true;
            fixZOrder();
        } else {
            resetJackpotVisuals();
        }

        this.isOk = true;
        return isWin;
    }

    /**
     * Convierte los simbolos visibles actuales a indices numericos.
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
     * Oculta el simulador y termina la sesion visual.
     */
    public void exit() {
        makeInvisible();
        this.isOk = true;
    }

    /**
     * Retorna true si la ultima operacion fue exitosa.
     */
    public boolean ok() {
        return this.isOk;
    }

    /**
     * Asegura que una posicion este dentro de los limites validos.
     */
    private int clipPosition(int pos, int max) {
        if (pos < 1) return 1;
        if (pos > max) return max;
        return pos;
    }

    /**
     * Restaura los colores normales y previene que tapen a las ruedas (Z-Index fix).
     */
    private void resetJackpotVisuals() {
        if (this.isJackpotActive) {
            this.marqueeLamp.changeColor("red");
            this.screenBezel.changeColor("yellow");
            this.isJackpotActive = false;
            fixZOrder();
        }
    }

    /**
     * Restaura el orden de las capas de BlueJ tras un cambio de color.
     */
    private void fixZOrder() {
        if (this.isVisible) {
            this.screenArea.makeInvisible();
            this.screenArea.makeVisible();
            for (Wheel w : this.wheels) {
                w.makeInvisible();
                w.makeVisible();
            }
        }
    }

    /**
     * Fija isOk en falso y muestra alerta si esta visible.
     */
    private void notifyError(String message) {
        this.isOk = false;
        if (this.isVisible) {
            JOptionPane.showMessageDialog(null, message, "SlotMachine - Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Pausa la ejecucion para animacion.
     */
    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}