import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creates wheels from their type name.
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public final class WheelFactory {
    private static final Map<String, Wheel> PROTOTYPES = new LinkedHashMap<>();

    static {
        register("normal", new NormalWheel());
        register("lefty", new LeftyWheel());
        register("rebel", new RebelWheel());
    }

    private WheelFactory() {
    }

    /**
     * Registers (or replaces) a wheel type.
     *
     * @param type      name of the type
     * @param prototype base wheel of that type
     */
    public static void register(String type, Wheel prototype) {
        PROTOTYPES.put(type.toLowerCase(), prototype);
    }

    /**
     * Tells whether a type is known.
     *
     * @param type name of the type
     * @return true if it is registered
     */
    public static boolean isRegistered(String type) {
        return type != null && PROTOTYPES.containsKey(type.toLowerCase());
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
        return PROTOTYPES.get(type.toLowerCase()).createNew();
    }
}
