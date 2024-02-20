// Tobias Hernandez Perez, 5CN

package u01_RIP;

import java.util.Objects;

public class IPAddress implements Comparable<IPAddress> {
    private int ip;

    public final static IPAddress LOCALHOST = new IPAddress();
    public final static IPAddress MODEM = new IPAddress("10.0.0.138");


    /**
     * Standardkonstruktor, setzt ip auf 127.0.0.1 (Loopback)
     */
    public IPAddress() {
        setIp("127.0.0.1");
    }

    /**
     * Setzt this auf ip
     *
     * @param ip als Integer
     */
    public IPAddress(int ip) {
        setIp(ip);
    }

    /**
     * Setzt this auf eine ip bestehend aus vier Oktetten
     *
     * @param o3 viertes Oktett
     * @param o2 drittes Oktett
     * @param o1 zweites Oktett
     * @param o0 erstes Oktett
     */
    public IPAddress(int o3, int o2, int o1, int o0) {
        setIp(o3, o2, o1, o0);
    }

    /**
     * Nutzt ein Array zum setzen der ip
     *
     * @param ip als int Array
     */
    public IPAddress(int[] ip) {
        setIp(ip);
    }

    /**
     * Nutzt einen String zum Setzen der ip
     *
     * @param ip als String
     */
    public IPAddress(String ip) {
        setIp(ip);
    }


    /**
     * Setzt this auf ip
     * IllegalArgumentException, falls ein Oktett zu groß/klein ist.
     *
     * @param ip als Integer
     */
    public void setIp(int ip) {
        int tempIp = ip;

        for (int i = 0; i < 4; i++) {
            if ((tempIp & 0xFF) < 0 || (tempIp & 0xFF) > 255) {
                throw new IllegalArgumentException("Octet has to be between 0 and 255");
            }

            tempIp >>= 8;
        }

        this.ip = ip;
    }

    /**
     * Setzt this auf eine ip bestehend aus vier Oktetten
     * IllegalArgumentException, falls ein Oktett zu groß/klein ist.
     *
     * @param o3 viertes oktet
     * @param o2 drittes oktet
     * @param o1 zweites oktet
     * @param o0 erstes oktet
     */
    public void setIp(int o3, int o2, int o1, int o0) {
        int ip = 0;

        if (o0 < 0 || o0 > 255 || o1 < 0 || o1 > 255 || o2 < 0 || o2 > 255 || o3 < 0 || o3 > 255) {
            throw new IllegalArgumentException("Octet has to be between 0 and 255");
        }

        ip = (ip << 8) | o3;
        ip = (ip << 8) | o2;
        ip = (ip << 8) | o1;
        ip = (ip << 8) | o0;

        setIp(ip);
    }

    /**
     * Nutzt ein Array zum setzen der ip
     * IllegalArgumentException, falls nicht genau vier Oktette vorhanden sind.
     *
     * @param ip als int Array
     */
    public void setIp(int[] ip) {
        if (ip.length != 4) {
            throw new IllegalArgumentException("IP has to have four octets");
        }

        setIp(ip[0], ip[1], ip[2], ip[3]);
    }

    /**
     * Nutzt einen String zum setzen der ip
     * IllegalArgumentException, falls nicht genau vier Oktette vorhanden sind.
     * IllegalArgumentException, falls ein Oktett zu groß/klein ist.
     * NumberFormatException, falls ein Oktett etwas anderes als Zahlen enthält.
     *
     * @param ip als String
     */
    public void setIp(String ip) {
        String[] sOctets = ip.split("\\.");

        if (sOctets.length != 4) {
            throw new IllegalArgumentException("IP has to have four octets");
        }

        int intIp = 0;

        for (String sOctet : sOctets) {
            int octet = 0;

            try {
                octet = Integer.parseInt(sOctet);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Octets have to be between 0 and 255");
            }

            if (octet < 0 || octet > 255) {
                throw new IllegalArgumentException("Octets have to be between 0 and 255");
            }

            intIp = (intIp << 8) | octet;
        }

        setIp(intIp);
    }


    /**
     * Gibt Ip als Int zurück
     *
     * @return Ip als Int
     */
    public int getAsInt() {
        return ip;
    }

    /**
     * Gibt das num-te Oktett zurück
     *
     * @param num Oktett welches zurückgegeben werden soll.
     * @return num-tes Oktett
     */
    public int getOctet(int num) {
        if (num < 0 || num > 4) {
            throw new ArrayIndexOutOfBoundsException("num has to be between 1 and 4");
        }

        return getAsArray()[num];
    }

    /**
     * Gibt ip als Array zurück
     *
     * @return ip als Array
     */
    public int[] getAsArray() {
        int ip = this.ip;
        int[] ipArray = new int[4];

        for (int i = ipArray.length - 1; i >= 0; i--) {
            ipArray[i] = (255 & ip);
            ip = ip >> 8;
        }

        return ipArray;
    }


    /**
     * Überschreibt die toString Methode
     *
     * @return ip als DDN
     */
    @Override
    public String toString() {
        String sIp = "";
        int ip = this.ip;

        for (int i = 0; i <= 3; i++) {
            if (i < 1) {
                sIp = (255 & ip) + sIp;
                ip = ip >> 8;
            } else {
                sIp = (255 & ip) + "." + sIp;
                ip = ip >> 8;
            }
        }

        return sIp;
    }

    /**
     * Überschreibt die equals Methode
     *
     * @param o zu vergleichendes Objekt
     * @return this == o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPAddress ue06IpAddress = (IPAddress) o;
        return ip == ue06IpAddress.ip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public int compareTo(IPAddress o) {
        return 0;
    }
}
