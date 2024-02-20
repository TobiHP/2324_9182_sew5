// Tobias Hernandez Perez, 5CN

package whp2023;

/**
 * @author Tobias Hernandez Perez
 */
public class FlipFlop extends Component {
    public static final int S = 0;
    public static final int R = 1;
    public static final int Q = 0;
    public static final int NOT_Q = 1;

    /**
     * FlipFlop-Constructor
     * calls calc so the output is correct from the get go
     * @param name Name of the flip-flop
     */
    public FlipFlop(String name) {
        super(name,2, 2);
        calc();
    }

    /**
     * Calculates the states of the output-ports
     */
    @Override
    public void calc() {
        State state = super.getOutputPortState(Q) ? State.ONE : State.ZERO;

        state = state.handleInput(super.in[S], super.in[R]);
        super.out[Q] = state == State.ONE;
        super.out[NOT_Q] = !super.out[Q];
    }

    /**
     * State Machine of the FlipFlop
     */
    private enum State {
        ZERO {
            @Override
            State handleInput(boolean s, boolean r) {
                if (s && !r) {
                    return ONE;
                }
                return ZERO;
            }
        },
        ONE {
            @Override
            State handleInput(boolean s, boolean r) {
                if (!s && r) {
                    return ZERO;
                }
                return ONE;
            }
        };

        /**
         * handles the flipflop-states
         * @param s set-input
         * @param r reset-input
         * @return new state
         */
        abstract State handleInput(boolean s, boolean r);
    }
}
