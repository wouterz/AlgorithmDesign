import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.util.*;

/**
 * Created by woute on 12/5/2016.
 */
public class Opdr3A {

    public static void main(String[] args) {
        String sample = "5 4 2\n" +
                "1.0 2.0 2.4 3.5\n" +
                "2.0 2.1 2.0 3.2\n" +
                "1.4 2.8 2.4 3.2\n" +
                "3.1 2.3 2.3 1.3\n" +
                "2.0 2.0 2.4 2.1  ";
        String sample2 = "2 2 1\n" +
                "1.0 4.0\n" +
                "5.0 1.0";
        InputStream is = new ByteArrayInputStream(sample.getBytes());
        String result = "3.1231";
        try {
            System.out.println(new Opdr3A().run(is));
        } catch (Exception e) {
        }
    }

    public String solve(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] info = line.split(" ");
        int vectors = Integer.parseInt(info[0]);
        int dimensions = Integer.parseInt(info[1]);
        int clusters = Integer.parseInt(info[2]);
        ArrayList<Vertex> vertices = new ArrayList<Vertex>(vectors);
        ArrayList<Edge> edges = new ArrayList<Edge>(vectors * vectors);

        //O(n*m)
        for (int i = 0; i < vectors; i++) {
            line = reader.readLine();
            info = line.split(" ");
            Vertex vertex = new Vertex();
            vertex.node = new UFNode(i);
            for (int d = 0; d < dimensions; d++) {
                vertex.coordinates.add(Double.parseDouble(info[d]));
            }
            vertices.add(vertex);
        }

        reader.close();

        //O(n*n)
        for (int i = 0; i < vectors; i++) {
            for (int j = i; j < vectors; j++) {
                if (i != j) {
                    //should be O(1)
                    edges.add(new Edge(vertices.get(i), vertices.get(j)));
                }
            }
        }

        Collections.sort(edges);

        PriorityQueue<Double> weights = new PriorityQueue<Double>(vectors, Collections.reverseOrder());
        for (Edge e : edges) {
            Vertex u = e.u;
            Vertex v = e.v;
            if (u.node.findSet() != v.node.findSet()) {
                weights.offer(e.weight);
                UFNode.union(u.node, v.node);
            }

        }

        for (int c = 1; c < clusters; c++) {
            weights.remove();
        }

        float sum = 0;

        for (double d : weights) {
            sum += d;
        }

        return Float.toString(sum);
    }


    public static String run(InputStream in) {
        try {
            return new Opdr3A().solve(in);
        } catch (IOException e) {
            return "666";
        }
    }


    class Vertex {
        ArrayList<Double> coordinates;
        UFNode node;

        public Vertex() {
            coordinates = new ArrayList<Double>();
        }

        public double distance(Vertex j) {
            Iterator<Double> coords_i = this.coordinates.iterator();
            Iterator<Double> coords_j = j.coordinates.iterator();
            double sum = 0;
            while (coords_i.hasNext()) {
                Double x = coords_i.next() - coords_j.next();
                sum += (x * x);
            }
            return Math.sqrt(sum);
        }
    }

    class Edge implements Comparable<Edge> {
        Vertex u, v;
        double weight;

        public Edge(Vertex u, Vertex v) {
            this.u = u;
            this.v = v;
            weight = u.distance(v);
        }

        public int compareTo(Edge e) {
            if (this.weight < e.weight)
                return -1;
            else if (this.weight > e.weight)
                return 1;
            else
                return 0;
        }
    }
}
/**
 * Objects that implement a Union-Find datastructure.
 */
class UFNode {
    int id;
    int rank;
    UFNode parent;

    /**
     * Constructor
     * @param id so that the node can store some information
     */
    public UFNode(int id) {
        this.id = id;
        this.rank = 0;
        this.parent = this;
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
}

