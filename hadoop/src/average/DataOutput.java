//DataOutput.java
//generate the data source
//
package average;

import java.io.PrintWriter;
import java.io.FileWriter; //write data to a file; buffer the output by wrapping it in a BufferedWriter
import java.io.BufferedWriter;
import java.io.File;

import java.util.Random;
import java.io.IOException;
import java.lang.String;


public class DataOutput {
    //static String file = "DataOutput.out";

    public static Random rand = new Random(47);
    public static Random rand1 = new Random();
    
    public char next(){
        return (char)rand.nextInt(5);
    }
    
    public static void main(String[] args) throws IOException {
    	if(args.length != 1){
    		System.err.println("Usage: DataOutput <outNum>");
    		System.err.println("eg. : DataOutput 0");
    		System.exit(2);
    		
    	} 
    	
        String dir = "/Users/wenlongzhao/workspace/MapReduce/input/";
       
    	String file = dir + "DataOutput" + args[0] + ".out";
    	File datafile = new File(file);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(datafile)));

        //int lineCount = 0;
        for(int i=0; i<3000; i++)
            out.println(rand.nextInt(30) + " ," + rand1.nextInt(2000));

        out.close();

        //System.out.println();
    }
    
}


