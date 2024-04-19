package u08_Steganographie;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PngSteganographie4pupil {
    /**
     * Wandelt ein byte in einen Bit-String um.
     * Bsp. toBinString((byte)0xaf): 1010.1111
     *
     * @param n ist das Byte, das umgewandelt wird
     * @return der Bit-String ist das umgewandelte Byte
     */
    public static String toBinString(byte n) {
        // Hier ist kein Code zu verändern
        String bits = "00000000" + Integer.toBinaryString(n);
        bits = bits.substring(bits.length() - 8);
        return bits.substring(0,4) + "." + bits.substring(4,8);
    }

    /**
     * Wandelt einen int-Wert in einen Bit-String um.
     * Bsp toBinString((0xabcd_ef98): 1100.1101  1100.1101  1110.1111  1001.1000
     *
     * @param n ist das int, das umgewandelt wird
     * @return der Bit-String ist das umgewandelte int
     */
    public static String toBinString(int n) {
        // Hier ist kein Code zu verändern
        String bits = "00000000000000000000000000000000" + Integer.toBinaryString(n);
        bits = bits.substring(bits.length() - 32);

        return bits.substring(0,4) + "." + bits.substring(4,8) + "  " +
                bits.substring(8,12) + "." + bits.substring(12,16) + "  " +
                bits.substring(16,20) + "." + bits.substring(20,24) + "  " +
                bits.substring(24,28) + "." + bits.substring(28,32);
    }

    /**
     * Wandelt Pixel aus einem Image in einen Bit-String um. Jedes Pixel besteht
     * aus 4 Bytes, die einzelnen Pixel werden durch einen senkrechten Strich
     * voneinander getrennt.
     * Beispiel: 1111.1111  1101.0010  1101.0010  1101.0010 | 1111.1111  1101.0010  1101.0010  1101.0010
     *
     * @param img das Bild, aus dem die Pixel ausgelesen werden.
     *
     * @param offset die Pixel-Positon des 1. Pixels (die Pixel werden von links oben
     *               nach rechts unten durchnummeriert)
     * @param length die Anzahl der Pixel, die ausgelesen werden sollen.
     * @return der Bit-String
     */
    public static String toBinString(BufferedImage img, int offset, int length) {
        // Hier ist kein Code zu verändern
        if (length <= 0) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        int width = img.getWidth();

        for (int pos = offset; pos < offset+length; pos++) {
            b.append(" | ").append(toBinString(img.getRGB(pos % width, pos / width)));
        }
        return b.substring(3);
    }

    /**
     * Liest ein PNG-File ein.
     *
     * @param filename der Pfad + Dateiname des PNG-Files
     * @return das PNG-Bild
     */
    public static BufferedImage readPngFile(String filename) throws IOException {
        return ImageIO.read(new File(filename));
    }

    /**
     * Schreibt ein PNG-Bild in eine Datei.
     *
     * @param filename der Pfad + Dateiname des PNG-Files
     * @param img das PNG-Bild
     */
    public static void writePngFile(String filename, BufferedImage img) throws IOException {
        // TODO: ergänze den Code
    }

    /**
     * Setzt ein Nibble (= 4 Bit = HalfByte) ins Pixel an der Stelle x,y.
     * Es wird jeweils ein Bit vom Nibble in das niederwertigste Bit von Alpha, r, g, oder b gesetzt.
     *
     * Ist z.B. das Pixel (argb) 0xAABBCCDD = binär: 1010.1010 1011.1011 1100.1100 1101.1101
     * und das Nibble 0x08 = binär. 1000, dann ist der neue Pixelwert:
     * binär: 1010.1011 1011.1010 1100.1100 1101.1100
     * neu gesetzt:   ^         ^         ^         ^
     *
     * @param img das Bild, in dem das Nibble versteckt wird
     * @param pixelPos die Pixel-Position (die Pixel werden von links oben
     *                 nach rechts unten durchnummeriert)
     * @param nibble das nibble, das versteckt werden soll.
     */
    public static void setNibble(BufferedImage img, int pixelPos, byte nibble) {
        int x = pixelPos % img.getWidth();
        int y = pixelPos / img.getWidth();
        int pixel = img.getRGB(x, y);

        pixel = ((nibble & 8) << 21) | (pixel & 0xfeffffff);    // alpha
        pixel = ((nibble & 4) << 14) | (pixel & 0xfffeffff);    // red
        pixel = ((nibble & 2) << 7) | (pixel & 0xfffffeff);     // green
        pixel = (nibble & 1) | (pixel & 0xfffffffe);            // blue

        img.setRGB(x, y, pixel);
    }


    /**
     * Liest aus dem PNG-Bild aus dem Pixel x, y ein Nibble aus.
     * Genaue Erklärung, siehe {@link #setNibble(BufferedImage, int, byte)}
     *
     * @param img das Bild, in dem das Nibble versteckt wird
     * @param pixelPos die Pixel-Position (die Pixel werden von links oben
     *                 nach rechts unten durchnummeriert)
     * @return das ausgelesene Nibble
     */
    public static byte getNibble(BufferedImage img, int pixelPos) {
        int x = pixelPos % img.getWidth();
        int y = pixelPos / img.getWidth();
        int pixel = img.getRGB(x, y);

        int nibble = (pixel >>> 21) & 0x00000008;
        nibble |= (pixel >>> 14) & 0x00000004;
        nibble |= (pixel >>> 7) & 0x00000002;
        nibble |= (pixel) & 0x00000001;

        return (byte) nibble;
    }

    /**
     * Speichert die beiden Nibble des Bytes b in das Bild, dazu werden 2 Pixel benötigt.
     *
     * @param img das Bild, in dem das Byte b versteckt wird
     * @param pixelPos die Pixel-Position für das höherwertige Nibble von b
     *                (die Pixel werden von links oben nach rechts unten durchnummeriert)
     * @param b das Byte, das versteckt werden soll
     */
    public static void setByte(BufferedImage img, int pixelPos, byte b) {
        setNibble(img, pixelPos, (byte) (b & 0x0f));
        setNibble(img, pixelPos + 1, (byte) ((b & 0xf0) >> 4));
//        int pixel = img.getRGB(pixelPos % img.getWidth(), pixelPos / img.getWidth());
//        System.out.println(pixel);
    }


    /**
     * Liest ein verstecktes Byte (= 2 Nibbles) aus einem Bild.
     *
     * @param img das Bild, in dem das Byte versteckt ist
     * @param pixelPos  die Pixel-Position für das höherwertige Nibble von b
     *                (die Pixel werden von links oben nach rechts unten durchnummeriert)
     * @return das versteckte Byte
     */
    public static byte getByte(BufferedImage img, int pixelPos) {
        return (byte) ((getNibble(img, pixelPos+1) << 4) | getNibble(img, pixelPos));
    }


    /**
     * Versteckt ein ganzes Byte-Array in einem Bild.
     * Zuerst wird die Länge des Byte-Arrays info als int-Zahl gespeichert,
     * danach das eigentliche Byte-Array info, mit dem Byte mit dem Index 0 beginnend.
     * Tipp: verwende die schon vorhandenen Methoden.
     *
     * @param img das Bild, in dem alles versteckt wird
     * @param info das Byte-Array, das versteckt wird
     */
    public static void setByteArray(BufferedImage img, byte[] info) {
        setByte(img, 0, (byte) info.length);

        for (int i = 1; i < info.length; i++) {
            setByte(img, i, info[i+1]);
        }
    }


    /**
     * Extrahiert das versteckte Byte-Array aus dem Bild.
     *
     * Zuerst ist die Länge des Byte-Arrays info als int-Zahl gespeichert,
     * danach das eigentliche Byte-Array info, mit dem Byte mit dem Index 0 beginnend.
     * Tipp: verwende die schon vorhandenen Methoden.
     *
     * @param img das Bild mit dem versteckten Byte-Array
     * @return das versteckte Byte-Array
     */
    public static byte[] getByteArray(BufferedImage img) {
        // TODO Code ergänzen
        return new byte[0];
    }

    /**
     * Extrahiert aus einem PNG-Bild das versteckte Byte-Array
     *
     * @param filename der Dateiname samt Pfad zum Bild
     * @return das versteckte Byte-Array
     * @throws IOException bei Lesefehlern
     */
    public static byte[] readSteganographieBytesFromPngFile(String filename) throws IOException {
        // TODO Code ergänzen
        return new byte[0];
    }

    /**
     * Versteckt ein Byte-Array in einer PNG-Datei und speichert sie
     *
     * @param filename Dateiname inkl. Pfad zur Ausgabedatei
     * @param img  das Bild, in dem das Byte-Array versteckt wird
     * @param info das Byte-Array, das versteckt werden soll
     * @throws IOException bei Schreibfehlern
     */
    public static void writeSteganographieBytesToPngFile(
            String filename, BufferedImage img, byte info[]) throws IOException {
        // TODO Code ergänzen
    }

    public static void main(String[] args) throws IOException {
        // Testcode von BRE, ergänze/verändere ihn, wie du willst
        BufferedImage img = readPngFile("res/steganographie/img.png");

        System.out.println("Pixel:      " + toBinString(img, 1, 2));
        setByte(img, 1, (byte)254);
        System.out.println("New Pixel:  " + toBinString(img, 1, 2));
        System.out.println(getByte(img, 1)&255);

        setByteArray(img, "abcd".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(getByteArray(img), StandardCharsets.UTF_8));
//
//        writeSteganographieBytesToPngFile("resources/img.geheim.png", img,
//                "Viel Erfolg bei der SEW-Matura!".getBytes(StandardCharsets.UTF_8));
//
//        System.out.println(new String(readSteganographieBytesFromPngFile("resources/img.geheim.png"), StandardCharsets.UTF_8));
    }
}
