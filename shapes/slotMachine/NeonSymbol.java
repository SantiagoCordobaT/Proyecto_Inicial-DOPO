/**
 * New symbol type proposed by the team (requirement 19): it glows while it is
 * the symbol shown by its wheel and turns off as soon as the wheel moves on.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class NeonSymbol extends Symbol {
    private boolean glowing;

    /**
     * Creates a neon symbol (turned off at the beginning).
     *
     * @param color color name of the symbol
     */
    public NeonSymbol(String color) {
        super(color);
        this.glowing = false;
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return "neon";
    }

    /** Turns the glow on. */
    @Override
    public void onSelected() {
        this.glowing = true;
    }

    /** Turns the glow off. */
    @Override
    public void onDeselected() {
        this.glowing = false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGlowing() {
        return this.glowing;
    }

    /** {@inheritDoc} */
    @Override
    public String getBadgeColor() {
        return "white";
    }
}
