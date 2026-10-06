/**
 * Two acceptance scenarios ready for the presentation. Run {@link #main}
 * with the machine visible; each scenario shows one requirement of cycle 4.
 * <ul>
 * <li>Scenario 1 - wheel types: lefty copies its left neighbor and rebel
 *     refuses to be locked, swapped or deleted.</li>
 * <li>Scenario 2 - symbol types: ephemeral shrinks, shy toggles, neon glows.</li>
 * </ul>
 *
 * @author Santiago Cordoba - Camilo Rivas
 * @version 4.0
 */
public class AcceptanceDemo {

    /**
     * Runs both scenarios.
     *
     * @param args not used
     * @throws InterruptedException if the pause is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        wheelTypes();
        symbolTypes();
    }

    /** Scenario 1: lefty and rebel wheels. */
    public static void wheelTypes() throws InterruptedException {
        SlotMachine m = new SlotMachine();
        m.addWheel("normal", 1);
        m.addWheel("lefty", 2);
        m.addWheel("rebel", 3);
        for (int w = 1; w <= 3; w++) {
            m.addSymbol(w, "red");
            m.addSymbol(w, "blue");
            m.addSymbol(w, "green");
        }
        m.makeVisible();
        m.spin(new String[] {"red", "green", "blue"});
        pause();
        m.spin(1, 1);
        m.spin(2, 1);
        System.out.println("Lefty copia a la izquierda: " + java.util.Arrays.toString(m.configuration()));
        pause();
        System.out.println("lock(3) = " + m.lock(3) + ", ok = " + m.ok() + " (rebel no se bloquea)");
        m.exit();
    }

    /** Scenario 2: ephemeral, shy and neon symbols. */
    public static void symbolTypes() throws InterruptedException {
        SlotMachine m = new SlotMachine();
        for (int w = 1; w <= 3; w++) {
            m.addWheel(w);
        }
        m.addSymbol("ephemeral", 1, "red");
        m.addSymbol("normal", 1, "blue");
        m.addSymbol("shy", 2, "yellow");
        m.addSymbol("normal", 2, "blue");
        m.addSymbol("neon", 3, "green");
        m.addSymbol("normal", 3, "blue");
        m.makeVisible();
        for (int i = 0; i < 6; i++) {
            m.spin();
            pause();
        }
        m.exit();
    }

    private static void pause() throws InterruptedException {
        Thread.sleep(1200);
    }
}
