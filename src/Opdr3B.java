import java.io.*;
import java.util.*;

/**
 * Created by woute on 12/9/2016.
 */
public class Opdr3B {

    public static void main(String[] args) {
        String sample = "4 1.0\n" +
                "1.0 2.0 2.4 3.5\n" +
                "2.0 2.1 2.0 3.2";
        InputStream is = new ByteArrayInputStream(sample.getBytes());
        String result = "1.8";
        try {
            System.out.println(new Opdr3B().run(is));
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
        for (int i = 0; i < length; i++) {
            v1[i] = Double.parseDouble(vector1Tokens[i]);
        }
        for (int i = 0; i < length; i++) {
            v2[i] = Double.parseDouble(vector2Tokens[i]);
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == length-1 && j ==length-1)
                    D[i][j] = Math.abs(v1[i]-v2[j]) + D[i-1][j-1];
                else if (i == 0 && j == 0) {
                    D[i][j] = Math.abs(v1[i] - v2[j]);
                }
                else if (i == 0) {
                    D[i][j] = Math.abs(v1[i]-v2[j]) + D[0][j-1];
                }
                else if (j == 0) {
                    D[i][j] = Math.abs(v1[i]-v2[j]) + D[i-1][0];
                }
                else {
                    if (j == 3) {
                        D[i][j] = //Math.min(
//                                Math.abs(v1[i] - v2[j]) + D[i - 1][j - 1],
                                        Math.abs(v1[i + 1] - v2[j]) + D[i][j - 1];
//                        );
                    }
                    else if (i == 3) {
                        D[i][j] = //Math.min(
//                                Math.abs(v1[i] - v2[j]) + D[i - 1][j - 1],
                                        Math.abs(v1[i] - v2[j + 1]) + D[i - 1][j];
//                        );
                    }
                    else {
                        double aDist = Math.abs(v1[i] - v2[j]);
                        double bDist = Math.abs(v1[i + 1] - v2[j]);
                        double cDist = Math.abs(v1[i] - v2[j + 1]);
                        double a = aDist + D[i - 1][j - 1];
                        double b = bDist + D[i][j - 1];
                        double c = cDist + D[i - 1][j];
                        double min = Math.min(a, Math.min(b, c));
                        if (min == a)
                            if (aDist > maxDistance)
                                continue;

                        if (min == b)
                            if (bDist > maxDistance)
                                continue;

                        if (min == c)
                            if (cDist > maxDistance)
                                continue;
                        D[i][j] = min;
                    }
                }
            }
        }
        return Double.toString(D[length-1][length-1]);
    }


    public static String run(InputStream in) {
        try {
            return new Opdr3B().solve(in);
        }
        catch (IOException e) {
            return "666";
        }
    }
}

