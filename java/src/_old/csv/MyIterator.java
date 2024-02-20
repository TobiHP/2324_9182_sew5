package _old.csv;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Tobias Hernandez Perez, 4CN
 */
public class MyIterator implements Iterator<String[]> {
    CSVFileReader csvFileReader;
    csv.CSVReader csvReader;

    /**
     * Constructor for the Iterator
     *
     * @param csvFileReader the File Reader
     * @param csvReader     the CSVReader
     */
    public MyIterator(CSVFileReader csvFileReader, csv.CSVReader csvReader) {
        this.csvFileReader = csvFileReader;
        this.csvReader = csvReader;
    }

    /**
     * checks whether the CSVFileReader has a next element
     *
     * @return next element or not
     */
    @Override
    public boolean hasNext() {
        try {
            return csvFileReader.ready();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * returns a split version of the next element of the CSVFileReader
     *
     * @return next element of FileReader
     */
    @Override
    public String[] next() {
        try {
            return csvReader.read(csvFileReader.readLine());
        } catch (Exception e) {
            throw new NoSuchElementException(e);
        }
    }
}
