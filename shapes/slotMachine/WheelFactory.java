import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Creates wheels from their type name. New wheel types are made available with
 * {@link #register(String, Supplier)}.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public final class WheelFactory {
    private static final Map<String, Supplier<Wheel>> CREATORS = new LinkedHashMap<>();

    static {
        register("normal", Wheel::new);
        register("lefty", LeftyWheel::new);
        register("rebel", RebelWheel::new);
    }

    private WheelFactory() {
    }

    /**
     * Registers (or replaces) a wheel type.
     *
     * @param type    name of the type
     * @param creator function that builds the wheel
     */
    public static void register(String type, Supplier<Wheel> creator) {
        CREATORS.put(type.toLowerCase(), creator);
    }

    /**
     * Tells whether a type is known.
     *
     * @param type name of the type
     * @return true if it is registered
     */
    public static boolean isRegistered(String type) {
        return type != null && CREATORS.containsKey(type.toLowerCase());
    }

    /**
     * Creates a wheel.
     *
     * @param type name of the type (case insensitive)
     * @return the new wheel
     * @throws IllegalArgumentException if the type is unknown
     */
    public static Wheel create(String type) {
        if (!isRegistered(type)) {
            throw new IllegalArgumentException("Tipo de rueda desconocido: " + type);
        }
        return CREATORS.get(type.toLowerCase()).get();
    }
}
