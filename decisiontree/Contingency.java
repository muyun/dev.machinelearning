package decisiontree;

import java.util.ArrayList;

/**
 * @author nirlepa
 * updated by wenlong
 *
 * This class implements the contingency table of the attributes whose conditional 
 * entropy needs to be calculated. It also maintains the information about the row
 * attribute and the column attribute. The values associated with the row and columnn 
 * attribute.
*/
public class Contingency {

	String row_title;  // row title of the attribute
	String col_title;  // column title of the attribute

	String rowValues[] = new String[UtilityI.MAXATTRVAL]; // row values of attribute
	String colValues[] = new String[UtilityI.MAXATTRVAL]; // column values of attribute
	String names[] = new String[UtilityI.MAXATTRVAL];

	int contingency_Table[][] = new int[UtilityI.MAXATTRVAL][UtilityI.MAXATTRVAL]; // stores contingency table 
	int[] count = new int [UtilityI.MAXATTRVAL]; // count of attribute values in table for some attr

	public int[][] getContingency_Table() {
		return contingency_Table;
	}
    
	public void setContingency_Table(int[][] contingencyTable) {
		contingency_Table = contingencyTable;
	}
    
	public int[] getCount() {
		return count;
	}
    
	public void setCount(int[] count) {
		this.count = count;
	}

	// * Contingency(): initialize the variables.
	public Contingency()
	{
		row_title="";
		col_title = "";
	}

    /*
	 * Function creates contingency table
	 * @param rowValue: row value
	 * @param colValue: column value
	 * @param data: data set from which table is created.
	 */
	public void createContigencyTable(String rowValue, String colValue, ArrayList<String[]> data )	
	{

		String[] attr;
		int rowindex=-1;
		int colindex=-1;

		DataSet.setAttributes();
		ArrayList<String[]> attrList = DataSet.getAttributes();

		String[] attrnames = UtilityI.ATTRNAME;

		for(int i=0;i < attrnames.length;i++) //get the attrname
		{
			if(rowValue.equalsIgnoreCase(attrnames[i]))
			{  
				row_title = rowValue;
				rowindex = i;    //row_title:Class-Label,rowindex:5
			}
            
			if(colValue.equalsIgnoreCase(attrnames[i]))
			{

				col_title = colValue;
				colindex = i;	    //col_title:top-middle-square,colindex:1	
			}
		}

        //System.out.println("row_title:" + row_title + ",rowindex:" + rowindex);
        //System.out.println("col_title:" + col_title + ",colindex:" + colindex);
		if(rowindex != -1 && colindex != -1){

			int[] numAttrVal = DataSet.getNumAttrVal();  //3
			attr = (String[])attrList.get(rowindex);   //Class-Label positive negative 

            for(int i=0;i<numAttrVal[rowindex];i++)
			{
			   	rowValues[i] = attr[i+1];  //,positive,negative, defined by the interface of UtilityI
                
			}

			attr = (String[])attrList.get(colindex);
            for(int i=0;i<numAttrVal[colindex];i++)
			{
			    colValues[i] = attr[i+1]; // x,o,b,
                
			}

			String[] rec;
			for(int i=0;i<data.size();i++)
			{
				rec = data.get(i);
				for(int j =0; j<UtilityI.MAXATTRVAL;j++){
					for(int k =0; k<UtilityI.MAXATTRVAL;k++){
						if(rec[rowindex].equals(rowValues[j]) && rec[colindex].equals(colValues[k]))
                            ////is negative/positive and is o/x/b?
							contingency_Table[j][k]++;
					}
				}

			}
		}	
	}


	/**
	 * count the attribute values in the table 
	 * @param title: name of the attribute
	 * @param data : data set
	 */
	public void calcount(String title, ArrayList<String[]> data)
	{
		int rowindex = -1;
		String[] attrnames = UtilityI.ATTRNAME;
		DataSet.setAttributes();
		ArrayList<String[]> attrList = DataSet.getAttributes();

		for(int i=0;i<attrnames.length;i++) //get the name of attr
		{
			if(title.equalsIgnoreCase(attrnames[i]))
			{  
				col_title = title;  //get the column title of the attribute
				rowindex = i;  //the column index of the attribute, here rowindex=5
			}
		}
        
		int[] numAttrVal = DataSet.getNumAttrVal();
		if(rowindex != -1){

			String[] attr = (String[])attrList.get(rowindex);
			for(int i=0;i<numAttrVal[rowindex];i++)
			{
				names[i] = attr[i+1];  //remove the attritute name "Class-Label" on the 1st line

			}
		}

		String[] rec;
		for(int i=1;i<data.size();i++)  //should be from i=1, because of the attribute name on 1st line
		{
			rec = data.get(i);
                       
			for(int j =0; j<UtilityI.MAXATTRVAL;j++){						
				if(rec[rowindex].equals(names[j]) ){  //negative **** positive
                    //System.out.println(rec[rowindex] + "****" + names[j]);
					count[j]++;
                   }
                                                   
			}
                   
		}
	}
    
	public void displayTable()
	{
		for(int i=0;i<UtilityI.MAXATTRVAL;i++)
		{   
			System.out.println(" ");
			for(int j=0;j<UtilityI.MAXATTRVAL;j++)
			{
				System.out.print("  "+ contingency_Table[i][j]);
			}
		}
        System.out.println();
	}
}
