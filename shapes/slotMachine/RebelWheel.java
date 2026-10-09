/**
 * Wheel that cannot be locked, swapped nor deleted.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class RebelWheel extends Wheel {

    /**
     * Creates a rebel wheel.
     */
    public RebelWheel() {
        super();
    }

    @Override
    public String getType() {
        return "rebel";
    }

    @Override
    protected String getBezelColor() {
        return "orange";
    }

    @Override
    public boolean canLock() {
        return false;
    }

    @Override
    public boolean canSwap() {
        return false;
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }
}
