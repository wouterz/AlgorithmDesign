import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by woute on 12/9/2016.
 */
public class Opdr3E {

    public static double[] testSet(int n) {
        double[] testSet = new double[n];
        Random random = new Random();
        for (int i = 0 ; i < n; i++) {
            testSet[i] = random.nextGaussian();
        }
        return testSet;
    }

    public static InputStream testStream(int n, double maxdist) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb.append(n + " " + maxdist + "\n");
        for (double d:Opdr3E.testSet(n)) {
            sb2.append(d + " ");
        }
        sb.append(sb2 + "\n");
        sb2 = new StringBuilder();
        for (double d:Opdr3E.testSet(n)) {
            sb2.append(d + " ");
        }
        sb.append(sb2);
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    public static void main(String[] args) {
        String sample = "4 1.0\n" +
                "1.0 2.0 2.4 3.5\n" +
                "2.0 2.1 2.0 3.2";
        InputStream is = new ByteArrayInputStream(sample.getBytes());


        try {
            System.out.println("3E.1");
            InputStream test = testStream(10000,1);
            long tic = System.nanoTime();
            System.out.println(new Opdr3E().run(test));
            long tac = System.nanoTime();
            System.out.println(tac - tic);
        }
        catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
    }

    public String solve(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] tokens = line.split(" ");
        int length = Integer.parseInt(tokens[0]);
        double maxDistance = Double.parseDouble(tokens[1]);
        double[] v1 = new double[length];
        double[] v2 = new double[length];

        double[][] D = new double[length][length];

        String vector1String = reader.readLine();
        String vector2String = reader.readLine();
        reader.close();
        String[] vector1Tokens = vector1String.split(" ");
        String[] vector2Tokens = vector2String.split(" ");
        v1[0] = Double.parseDouble(vector1Tokens[0]);
        v2[0] = Double.parseDouble(vector2Tokens[0]);
        D[0][0] = Math.abs(v1[0] - v2[0]);

        if (D[0][0] > maxDistance)
            D[0][0] = Double.POSITIVE_INFINITY;
        for (int i = 1; i < length; i++) {
            v1[i] = Double.parseDouble(vector1Tokens[i]);
            v2[i] = Double.parseDouble(vector2Tokens[i]);

            double distance = Math.abs(v1[i] - v2[0]);
            if (distance < maxDistance)
                D[i][0] = distance + D[i-1][0];
            else
                D[i][0] = Double.POSITIVE_INFINITY;

            distance = Math.abs(v1[0] - v2[i]);
            if (distance < maxDistance)
                D[0][i] = distance + D[0][i-1];
            else
                D[0][i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 1; i < length; i++) {
            for (int j = 1; j < length; j++) {
                double distance = Math.abs(v1[i] - v2[j]);
                if (distance > maxDistance) {
                    distance = Double.POSITIVE_INFINITY;
                }
                double left = D[i-1][j];
                double across = D[i-1][j-1];
                double above = D[i][j-1];
                D[i][j] = distance + Math.min(left, Math.min(across, above));
            }
        }
        return Double.toString(D[length-1][length-1]);
    }


    public static String run(InputStream in) {
        try {
            return new Opdr3E().solve(in);
        }
        catch (IOException e) {
            return "666";
        }
    }
}

