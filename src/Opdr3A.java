import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by woute on 12/5/2016.
 */
public class Opdr3A {

    public static void main(String[] args) throws IOException {
        String sample = "5 4 2\n" +
                "1.0 2.0 2.4 3.5\n" +
                "2.0 2.1 2.0 3.2\n" +
                "1.4 2.8 2.4 3.2\n" +
                "3.1 2.3 2.3 1.3\n" +
                "2.0 2.0 2.4 2.1  ";
        InputStream is = new ByteArrayInputStream(sample.getBytes());
        String result = "3.1231";
        run(is);
    }

    public static double distance(UFNode n1, UFNode n2) {
        Iterator<Double> locations_n1 = n1.location.iterator();
        Iterator<Double> locations_n2 = n2.location.iterator();
        int sum = 0;
        while (locations_n1.hasNext()) {
            Double x = locations_n1.next() - locations_n2.next();
            sum += (x*x);
        }
        return Math.sqrt(sum);
    }

    public static String run(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] info = line.split(" ");
        int vectors = Integer.parseInt(info[0]);
        int dimensions = Integer.parseInt(info[1]);
        int clusters = Integer.parseInt(info[2]);
        TreeSet<UFNode> graph = new TreeSet<UFNode>();
        for (int i = 0; i < vectors; i++) {
            line = reader.readLine();
            info = line.split(" ");
            UFNode node = new UFNode(i);
            for (int d = 0; d < dimensions; d++) {
                node.location.add(Double.parseDouble(info[d]));
            }
            graph.add(node);
        }

        
        return "0";
    }
}

/**
 * Objects that implement a Union-Find datastructure.
 */
class UFNode {
    int id;
    int rank;
    UFNode parent;

    ArrayList<Double> location;

    /**
     * Constructor
     * @param id so that the node can store some information
     */
    public UFNode(int id) {
        this.id = id;
        this.rank = 0;
        this.parent = this;
        location = new ArrayList<Double>();
    }

    /**
     * Implements the `find` operation for the Union-Find datastructure
     * @return the root of the tree that this UFNode is connected to (or this if it is the root).
     */
    public UFNode findSet() {
        if (this.parent != this) {
            this.parent = this.parent.findSet();
        }
        return this.parent;
    }

    /**
     * Merges two trees in the Union-Find datastructure
     * @param x a node in the first tree
     * @param y a node in the second tree
     */
    public static void union(UFNode x, UFNode y) {
        UFNode xRoot = x.findSet();
        UFNode yRoot = y.findSet();

        if (xRoot.id == yRoot.id) {
            return;
        }
        if (xRoot.rank < yRoot.rank){
            xRoot.parent = yRoot;
        } else if (xRoot.rank > yRoot.rank) {
            yRoot.parent = xRoot;
        } else {
            yRoot.parent = xRoot;
            xRoot.rank++;
        }

    }
}//
