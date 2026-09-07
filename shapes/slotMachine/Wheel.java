import java.util.ArrayList;

/**
 * Representa un carrete mecánico del simulador SlotMachine.
 * Gestiona la secuencia circular de símbolos, su estado de fijación
 * y una representación visual calibrada para las coordenadas de BlueJ.
 * 
 * @author Santiago Cordoba - Camilo Rivas
 * @version 5.0 (Corrección de Parámetros BlueJ)
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
     * Construye un nuevo carrete alineando matemáticamente los componentes de BlueJ.
     * Nace exactamente en la coordenada (0,0) relativa.
     */
    public Wheel() {
        this.symbols = new ArrayList<>();
        this.currentVisibleIndex = 0;
        this.isVisible = false;
        this.isLocked = false;

        // 1. Marco exterior de la rueda (Blanco)
        this.outerBezel = new Rectangle();
        this.outerBezel.changeColor("white");
        // ATENCIÓN BLUEJ: changeSize(alto, ancho) -> Alto: 90, Ancho: 50
        this.outerBezel.changeSize(90, 50); 
        this.outerBezel.moveHorizontal(-60); // Neutraliza a X = 0
        this.outerBezel.moveVertical(-50);   // Neutraliza a Y = 0

        // 2. Fondo oscuro del visor (Negro)
        this.innerReel = new Rectangle();
        this.innerReel.changeColor("black");
        // changeSize(alto, ancho) -> Alto: 80, Ancho: 40
        this.innerReel.changeSize(80, 40);
        // Centrado: 5px de margen interno respecto al marco
        this.innerReel.moveHorizontal(-60 + 5); 
        this.innerReel.moveVertical(-50 + 5);   

        // 3. Símbolo (Círculo)
        this.symbolShape = new Circle();
        this.symbolShape.changeSize(30); // Diámetro 30
        // Centrado matemático: X=10, Y=30 relativas al marco
        this.symbolShape.moveHorizontal(-20 + 10); 
        this.symbolShape.moveVertical(-60 + 30);   
    }

    public void makeVisible() {
        this.outerBezel.makeVisible();
        this.innerReel.makeVisible();
        if (!this.symbols.isEmpty()) {
            this.symbolShape.makeVisible();
        }
        this.isVisible = true;
    }

    public void makeInvisible() {
        this.outerBezel.makeInvisible();
        this.innerReel.makeInvisible();
        this.symbolShape.makeInvisible();
        this.isVisible = false;
    }

    public void moveHorizontal(int distance) {
        this.outerBezel.moveHorizontal(distance);
        this.innerReel.moveHorizontal(distance);
        this.symbolShape.moveHorizontal(distance);
    }

    public void moveVertical(int distance) {
        this.outerBezel.moveVertical(distance);
        this.innerReel.moveVertical(distance);
        this.symbolShape.moveVertical(distance);
    }

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
     * Requisito 10: Fija la rueda impidiendo rotaciones y cambia su marco a rojo.
     */
    public void lock() {
        this.isLocked = true;
        this.outerBezel.changeColor("red"); // Alerta visual
    }

    /**
     * Requisito 10: Libera la rueda y restaura el color del bisel.
     */
    public void unlock() {
        this.isLocked = false;
        this.outerBezel.changeColor("white"); // Restaura
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void spin() {
        spinOneStep(1);
    }

    public void spinBackwards() {
        spinOneStep(-1);
    }

    private void spinOneStep(int delta) {
        if (this.isLocked || this.symbols.size() <= 1) {
            return;
        }
        int total = this.symbols.size();
        this.currentVisibleIndex = ((this.currentVisibleIndex + delta) % total + total) % total;
        String newColor = this.symbols.get(this.currentVisibleIndex);
        this.symbolShape.changeColor(newColor);
    }

    public boolean setVisibleSymbol(String color) {
        int idx = this.symbols.indexOf(color);
        if (idx != -1) {
            this.currentVisibleIndex = idx;
            this.symbolShape.changeColor(color);
            return true;
        }
        return false;
    }

    public ArrayList<String> getSymbols() {
        return this.symbols;
    }

    public String getVisibleSymbol() {
        if (this.symbols.isEmpty()) {
            return null;
        }
        return this.symbols.get(this.currentVisibleIndex);
    }
}