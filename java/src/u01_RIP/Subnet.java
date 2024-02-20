// Tobias Hernandez Perez, 5CN

package u01_RIP;

import java.util.Objects;

public class Subnet {
    private IPAddress addr;
    private IPAddress mask;

    public static final Subnet LOCALNET = new Subnet("127.0.0.1/24");
    public static final Subnet PRIVATENET10 = new Subnet("10.0.0.0/10");


    /**
     * Konstruiert ein Subnet auf Basis eines Strings
     * String muss IP-Adresse und Netzmaske enthalten
     * Bei falschem Syntax: IllegalArgumentException
     * @param net IP-Adresse/Maske als String
     */
    public Subnet(String net) {
        if (!net.contains("/")) {
            throw new IllegalArgumentException("Input must contain a mask!");
        }

        String[] netmask = net.split("\\/");

        setAddr(new IPAddress(netmask[0]));

        if (netmask[1].length() > 2) {
            setMask(new IPAddress(netmask[1]));
        } else {
            setMask(new IPAddress(-1 << (32 - Integer.parseInt(netmask[1]))));
        }
    }

    /**
     * Konstruiert ein Subnet auf Basis einer IP-Adresse (als IpAddress) und einer Maske (als int)
     * Falls die Maske invalid ist: IllegalArgumentException
     * @param addr IP-Adresse als IpAddress
     * @param mask Netzmaske als int
     */
    public Subnet(IPAddress addr, int mask) {
        if (mask < 0 || mask > 32) {
            throw new IllegalArgumentException("Mask has to be between 1 and 32");
        }

        setAddr(addr);
        setMask(new IPAddress(-1 << (32 - mask)));
    }

    /**
     * Konstruiert ein Subnet auf Basis einer IP-Adresse (als IpAddress) und einer Maske (als IpAddress)
     * @param addr IP-Adresse als IpAddress
     * @param mask Netzmaske als IpAddress
     */
    public Subnet(IPAddress addr, IPAddress mask) {
        setAddr(addr);
        setMask(mask);
    }

    /**
     * Konstruiert ein Subnet auf Basis einer IP-Adresse (als String) und einer Maske (als String)
     * @param addr IP-Adresse als String
     * @param mask Netzmaske als String
     */
    public Subnet(String addr, String mask) {
        try {
            Integer.parseInt(mask);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Mask must only contain Numbers and periods!");
        }

        if (Integer.parseInt(mask) < 0 || Integer.parseInt(mask) > 32) {
            throw new IllegalArgumentException("Mask has to be between 1 and 32!");
        }

        setAddr(new IPAddress(addr));

        if (mask.length() > 2) {
            setMask(new IPAddress(mask));
        } else {
            setMask(new IPAddress(-1 << (32 - Integer.parseInt(mask))));
        }
    }

    /**
     * Konstruiert ein (classful) Subnet auf Basis einer IP-Adresse (als IpAddress)
     * @param addr IP-Adresse als IpAddress
     */
    public Subnet(IPAddress addr) {
        setAddr(addr);

        int oct = addr.getOctet(0);
        if (oct < 127) { setMask(new IPAddress("255.0.0.0")); }
        if (oct > 127 && oct < 192) { setMask(new IPAddress("255.255.0.0")); }
        if (oct > 191 && oct < 224) { setMask(new IPAddress("255.255.255.0"));  }

        if (oct < 0 || oct > 224) { throw new IllegalArgumentException("Only between 0 and 224"); }
    }


    /**
     * Liefert die Netzadresse als IpAddress zurück
     * @return Netzadresse als IpAddress
     */
    public IPAddress getNetAddress() {
        return addr;
    }

    /**
     * Liefert die Netzmaske als IpAddress zurück
     * @return Netzmaske als IpAddress
     */
    public IPAddress getNetMask() {
        return mask;
    }

    /**
     * Liefert die Anzahl der Hosts in dem Subnet zurück
     * @return Anzahl der Hosts im Subnet
     */
    public int getNumberOfHosts() {
        return (int)Math.pow(2, 32 - toCIDR(mask));
    }

    /**
     * Liefert die Prefix-Version der Maske zurück
     * @param mask Netzmaske als IpAddress
     * @return Prefix-Version der Maske
     */
    private int toCIDR(IPAddress mask) {
        int cidr = 0;
        int intMask = mask.getAsInt();

        for (int i = 0; i < 32; i++) {
            if ((intMask & 1) == 1) {
                cidr++;
            }

            intMask >>= 1;
        }

        return cidr;
    }

    /**
     * Setzt this.addr auf addr
     * @param addr IP-Adresse als IpAddress
     */
    public void setAddr(IPAddress addr) {
        this.addr = addr;
    }

    /**
     * Setzt this.mask auf mask
     * @param mask Netzmaske als IpAddress
     */
    public void setMask(IPAddress mask) {
        this.mask = mask;
    }


    /**
     * Überschreibt die toString Methode
     * @return addr/mask (als DDN)
     */
    @Override
    public String toString() {
        return addr + "/" + mask;
    }

    /**
     * Überschreibt die equals Methode
     * @param o zu vergleichendes Objekt
     * @return this == o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subnet Subnet = (Subnet) o;
        return Objects.equals(addr, Subnet.addr) &&
                Objects.equals(mask, Subnet.mask);
    }

    /**
     * Testet ob ip in diesem Subnet ist
     * @param ip zu checkende IP-Adresse als IpAddress
     * @return ob ip in diesem Subnet ist
     */
    public boolean isInNetwork(IPAddress ip) {
        return equals(new Subnet(new IPAddress(ip.getAsInt() & mask.getAsInt()), mask));
    }


    /**
     * Liefert die Broadcast-Adresse des Subnets zurück
     * @return Broadcast-Adresse des Subnets
     */
    public IPAddress getBroadcastAddress() {
        return new IPAddress(addr.getAsInt() | getNumberOfHosts() - 1);
    }

    /**
     * Liefert die erste nutzbare IP Adresse des Subnets zurück
     * @return erste nutzbare IP Adresse des Subnets
     */
    public IPAddress getFirstIp() {
        return new IPAddress(addr.getAsInt() | 1);
    }

    /**
     * Liefert die letzte nutzbare IP Adresse des Subnets zurück
     * @return erste letzte IP Adressedes Subnets
     */
    public IPAddress getLastIp() {
        return new IPAddress(addr.getAsInt() | getNumberOfHosts() - 2);
    }

    /**
     * Liefert alle IPs im Subnet zurück
     * @return alle IPs im Subnet
     */
    public IPAddress[] getAllIpsInNetwork() {
        IPAddress[] ips = new IPAddress[getNumberOfHosts()];

        for (int i = 0; i < ips.length; i++) {
            ips[i] =  new IPAddress(addr.getAsInt() + i);
        }

        return ips;
    }

    /**
     * Liefert das nächste Subnet
     * @return nächstes Subnet
     */
    public Subnet getNextSubnet() {
        int cidr = toCIDR(mask);
        int intIp = addr.getAsInt();
        return new Subnet(new IPAddress((intIp >> 32 - cidr) + 1 << 32 - (cidr | intIp << 32 - cidr)), mask);
    }

    /**
     * Teilt das Subnet in n viele (so groß wie mögliche) Subnets
     * @param n Anzahl der Subnets
     * @return n viele (so groß wie mögliche) Subnets
     */
    public Subnet[] splitNet(int n) {
        Subnet[] ue06Subnets = new Subnet[n];
        int cidr = toCIDR(mask);

        for (int i = 0; i < n; i++) {
            ue06Subnets[i] = getNextSubnet();
            addr = new IPAddress((addr.getAsInt() >> 32 - cidr) + 1 << 32 - cidr | addr.getAsInt() << 32 - cidr);
        }
        return ue06Subnets;
    }
}
