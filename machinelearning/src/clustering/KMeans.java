package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenlong
 */
public class KMeans {

        int k;
        Vector[] patterns;
        MembershipMatrix mm;
        
        public KMeans(int k, Vector... patterns) {
                this.k = k;
                this.patterns = patterns;
                mm = new MembershipMatrix(patterns.length, k);
        }
        
        public KMeans(int k, int dims, String filename, String delimiter) {
                this.k = k;
                
                try {
                        BufferedReader in = new BufferedReader(new FileReader(filename));
                        List<Vector> patternList = new ArrayList<Vector>();
                        for (String line = in.readLine(); line != null; line = in.readLine()) {
                                String[] strValues = line.split(delimiter);
                                double[] values = new double[dims];
                                for (int i = 0; i < dims && i < strValues.length; i++) {
                                        values[i] = Double.valueOf(strValues[i]);
                                }
                                patternList.add(new Vector(values));
                        }
                        patterns = new Vector[patternList.size()];
                        for (int i = 0; i < patternList.size(); i++) {
                                patterns[i] = patternList.get(i);
                        }
                        
                } catch (Exception e) {
                        e.printStackTrace();
                }
                
                mm = new MembershipMatrix(patterns.length, k);
        }
        
        /**
         * returns an array of integers, the ith elements represents the cluster 
         * index of the ith pattern
         */
        public List<Integer> partition() {
                List<Vector> newZ = new ArrayList<Vector>();
                for (int i = 0; i < k; i++) { //choose the first two point as the centroid firstly
                        newZ.add(patterns[i]); 
                }
                
                List<Vector> oldZ;
                
                while(true) {
                        oldZ = newZ;
                        
                        // for each pattern, find the cluster center that is closest
                        // to that pattern, and then put that pattern into that cluster
                        int[] clusterIndexes = new int[patterns.length];
                        for (int i = 0; i < patterns.length; i++) {
                                int nearestCluster = patterns[i].getNearestPointIndex(newZ);
                                clusterIndexes[i] = nearestCluster;
                        }
                        mm.moveAllPatterns(clusterIndexes);
                        
                        newZ = calculateZ();
                        
                        if (newZ.equals(oldZ))
                                break;
                }
                
                
                return mm.getClusters();        
        }
        
        /**
         * calculates cluster centers
         */
        private List<Vector> calculateZ() {
                List<Vector> z = new ArrayList<Vector>();
                for (int j = 0; j < k; j++) {
                        int[] patternIndexes = mm.getPatterns(j);
                        z.add(Vector.calculateCenter(getPatternsWithIndexes(patternIndexes)));
                }
                
                return z;
        }
        
        private Vector[] getPatternsWithIndexes(int[] indexes) {
                Vector[] patterns = new Vector[indexes.length];
                for (int i = 0; i < patterns.length; i++) {
                        patterns[i] = this.patterns[indexes[i]];
                }
                
                return patterns;
        }
        
        public String printResults() {
                StringBuilder sb = new StringBuilder("");
                sb.append("pattern \t\t cluster" + "\n");
                
                List<Integer> clusters = mm.getClusters();
                for (int i = 0; i < patterns.length; i++) {
                        sb.append(patterns[i] + "\t" + (clusters.get(i) + 1) + "\n");
                }
                
                return sb.toString();
        }
        
        public static void main(String[] args) { 
        	   //need to give the longitude of the dataset
                KMeans kmeans = new KMeans(3, 2, "/Users/wenlongzhao/workspace/proj/dev.machinelearning/machinelearning/src/clustering/Gaussian.txt", ",");
                List<Integer> clusters = kmeans.partition();
                System.out.println(kmeans.printResults());
        }
}

