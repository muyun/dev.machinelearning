//DataOutput.java
//generate the data source
//
import java.io.PrintWriter;
import java.io.FileWriter; //write data to a file; buffer the output by wrapping it in a BufferedWriter
import java.io.BufferedWriter;

import java.util.Random;
import java.io.IOException;
import java.lang.String;


public class DataOutput {
    static String file = "DataOutput2.out";

    public static Random rand = new Random(47);
    public static Random rand1 = new Random();
    
    public char next(){
        return (char)rand.nextInt(5);
    }
    
    public static void main(String[] args) throws IOException {
        
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        int lineCount = 0;
        for(int i=0; i<50; i++)
            out.println(lineCount++ + " ," + rand.nextInt(100) + " ," + rand1.nextInt(2000));

        out.close();

        //System.out.println();
    }
    
}


