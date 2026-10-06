import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a slot machine wheel (the normal one). Subclasses change its
 * behavior by overriding {@link #canLock()}, {@link #canSwap()},
 * {@link #canBeDeleted()}, {@link #spin(Wheel)} and {@link #getBezelColor()}.
 * <p>
 * The wheel holds {@link Symbol} objects and drives their life cycle: every
 * step it turns it calls {@link Symbol#onSpin()} on all of them,
 * {@link Symbol#onDeselected()} on the one that leaves and
 * {@link Symbol#onSelected()} on the one that arrives.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class Wheel {
    private final List<Symbol> symbols;
    private int currentVisibleIndex;
    private boolean isVisible;
    private boolean isLocked;
    private int appliedOffset;

    private final Rectangle outerBezel;
    private final Rectangle innerReel;
    private final Circle halo;
    private final Circle symbolShape;
    private final Rectangle badge;

    /**
     * Creates a new wheel at default position (0, 0).
     */
    public Wheel() {
        this.symbols = new ArrayList<>();
        this.currentVisibleIndex = 0;
        this.isVisible = false;
        this.isLocked = false;
        this.appliedOffset = 0;

        this.outerBezel = new Rectangle();
        this.outerBezel.changeColor(getBezelColor());
        this.outerBezel.changeSize(90, 50);
        this.outerBezel.moveHorizontal(-60);
        this.outerBezel.moveVertical(-50);

        this.innerReel = new Rectangle();
        this.innerReel.changeColor("gray");
        this.innerReel.changeSize(80, 40);
        this.innerReel.moveHorizontal(-60 + 5);
        this.innerReel.moveVertical(-50 + 5);

        this.halo = new Circle();
        this.halo.changeColor("white");
        this.halo.changeSize(Symbol.DEFAULT_SIZE + 8);
        this.halo.moveHorizontal(-14);
        this.halo.moveVertical(-34);

        this.symbolShape = new Circle();
        this.symbolShape.changeSize(Symbol.DEFAULT_SIZE);
        this.symbolShape.moveHorizontal(-20 + 10);
        this.symbolShape.moveVertical(-60 + 30);

        this.badge = new Rectangle();
        this.badge.changeSize(8, 8);
        this.badge.moveHorizontal(-52);
        this.badge.moveVertical(25);
    }

    /**
     * Name of the wheel type.
     *
     * @return "normal" for this class; subclasses return their own name
     */
    public String getType() {
        return "normal";
    }

    /**
     * Color of the border while the wheel is not locked. It identifies the
     * type of wheel on the screen.
     *
     * @return color name
     */
    protected String getBezelColor() {
        return "white";
    }

    /**
     * Tells whether this wheel accepts to be locked.
     *
     * @return true if it can be locked
     */
    public boolean canLock() {
        return true;
    }

    /**
     * Tells whether this wheel accepts to be swapped with another one.
     *
     * @return true if it can be swapped
     */
    public boolean canSwap() {
        return true;
    }

    /**
     * Tells whether this wheel accepts to be deleted.
     *
     * @return true if it can be deleted
     */
    public boolean canBeDeleted() {
        return true;
    }

    /**
     * Makes the wheel and its components visible on screen.
     */
    public void makeVisible() {
        this.isVisible = true;
        this.outerBezel.makeVisible();
        redrawLayers();
    }

    /**
     * Hides the wheel from the screen.
     */
    public void makeInvisible() {
        this.outerBezel.makeInvisible();
        this.innerReel.makeInvisible();
        this.halo.makeInvisible();
        this.symbolShape.makeInvisible();
        this.badge.makeInvisible();
        this.isVisible = false;
    }

    /**
     * Moves the wheel horizontally by a given distance.
     *
     * @param distance pixels to move (negative moves to the left)
     */
    public void moveHorizontal(int distance) {
        this.outerBezel.moveHorizontal(distance);
        this.innerReel.moveHorizontal(distance);
        this.halo.moveHorizontal(distance);
        this.symbolShape.moveHorizontal(distance);
        this.badge.moveHorizontal(distance);
    }

    /**
     * Moves the wheel vertically by a given distance.
     *
     * @param distance pixels to move (negative moves up)
     */
    public void moveVertical(int distance) {
        this.outerBezel.moveVertical(distance);
        this.innerReel.moveVertical(distance);
        this.halo.moveVertical(distance);
        this.symbolShape.moveVertical(distance);
        this.badge.moveVertical(distance);
    }

    /**
     * Adds a symbol to the wheel; if it is the first one it becomes the shown one.
     *
     * @param symbol the symbol to add
     * @throws IllegalArgumentException if the symbol is null
     */
    public void place(Symbol symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("El simbolo no puede ser nulo.");
        }
        this.symbols.add(symbol);
        if (this.symbols.size() == 1) {
            this.currentVisibleIndex = 0;
            refreshSymbol();
        }
    }

    /**
     * Adds a normal symbol of the given color.
     *
     * @param color color of the new symbol
     */
    public void place(String color) {
        place(new NormalSymbol(color));
    }

    /**
     * Removes every symbol of the given color.
     *
     * @param color color of the symbol(s) to remove
     * @return true if something was removed
     */
    public boolean removeSymbol(String color) {
        Symbol current = getCurrentSymbol();
        boolean removed = this.symbols.removeIf(s -> s.getColor().equals(color));
        if (!removed) {
            return false;
        }
        if (this.symbols.isEmpty()) {
            this.currentVisibleIndex = 0;
        } else {
            int idx = this.symbols.indexOf(current);
            this.currentVisibleIndex = idx >= 0 ? idx
                : Math.min(this.currentVisibleIndex, this.symbols.size() - 1);
        }
        refreshSymbol();
        return true;
    }

    /**
     * Tells whether the wheel has a symbol of the given color.
     *
     * @param color color to look for
     * @return true if it has it
     */
    public boolean hasSymbol(String color) {
        return indexOfColor(color) != -1;
    }

    /**
     * Locks the wheel (it cannot move) and turns the border red.
     *
     * @return true if the wheel was locked, false if this wheel refuses to be locked
     */
    public boolean lock() {
        if (!canLock()) {
            return false;
        }
        this.isLocked = true;
        this.outerBezel.changeColor("red");
        redrawLayers();
        return true;
    }

    /**
     * Unlocks the wheel and restores the color of its border.
     */
    public void unlock() {
        this.isLocked = false;
        this.outerBezel.changeColor(getBezelColor());
        redrawLayers();
    }

    /**
     * Tells whether the wheel is locked.
     *
     * @return true if it is locked
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Spins the wheel one step forward.
     */
    public void spin() {
        spin(null);
    }

    /**
     * Spins the wheel one step backward.
     */
    public void spinBackwards() {
        spinBackwards(null);
    }

    /**
     * Spins the wheel one step forward knowing which wheel is at its left.
     * A normal wheel ignores the neighbor.
     *
     * @param left the wheel at the left, or null if there is none
     */
    public void spin(Wheel left) {
        advance(1);
    }

    /**
     * Spins the wheel one step backward knowing which wheel is at its left.
     * A normal wheel ignores the neighbor.
     *
     * @param left the wheel at the left, or null if there is none
     */
    public void spinBackwards(Wheel left) {
        advance(-1);
    }

    /**
     * Turns the wheel delta positions.
     *
     * @param delta positions to advance (negative goes backwards)
     * @return false if the wheel did not move (locked or empty)
     */
    protected boolean advance(int delta) {
        if (this.isLocked || this.symbols.isEmpty()) {
            return false;
        }
        int total = this.symbols.size();
        land(((this.currentVisibleIndex + delta) % total + total) % total);
        return true;
    }

    /**
     * Turns the wheel so that it lands on the given color (used to copy the
     * state of another wheel). It counts as a spin for the symbols.
     *
     * @param color the color to show
     * @return false if the wheel is locked or does not have that color
     */
    protected boolean advanceTo(String color) {
        int idx = indexOfColor(color);
        if (this.isLocked || idx == -1) {
            return false;
        }
        land(idx);
        return true;
    }

    /**
     * Forces the wheel to show a specific symbol if present. It is a
     * selection, not a spin: the symbols are not notified with onSpin.
     *
     * @param color the color to show
     * @return true if the symbol exists in this wheel
     */
    public boolean setVisibleSymbol(String color) {
        int idx = indexOfColor(color);
        if (idx == -1) {
            return false;
        }
        if (idx != this.currentVisibleIndex) {
            this.symbols.get(this.currentVisibleIndex).onDeselected();
            this.currentVisibleIndex = idx;
            this.symbols.get(idx).onSelected();
        }
        refreshSymbol();
        return true;
    }

    /**
     * Returns the colors of all symbols contained in this wheel.
     *
     * @return a new list with the colors
     */
    public ArrayList<String> getSymbols() {
        ArrayList<String> colors = new ArrayList<>();
        for (Symbol s : this.symbols) {
            colors.add(s.getColor());
        }
        return colors;
    }

    /**
     * Returns the symbols (objects) of this wheel.
     *
     * @return unmodifiable list of symbols
     */
    public List<Symbol> getSymbolObjects() {
        return Collections.unmodifiableList(this.symbols);
    }

    /**
     * Returns the symbol currently selected.
     *
     * @return the symbol, or null if the wheel is empty
     */
    public Symbol getCurrentSymbol() {
        if (this.symbols.isEmpty()) {
            return null;
        }
        return this.symbols.get(this.currentVisibleIndex);
    }

    /**
     * Returns the color of the currently selected symbol.
     *
     * @return the color, or null if the wheel is empty
     */
    public String getVisibleSymbol() {
        Symbol current = getCurrentSymbol();
        return current == null ? null : current.getColor();
    }

    /**
     * Tells whether the wheel is showing a symbol (it has one and it is visible).
     *
     * @return true if a symbol can be seen
     */
    public boolean isShowingSymbol() {
        Symbol current = getCurrentSymbol();
        return current != null && current.isVisible();
    }

    private int indexOfColor(String color) {
        for (int i = 0; i < this.symbols.size(); i++) {
            if (this.symbols.get(i).getColor().equals(color)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Moves to a symbol as a result of a spin and notifies the symbols.
     */
    private void land(int newIndex) {
        this.symbols.get(this.currentVisibleIndex).onDeselected();
        for (Symbol s : this.symbols) {
            s.onSpin();
        }
        this.currentVisibleIndex = newIndex;
        this.symbols.get(newIndex).onSelected();
        refreshSymbol();
    }

    /**
     * Updates the shapes (color, size, marker, glow) with the selected symbol.
     */
    private void refreshSymbol() {
        Symbol current = getCurrentSymbol();
        if (current != null) {
            this.symbolShape.changeColor(current.getColor());
            this.symbolShape.changeSize(current.getSize());
            int target = (Symbol.DEFAULT_SIZE - current.getSize()) / 2;
            int delta = target - this.appliedOffset;
            if (delta != 0) {
                this.symbolShape.moveHorizontal(delta);
                this.symbolShape.moveVertical(delta);
                this.appliedOffset = target;
            }
            if (current.getBadgeColor() != null) {
                this.badge.changeColor(current.getBadgeColor());
            }
        }
        redrawLayers();
    }

    /**
     * Restores the proper shape layering on canvas and shows or hides the
     * symbol, its glow and its marker.
     */
    private void redrawLayers() {
        if (!this.isVisible) {
            return;
        }
        Symbol current = getCurrentSymbol();
        boolean showSymbol = current != null && current.isVisible();

        this.innerReel.makeInvisible();
        this.innerReel.makeVisible();

        this.halo.makeInvisible();
        if (showSymbol && current.isGlowing()) {
            this.halo.makeVisible();
        }
        this.symbolShape.makeInvisible();
        if (showSymbol) {
            this.symbolShape.makeVisible();
        }
        this.badge.makeInvisible();
        if (current != null && current.getBadgeColor() != null) {
            this.badge.makeVisible();
        }
    }
}
