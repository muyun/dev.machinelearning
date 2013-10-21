package decisiontree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;	
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author wenlong
 * This class implements the decision tree classification
 */

public class DecisionTree {

	private static final String TRAININGFILE="training.txt";
	private static final String TESTFILE = "test.txt";

	public static void main(String args[])
	{
		if(args.length < 2)
		{
			System.out.println(" Please Enter the Training Data and Test Data File Name");
			System.exit(1);
		}

		File trainingData = new File(args[0]);
		//File trainingData = new File(TRAININGFILE);

		if(!trainingData.exists())
		{
			System.err.print(" NO such File "+TRAININGFILE+" found");
			System.exit(1);
		}

		File testData = new File(args[1]);
		//File testData = new File(TESTFILE);

		if(!testData.exists())
		{
			System.err.print(" NO such File "+TESTFILE+" found");
			System.exit(1);			
		}

        //get the data
		try{ 
			BufferedReader readTrainingData = new BufferedReader( new FileReader(trainingData));
			BufferedReader readTestData = new BufferedReader( new FileReader(testData));
			String trainingLine;
			String testLine;
			DataSet  dataSet = new DataSet();
            
			ArrayList<String[]> trainingRecord = new ArrayList<String[]>(); 
			ArrayList<String[]> testRecord = new ArrayList<String[]>();

			while(true)
			{
				trainingLine = readTrainingData.readLine();
				if(trainingLine != null)
				{
					if(dataSet.insertRecord(trainingLine) != null)
						trainingRecord.add(dataSet.insertRecord(trainingLine));
				}else
					break;
			}

			while(true)
			{
				testLine = readTestData.readLine();
				if(testLine != null)
				{
					testRecord.add(dataSet.insertRecord(testLine));
				}else
					break;
			}
            
			trainingRecord.add(0, UtilityI.ATTRNAME);  //add UtilityI.ATTRNAME at 1st position

            System.out.println("*****The data:*****");
            for (String [] s : trainingRecord) {
                for(String str : s){
                    System.out.print(str + " ");
                }
                System.out.println();
            }    

			DataSet dataTrain = new DataSet();
			dataTrain.setNumRec(trainingRecord.size());
			DataSet dataTest = new DataSet();
			dataTest.setNumRec(testRecord.size());

			readTrainingData.close();
			readTestData.close();

			// setup the standard
			BuildTree trainTree = new BuildTree();
			Node root = new Node();

               //calculate the info gain for each attr H(D)
			trainTree.setInfoGain(trainingRecord); 
			trainTree.sortEntropy(trainingRecord);  //sort 
			trainTree.levelTree(false, trainingRecord, 0);  //mlevel=0

            //construt the tree
			trainTree.constructTree(trainingRecord, 0 , root);
			trainTree.leaves(root);  //the leaves of the decision tree is 'NEGATIVE' or 'POSITIVE'
			trainTree.setClassLabel(trainingRecord, root);
            
			//double errorRate =  trainTree.calErrorRate(trainingRecord);
			//System.out.println("Training Error Rate : "+errorRate);

			//the test part
			trainTree.setClassLabel(testRecord, root);
            
			double testerror = trainTree.calErrorRate(testRecord);

            System.out.println("*********************");
            System.out.println("Test Error Rate : "+testerror);

		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}

	}

	public static void displayRecord(ArrayList<String[]> list)
	{
		String buff[];
		for(int i=0; i<list.size();i++)
		{
			buff = (String[])list.get(i);
			System.out.println(" Record No: "+(i+1));
			for(int j=0; j <buff.length ; j++)
				System.out.println("Record Value : "+buff[j]);
		}
	}

}

