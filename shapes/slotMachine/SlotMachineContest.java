import java.util.ArrayList;
import java.util.List;

/**
 * Contest solver and simulator for the Slot Machine problem.
 */
public class SlotMachineContest {

    private static boolean isVisible = false;

    /**
     * Solves a standard slot machine and returns the sequence of winning moves.
     * 
     * @param n Number of wheels and symbols
     * @return Array of actions where each row is {wheel, steps}
     */
    public static int[][] solve(int n) {
        SlotMachine machine = new SlotMachine(n);
        return solve(machine, n);
    }

    /**
     * Solves a custom slot machine supporting specialized wheels and symbols.
     * 
     * @param machine The pre-configured SlotMachine instance
     * @param n Number of wheels and symbols
     * @return Array of actions where each row is {wheel, steps}
     */
    public static int[][] solve(SlotMachine machine, int n) {
        List<int[]> actions = new ArrayList<>();

        if (isVisible) {
            machine.makeVisible();
        }

        if (machine.distinctSymbols() == 1) {
            return new int[0][0];
        }

        optimizeWheelSymbols(machine, n, actions);

        boolean[] mark = new boolean[n + 1];
        int[] wheelBySymbol = new int[n];
        mapWheelsBySymbol(machine, n, actions, mark, wheelBySymbol);

        finalizeAlignment(machine, n, actions, wheelBySymbol);

        return actions.toArray(new int[actions.size()][2]);
    }

    /**
     * Optimizes the symbols across the wheels to maximize distinct symbols.
     */
    private static void optimizeWheelSymbols(SlotMachine machine, int n, List<int[]> actions) {
        for (int i = 2; i <= n; i++) {
            if (machine.distinctSymbols() == n) {
                break;
            }

            int maxDistinct = machine.distinctSymbols();
            int bestStep = 0;

            for (int k = 1; k <= n; k++) {
                machine.spin(i, 1);
                actions.add(new int[]{i, 1});

                int current = machine.distinctSymbols();
                if (current > maxDistinct) {
                    maxDistinct = current;
                    bestStep = k;
                }
            }

            if (bestStep > 0) {
                machine.spin(i, bestStep);
                actions.add(new int[]{i, bestStep});
            }
        }
    }

    /**
     * Maps each wheel to its corresponding symbol position.
     */
    private static void mapWheelsBySymbol(SlotMachine machine, int n, List<int[]> actions, 
                                        boolean[] mark, int[] wheelBySymbol) {
        for (int s = 1; s <= n - 1; s++) {
            machine.spin(1, 1);
            actions.add(new int[]{1, 1});

            for (int w = 2; w <= n; w++) {
                if (!mark[w]) {
                    machine.spin(w, n - 1);
                    actions.add(new int[]{w, n - 1});

                    if (machine.distinctSymbols() == n) {
                        mark[w] = true;
                        wheelBySymbol[s] = w;
                        break;
                    } else {
                        machine.spin(w, 1);
                        actions.add(new int[]{w, 1});
                    }
                }
            }
        }
    }

    /**
     * Finalizes the alignment steps needed to reach the jackpot state.
     */
    private static void finalizeAlignment(SlotMachine machine, int n, List<int[]> actions, int[] wheelBySymbol) {
        for (int s = 1; s <= n - 1; s++) {
            int w = wheelBySymbol[s];
            int steps = n - s;
            machine.spin(w, steps);
            actions.add(new int[]{w, steps});
        }
    }

    /**
     * Simulates the solving process visually for a standard machine.
     * 
     * @param n Number of wheels and symbols
     */
    public static void simulate(int n) {
        isVisible = true;
        solve(n);
        isVisible = false;
    }

    /**
     * Simulates the solving process visually for a custom machine configuration.
     * 
     * @param machine The pre-configured SlotMachine instance
     * @param n Number of wheels and symbols
     */
    public static void simulate(SlotMachine machine, int n) {
        isVisible = true;
        solve(machine, n);
        isVisible = false;
    }
}