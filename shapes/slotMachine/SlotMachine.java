import java.util.*;

/**
 * Clase principal de la maquina tragamonedas.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 1
 */
public class SlotMachine
{
    private boolean isOk;
    private boolean isVisible;
    private ArrayList<Wheel> wheels; //machine's wheels (composition)
    private ArrayList<String> allowedSymbols;
    private Rectangle background;

    /**
     * Constructor for objects of class SlotMachine.
     */
    public SlotMachine()
    {
        this.wheels = new ArrayList<>();
        this.allowedSymbols = new ArrayList<>();
        this.isOk = true;
        this.isVisible = false;
        
        this.background = new Rectangle();
        background.changeColor("black");
        background.moveHorizontal(50);
        background.moveVertical(50);
        background.changeSize(300, 200);
    }

    /**
     * Make's the background machine visible in the computer.
     */
    public void makeVisible(){
        background.makeVisible();
        this.isVisible = true;
    }
    
    /**
     * Make's the background machine invisible in the computer.
     */
    public void makeInvisible(){
        background.makeInvisible();
        this.isVisible = false;
    }
    
    /**
     * Add wheels with their background in slotMachine.
     */
    public void addWheel(int pos){
        // Al momento de registrar una posicion negativa o cero, lo corrige y lo toma como la posicion 1.
        if (pos < 1){
            pos = 1;
        }
        
        // Esto funciona como un ArrayList, donde si se quiere meter una nueva rueda, puede 
        // agrandar el tamaño del limite superior (insercion al final).
        if (pos > wheels.size() + 1){
            pos = wheels.size() + 1;
        }
        
        // Crea una nueva rueda (objeto).
        Wheel nuevaRueda = new Wheel();
        
        // Añade a la ArrayList en la posicion correcta (pos - 1 para el indice de Java).
        wheels.add(pos - 1, nuevaRueda);
        
        // Actualiza el estado de la operacion, donde informa que si se pudo hacer el cambio. 
        this.isOk = true;
    }
    
    /**
     * Deletes a wheel with a background who is attach. 
     */
    public void delWheel(int pos){
        // Si no hay ruedas en la maquina, no hay nada que borrar y la operacion falla.
        if (wheels.isEmpty()){
            this.isOk = false;
            return;
        }
        
        // Al momento de registrar una posicion negativa o cero, lo corrige y lo toma como la posicion 1.
        if (pos < 1){
            pos = 1;
        }
        
        // Esto funciona como un ArrayList, donde utiliza el limite superior maximo de elementos existentes.
        if (pos > wheels.size()){
            pos = wheels.size();
        }
        
        // Elimina el objeto utilizando la posicion del usuario (restando 1 para el indice).   
        wheels.remove(pos - 1);
        
        // Actualiza el estado de la operacion.
        this.isOk = true;
    }
    
    /**
     * @return true if the last operation was successful, false if not.
     */
    public boolean ok(){
        return isOk;
    }
}