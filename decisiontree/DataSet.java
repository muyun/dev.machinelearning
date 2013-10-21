package decisiontree;

import java.util.ArrayList;
/*
 * @author wenlong
 * the class deals with the data set 
*/

public class DataSet {
	private static int numAttr; //  num of attributes
	private  int numData;	// num of records
	private static int[] numAttrVal = new int[UtilityI.MAXATTR]; //the count of attribute value
	String recordBuff[];	// Store the record values.
	static ArrayList<String[]> attributeList = new ArrayList<String[]>(); //the list of attributes, their name and values


	public static int[] getNumAttrVal() {
		return numAttrVal;
	}

	public static void setNumAttrVal(int[] numAttrVal) {
		DataSet.numAttrVal = numAttrVal;
	}

	public int getNumRec() {
		return numData;
	}
	public  void setNumRec(int noRec) {
		numData = noRec;
	}

	//Insert the record. 
	public String[] insertRecord(String record)
	{
		if(record != null)
		{
			recordBuff = record.split(",");
			return recordBuff;
            
		}else
			return null;

	}
    
    //Set the required attributes 
	public static void setAttributes()
	{
		attributeList.add(UtilityI.TOP_LEFT.split(","));
		attributeList.add(UtilityI.TOP_MIDDLE.split(","));
		attributeList.add(UtilityI.TOP_RIGHT.split(","));
		attributeList.add(UtilityI.MIDDLE_LEFT.split(","));
		attributeList.add(UtilityI.MIDDLE_MIDDLE.split(","));	

		attributeList.add(UtilityI.CLASS_LABEL.split(","));

		//the size of attribute in the given test data
		numAttr = attributeList.size();

		//num of attribute values for each attribute in each records.
		for(int i=0 ; i<UtilityI.MAXATTR; i++)
			numAttrVal[i] = UtilityI.MAXATTRVAL;
	}

    //creates a branch of the tree. 
	public ArrayList<String[]> branch(ArrayList<Integer> position , ArrayList<String[]> data)
	{
		int n = UtilityI.MAXATTR;

		int recsize = position.size();
		ArrayList<String[]> subset = new ArrayList<String[]>();

		for(int i=0 ; i<UtilityI.MAXATTR; i++)
			numAttrVal[i] = UtilityI.MAXATTRVAL;

		int val;
		for(int i=0;i<recsize;i++)
			for(int j=0;j<n;j++)
			{
				val = ((Integer)position.get(i)).intValue();
				subset.add(((String[])data.get(val)));				
			}	

		return subset;
	}

	public static ArrayList<String[]> getAttributes()
	{
		return attributeList;
	}

	public static int getNumAttr()
	{
		return numAttr;
	}

}
