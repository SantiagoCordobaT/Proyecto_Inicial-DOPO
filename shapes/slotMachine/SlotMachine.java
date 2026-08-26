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
        if (pos < 1){
            pos = 1;
        }
        if (pos > wheels.size()+1){
            pos = wheels.size()+1;
        }
        Wheel otherWheel = new Wheel();
        wheels.add(pos - 1, otherWheel);
        this.isOk = true;
    }
    
    /**
     * Delets a wheel with a background who is attach. 
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
     * @return true if the last operation was successful, false if not.
     */
    public boolean ok(){
        return isOk;
    }
    
    /**
     * Add wheel
     */
    public void addWheel (int pos){
        //Al momento de registar una posicion negativa, lo corrige y lo toma como una 
        //posicion correcta.
        if (pos < 1) {
            pos = 1;
        }
        //Esto funciona como una ArrayList, donde si se quiere meter una nueva rueda, puede 
        //agrandar el tamaño de limite superior.
        if (pos > wheels.size() + 1) {
            pos = wheels.size() + 1;
        }
        //Crea una nueva rueda (objeto).
        Wheel nuevaRueda = new Wheel();
        //añade a la ArrayList en la posicion 1 y no 0 por (pos - 1).
        wheels.add(pos - 1, nuevaRueda);
        //Actualiza el estado de la operacion, donde infroma que si se pudo hacer el cambio. 
        isOk = true; 
    }
    
    /**
     * delete wheel
     */
    public void delwheel (int pos){
        //Al momento de registar una posicion negativa, lo corrige y lo toma como una 
        //posicion correcta.
        if (pos < 1){
            pos = 1;
        }
        //Esto funciona como una ArrayList, donde utiliza el limite superior
        if (pos > wheels.size()){
            pos = wheels.size();
        }
        //Elimina el objeto utilizando la posicion del usuario.   
        wheels.remove(pos - 1);
        isOk = true;
    }
}