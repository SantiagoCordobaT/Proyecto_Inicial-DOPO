import java.util.ArrayList;
import java.util.List;

/**
 * Contest solver and simulator for the Slot Machine problem.
 */
public class SlotMachineContest {

    private static boolean isVisible = false;

    /**
     * Solves the slot machine and returns the sequence of winning moves.
     * 
     * @param n Number of wheels and symbols
     * @return Array of actions where each row is {wheel, steps}
     */
    public static int[][] solve(int n) {
        List<int[]> actions = new ArrayList<>();

        SlotMachine machine = new SlotMachine(n);
        
        if (isVisible) {
            machine.makeVisible();
        }

        if (machine.distinctSymbols() == 1) {
            return new int[0][0];
        }

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

        boolean[] mark = new boolean[n + 1];
        int[] wheelBySymbol = new int[n];

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

        for (int s = 1; s <= n - 1; s++) {
            int w = wheelBySymbol[s];
            int steps = n - s;
            machine.spin(w, steps);
            actions.add(new int[]{w, steps});
        }

        return actions.toArray(new int[actions.size()][2]);
    }

    /**
     * Simulates the solving process visually by executing solve in visible mode.
     * 
     * @param n Number of wheels and symbols
     */
    public static void simulate(int n) {
        isVisible = true;
        solve(n);
        isVisible = false;
    }
}