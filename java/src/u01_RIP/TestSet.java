// Tobias Hernandez Perez, 5CN

package u01_RIP;

import java.util.*;

/**
 * Eine Klasse zum testen von Set und Map
 * @author Tobias Hernandez Perez
 */
public class TestSet {
    /**
     * Fügt sIP drei Werte hinzu und gibt die größe sowie dessen Inhalt aus.
     * @param sIP IPAdress Set
     */
    private static void testSet(Set<IPAddress> sIP) {
        sIP.add(new IPAddress("10.0.0.1"));
        sIP.add(new IPAddress("10.0.0.2"));
        sIP.add(new IPAddress("10.0.0.1"));

        System.out.println("sIP.size() = " + sIP.size());

        for (IPAddress ip : sIP) {
            System.out.println("ip = " + ip);
        }
    }

    /**
     * Gibt die länge der Zeit aus, die benötigt wird um Werte einzutrage, sowie um diese zu vergleichen.
     * @param subnet Subnet
     * @param sIP IPAddress Set
     */
    public static void testFullSubnet(Subnet subnet, Set<IPAddress> sIP) {
        long t0 = System.currentTimeMillis();
        sIP = Set.of(subnet.getAllIpsInNetwork());
        Set notContained = Set.of(subnet.getNextSubnet());

        System.out.println("Eintragzeit: " + (System.currentTimeMillis() - t0) + "ms");

        long t1 = System.currentTimeMillis();
        for (IPAddress i: subnet.getAllIpsInNetwork()) {
            sIP.contains(i);
        }
        for (IPAddress i: subnet.getAllIpsInNetwork()) {
            notContained.contains(i);
        }
        System.out.println("Vergleichszeit: " + (System.currentTimeMillis() - t1) + "ms");
        System.out.println();
    }

    /**
     * Fügt fünf Werte zu nextHop hinzu und iterriert darüber.
     * @param nextHop Map: Key = Subnet, Value = IPAddress
     */
    public static void testNextHop(Map<Subnet, IPAddress> nextHop) {
        Subnet subnet = new Subnet("10.0.0.0/8");

        nextHop.put(subnet, new IPAddress("10.0.0.1"));
        nextHop.put(new Subnet("172.16.0.0/16"), new IPAddress("172.16.0.1"));
        nextHop.put(new Subnet("192.168.2.0/24"), new IPAddress("192.168.2.1"));
        nextHop.put(new Subnet("127.0.0.0/30"), new IPAddress("127.0.0.1"));
        nextHop.put(new Subnet("194.1.10.0/31"), new IPAddress("194.1.10.1"));

        System.out.println(nextHop.toString());

        System.out.println(nextHop.get(subnet));
        System.out.println(nextHop.get(new Subnet("1.1.1.1/32")));
    }

    public static void main(String[] args) {

        testSet(new HashSet<IPAddress>());
        testSet(new TreeSet<IPAddress>());

        System.out.println();

        System.out.println("HashSet: ");
        testFullSubnet(new Subnet("192.168.1.0/30"), new HashSet<IPAddress>());
        System.out.println("TreeSet: ");
        testFullSubnet(new Subnet("192.168.1.0/30"), new TreeSet<IPAddress>());

        System.out.println();

        System.out.println("HashSet: ");
        testFullSubnet(new Subnet("192.168.1.0/24"), new HashSet<IPAddress>());
        System.out.println("TreeSet: ");
        testFullSubnet(new Subnet("192.168.1.0/24"), new TreeSet<IPAddress>());

        System.out.println();

        System.out.println("HashSet: ");
        testFullSubnet(new Subnet("192.168.1.0/20"), new HashSet<IPAddress>());
        System.out.println("TreeSet: ");
        testFullSubnet(new Subnet("192.168.1.0/20"), new TreeSet<IPAddress>());

        System.out.println();

        System.out.println("HashSet: ");
        testFullSubnet(new Subnet("192.168.1.0/16"), new HashSet<IPAddress>());
        System.out.println("TreeSet: ");
        testFullSubnet(new Subnet("192.168.1.0/16"), new TreeSet<IPAddress>());

        System.out.println();

        System.out.println("HashSet: ");
        testFullSubnet(new Subnet("192.168.1.0/8"), new HashSet<IPAddress>());
        System.out.println("TreeSet: ");
        testFullSubnet(new Subnet("192.168.1.0/8"), new TreeSet<IPAddress>());

        Map<Subnet, IPAddress> next0 = new TreeMap<>(new MySubnetComparator());

        testNextHop(next0);

        TreeMap<Subnet, IPAddress> next1 = new TreeMap<>(new MySubnetComparator());
        IPAddress ip = new IPAddress("10.1.1.1");

        next1.put(new Subnet("172.16.0.0/24"), ip);
        next1.put(new Subnet("192.168.0.0/16"), ip);
        next1.put(new Subnet("127.0.0.0/8"), ip);

        testNextHop(next1);
    }

    /**
     * Subklasse damit Maps funktionieren.
     */
    static class MySubnetComparator implements Comparator<Subnet> {
        @Override
        public int compare(Subnet o1, Subnet o2) {
            return -1;
        }
    }
}
