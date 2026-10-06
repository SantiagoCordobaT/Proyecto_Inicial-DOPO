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

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return "rebel";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBezelColor() {
        return "orange";
    }

    /** {@inheritDoc} */
    @Override
    public boolean canLock() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean canSwap() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean canBeDeleted() {
        return false;
    }
}
