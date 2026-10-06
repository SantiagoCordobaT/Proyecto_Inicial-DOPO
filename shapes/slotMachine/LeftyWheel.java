/**
 * Wheel that, when it spins and has a wheel at its left, copies the symbol
 * shown by that wheel. Without a left wheel (or if the left symbol does not
 * exist in this wheel) it turns like a normal wheel.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class LeftyWheel extends Wheel {

    /**
     * Creates a lefty wheel.
     */
    public LeftyWheel() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return "lefty";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBezelColor() {
        return "cyan";
    }

    /** {@inheritDoc} */
    @Override
    public void spin(Wheel left) {
        if (!copy(left)) {
            super.spin(left);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void spinBackwards(Wheel left) {
        if (!copy(left)) {
            super.spinBackwards(left);
        }
    }

    /**
     * Copies the state of the left wheel.
     *
     * @param left the wheel at the left (may be null)
     * @return true if the symbol was copied
     */
    private boolean copy(Wheel left) {
        if (left == null || isLocked()) {
            return false;
        }
        String leftSymbol = left.getVisibleSymbol();
        return leftSymbol != null && advanceTo(leftSymbol);
    }
}
