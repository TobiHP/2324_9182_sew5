//package csv;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CSVReaderTest {
//    @Test
//    public void testSingleEmptyString() {
//        assertArrayEquals(new CSVReader().read(""), new String[]{""});
//        assertArrayEquals(new CSVReader().read(" "), new String[]{""});
//        assertArrayEquals(new CSVReader().read("     "), new String[]{""});
//    }
//
//    @Test
//    public void testMultiEmptyString() {
//        assertArrayEquals(new CSVReader().read(","), new String[]{"", ""});
//        assertArrayEquals(new CSVReader().read("         ,"), new String[]{"", ""});
//        assertArrayEquals(new CSVReader().read(",      "), new String[]{"", ""});
//    }
//
//    @Test
//    public void testSingleChars() {
//        assertArrayEquals(new CSVReader().read("a"), new String[]{"a"});
//        assertArrayEquals(new CSVReader().read("      a"), new String[]{"a"});
//        assertArrayEquals(new CSVReader().read("a      "), new String[]{"a      "});
//        assertArrayEquals(new CSVReader().read("     a      "), new String[]{"a      "});
//        assertArrayEquals(new CSVReader().read("a,b"), new String[]{"a", "b"});
//        assertArrayEquals(new CSVReader().read("a,b,    c"), new String[]{"a", "b", "c"});
//    }
//
//    @Test
//    public void testBasicStrings() {
//        assertArrayEquals(new CSVReader().read("abc, de"), new String[]{"abc", "de"});
//        assertArrayEquals(new CSVReader().read("a,b,c,d,e"), new String[]{"a", "b", "c", "d", "e"});
//        assertArrayEquals(new CSVReader().read("Hallo, Du Wappler"), new String[]{"Hallo", "Du Wappler"});
//    }
//
//    @Test
//    public void testAdvancedStrings() {
//        assertArrayEquals(new CSVReader().read("\"abc, de\""), new String[]{"abc, de"});
//        assertArrayEquals(new CSVReader().read("abc, de\""), new String[]{"abc", "de\""});
//        assertArrayEquals(new CSVReader().read("a\"b\"c, de\""), new String[]{"a\"b\"c", "de\""});
//        assertArrayEquals(new CSVReader().read("\"a,b,\"\"c,d,e\""), new String[]{"a,b,\"c,d,e"});
//        assertArrayEquals(new CSVReader().read("\"Hallo\", \"Du Wappler\""), new String[]{"Hallo", "Du Wappler"});
//    }
//
//    @Test
//    public void testExceptions() {
//        assertThrows(IllegalArgumentException.class, () -> new CSVReader().read("\"abc"));
//        assertThrows(IllegalArgumentException.class, () -> new CSVReader().read("\"abc\"a"));
//        assertThrows(IllegalArgumentException.class, () -> new CSVReader().read("abc,    \""));
//    }
//}