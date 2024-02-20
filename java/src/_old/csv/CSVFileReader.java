package _old.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class CSVFileReader implements Iterable<String[]>, Closeable {
    private final BufferedReader in;
    private Path path;
    private csv.CSVReader csvReader;


    /**
     * Constructor of the file reader
     *
     * @param path                  path to the csv file as string
     * @param delimiter             character which splits the csv line
     * @param doublequote           field delimiter
     * @param skipinitialwhitespace whether to use skipinitialwhitespace or not
     */
    public CSVFileReader(String path, char delimiter, char doublequote, boolean skipinitialwhitespace) throws IOException {
        this.path = Paths.get(path);
        this.csvReader = new csv.CSVReader(delimiter, doublequote, skipinitialwhitespace);
        this.in = Files.newBufferedReader(this.path, StandardCharsets.UTF_8);
    }

    /**
     * reads a line of the file and splits it
     *
     * @return split line of file
     */
    private String[] next() throws IOException {
        if (!iterator().hasNext()) {
            return null;
        }
        return iterator().next();
    }

    /**
     * Overwrites iterator
     * used to iterate over split csv lines
     *
     * @return
     */
    @Override
    public Iterator<String[]> iterator() {
//        return new csv.MyIterator(this, this.csvReader);
        return null;
    }

    /**
     * Overwrites close
     * closes the BufferedReader
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * Checks whether or not the BufferedReader has a next element
     *
     * @return whether next element or not
     * @throws IOException
     */
    public boolean ready() throws IOException {
        return in.ready();
    }

    /**
     * reads the next line of a file
     *
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        return in.readLine();
    }


    /**
     * print out the matrix
     *
     * @param args
     */
    public static void main(String[] args) {
        try (CSVFileReader matrix = new CSVFileReader("res/csv.txt", ';', '"', true)) {

            String[] firstLine = matrix.next();
            int counter = 1;

            assert firstLine != null;
            for (String[] line : matrix) {
                System.out.print(firstLine[counter] + ": ");
                for (int i = 1; i < line.length - 1; i++) {
                    if (!line[i].isBlank()) {
                        System.out.print("nach " + firstLine[i] + ":" + line[i] + ", ");
                    }
                }
                counter++;
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
