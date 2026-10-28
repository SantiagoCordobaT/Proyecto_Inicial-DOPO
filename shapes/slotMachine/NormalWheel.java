/**
 * The standard slot machine wheel.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class NormalWheel extends Wheel {

    /**
     * Creates a normal wheel.
     */
    public NormalWheel() {
        super();
    }

    @Override
    public String getType() {
        return "normal";
    }

    @Override
    protected String getBezelColor() {
        return "white";
    }
}