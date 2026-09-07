import java.util.ArrayList;

/**
 * Representa rueda de SlotMachine.
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
     * Construye una nueva rueda en la coordenada (0,0).
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
     * Dibuja la rueda en pantalla respetando las capas.
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
     * Oculta la rueda de la pantalla.
     */
    public void makeInvisible() {
        this.outerBezel.makeInvisible();
        this.innerReel.makeInvisible();
        this.symbolShape.makeInvisible();
        this.isVisible = false;
    }

    /**
     * Mueve la rueda horizontalmente.
     */
    public void moveHorizontal(int distance) {
        this.outerBezel.moveHorizontal(distance);
        this.innerReel.moveHorizontal(distance);
        this.symbolShape.moveHorizontal(distance);
    }

    /**
     * Mueve la rueda verticalmente.
     */
    public void moveVertical(int distance) {
        this.outerBezel.moveVertical(distance);
        this.innerReel.moveVertical(distance);
        this.symbolShape.moveVertical(distance);
    }

    /**
     * Agrega un simbolo a la rueda y lo muestra si es el primero.
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
     * Fija la rueda impidiendo giros y marca en rojo.
     */
    public void lock() {
        this.isLocked = true;
        this.outerBezel.changeColor("red");
        fixZOrder();
    }

    /**
     * Libera la rueda y restaura color original.
     */
    public void unlock() {
        this.isLocked = false;
        this.outerBezel.changeColor("white");
        fixZOrder();
    }

    /**
     * Devuelve true si la rueda esta bloqueada.
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Rota la rueda un paso adelante.
     */
    public void spin() {
        spinOneStep(1);
    }

    /**
     * Rota la rueda un paso atras.
     */
    public void spinBackwards() {
        spinOneStep(-1);
    }

    /**
     * Calcula y muestra el siguiente simbolo.
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
     * Muestra un simbolo especifico forzando el giro.
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
     * Devuelve los simbolos de la rueda.
     */
    public ArrayList<String> getSymbols() {
        return this.symbols;
    }

    /**
     * Devuelve el simbolo visible actual.
     */
    public String getVisibleSymbol() {
        if (this.symbols.isEmpty()) {
            return null;
        }
        return this.symbols.get(this.currentVisibleIndex);
    }

    /**
     * Restaura el orden visual tras un cambio de color.
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