// Tobias Hernandez Perez, 4CN

package csv;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Tobias Hernandez Perez
 */
public class CSVReader {
    private ArrayList<String> out;
    private String cell;

    private final char delimiter;
    private final char doublequote;
    private final boolean skipinitialwhitespace;


    /**
     * Default Constructor
     * sets the delimiter to ,
     * sets the doublequote to "
     * sets skipinitialwhitespace to true
     */
    public CSVReader() {
        this.delimiter = ',';
        this.doublequote = '"';
        this.skipinitialwhitespace = true;
    }

    /**
     * Constructor for the CSVReader
     *
     * @param delimiter             the character to split with
     * @param doublequote           field delimiter
     * @param skipinitialwhitespace whether to skip whitespaces in the beginning
     */
    public CSVReader(char delimiter, char doublequote, boolean skipinitialwhitespace) {
        this.delimiter = delimiter;
        this.doublequote = doublequote;
        this.skipinitialwhitespace = skipinitialwhitespace;
    }

    /**
     * states of the CSVReader
     */
    private enum State {
        NOCELL {
            @Override
            State handleChar(char ch, CSVReader context) {
                if (Character.isWhitespace(ch)) {
                    return NOCELL;
                }

                if (ch == context.delimiter) {
                    context.out.add(context.cell);
                    context.cell = "";
                    return NOCELL;
                } else if (ch == context.doublequote) {
                    return INSTRING;
                } else {
                    context.cell += ch;
                    return INCELL;
                }
            }
        },

        INCELL {
            @Override
            State handleChar(char ch, CSVReader context) {
                if (ch == context.delimiter) {
                    context.out.add(context.cell);
                    context.cell = "";
                    return NOCELL;
                }
                context.cell += ch;
                return INCELL;
            }
        },

        INSTRING {
            @Override
            State handleChar(char ch, CSVReader context) {
                if (ch == context.doublequote) {
                    return POSTSTRING;
                }
                context.cell += ch;
                return INSTRING;
            }
        },

        POSTSTRING {
            @Override
            State handleChar(char ch, CSVReader context) {
                if (Character.isWhitespace(ch)) {
                    return POSTSTRING;
                }

                if (ch == context.delimiter) {
                    context.out.add(context.cell);
                    context.cell = "";
                    return NOCELL;
                } else if (ch == context.doublequote) {
                    context.cell += "\"";
                    return INSTRING;
                } else {
                    throw new IllegalArgumentException("Illegal character after String");
                }
            }
        };

        /**
         * abstract method to handle single characters
         *
         * @param ch      Character
         * @param context the CSVReader
         * @return the state of the state machine after the current character
         */
        abstract State handleChar(char ch, CSVReader context);
    }


    /**
     * reads a CSV-String and converts it to an Array
     *
     * @param s CSV
     * @return CSV as String Array
     */
    public String[] read(String s) {
        State state = State.NOCELL;
        out = new ArrayList<>();
        cell = "";

        s += delimiter;
        for (char c : s.toCharArray()) {
            state = state.handleChar(c, this);
        }

        if (state == State.INSTRING) {
            throw new IllegalArgumentException("String wasn't closed!");
        }

        return out.toArray(new String[0]);
    }

    public static void main(String[] args) {
        CSVReader reader = new CSVReader(';', '"', true);

        System.out.println(Arrays.toString(reader.read("\"Test\"    ; hi;     sup")));
//        System.out.println(Arrays.toString(reader.read("\"Test\"    , hi,     sup")));
//        System.out.println(Arrays.toString(reader.read("\"ab,,,,,,c\"    ,hi,,,,,,     sup")));
//
//        try {
//            System.out.println(Arrays.toString(reader.read("\"Test\"\"    , hi,     sup")));
//        } catch (IllegalArgumentException e) {
//            System.out.println("Working as intended");
//        }

    }
}
