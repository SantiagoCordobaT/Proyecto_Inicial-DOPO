/**
 * Base class of every symbol that can be placed on a {@link Wheel}.
 * <p>
 * A symbol has a color, a size, a visibility state and three life-cycle hooks
 * that a {@link Wheel} invokes: {@link #onSpin()} on every step the wheel turns,
 * {@link #onSelected()} when the symbol becomes the one shown by the wheel and
 * {@link #onDeselected()} when it stops being the shown one. Each subclass
 * decides what to do in those hooks, so new symbol types do not require any
 * change in {@link Wheel} or {@link SlotMachine}.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public abstract class Symbol {
    /** Diameter, in pixels, of a symbol that has not been modified. */
    public static final int DEFAULT_SIZE = 30;

    private final String color;
    private boolean visible;
    private int size;

    /**
     * Creates a visible symbol of the default size.
     *
     * @param color color name of the symbol (it must not be null or empty)
     * @throws IllegalArgumentException if the color is null or empty
     */
    public Symbol(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("El simbolo necesita un color.");
        }
        this.color = color;
        this.visible = true;
        this.size = DEFAULT_SIZE;
    }

    /**
     * Returns the name of the symbol type (for example "normal" or "shy").
     *
     * @return type name, in lower case
     */
    public abstract String getType();

    /**
     * Called every time the wheel that owns this symbol turns one step.
     * By default it does nothing.
     */
    public void onSpin() {
    }

    /**
     * Called when this symbol becomes the one shown by its wheel.
     * By default it does nothing.
     */
    public void onSelected() {
    }

    /**
     * Called when this symbol stops being the one shown by its wheel.
     * By default it does nothing.
     */
    public void onDeselected() {
    }

    /**
     * Color of the small marker drawn under the symbol so that its type can be
     * recognized at first sight. A normal symbol has no marker.
     *
     * @return the marker color, or null if the symbol has no marker
     */
    public String getBadgeColor() {
        return null;
    }

    /**
     * Tells whether the symbol is drawn with a glow around it.
     *
     * @return true if it is glowing
     */
    public boolean isGlowing() {
        return false;
    }

    /**
     * Returns the color of the symbol.
     *
     * @return color name
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Tells whether the symbol is visible.
     *
     * @return true if it is visible
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Returns the current diameter of the symbol.
     *
     * @return size in pixels
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Changes the visibility of the symbol.
     *
     * @param visible the new visibility state
     */
    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Changes the size of the symbol.
     *
     * @param size the new diameter in pixels (never negative)
     */
    protected void setSize(int size) {
        this.size = Math.max(0, size);
    }
}
