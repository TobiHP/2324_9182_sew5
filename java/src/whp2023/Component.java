package whp2023;

/**
 * @author Tobias Hernandez Perez
 */
public abstract class Component {
    public String name;
    public boolean[] in;
    public boolean[] out;


    /**
     * Constructor for Components
     *
     * @param name Name of the Component
     */
    public Component(String name, int inLen, int outLen) {
        this.name = name;
        in = new boolean[inLen];
        out = new boolean[outLen];
    }

    /**
     * abstract method used for calculation of port states
     */
    public abstract void calc();

    /**
     * Gets the state of a given output-port
     * @param portNr number of the port
     * @return state of the port
     */
    public boolean getOutputPortState(int portNr) {
        return out[portNr];
    }

    /**
     * Sets the state of a given input-port
     * @param portNr number of the port
     * @param state state to be set
     */
    public void setInputPortState(int portNr, boolean state) {
        in[portNr] = state;
    }

    /**
     * custom toString method, used to convert a boolean array to a string
     * @param a boolean array
     * @return boolean string
     */
    private static String toString(boolean a[]) {
        char r[] = new char[a.length];

        for (int i = 0; i < a.length; i++) {
            r[i] = a[i] ? '1' : '0';
        }
        return new String(r);
    }

    /**
     * Overrides the toString method
     * e.g -> FlipFlop: FF:10/10
     * @return
     */
    @Override
    public String toString() {
        return name + ":" + toString(in) + "/" + toString(out);
    }
}