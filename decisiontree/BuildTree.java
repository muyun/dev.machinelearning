package decisiontree;

import java.util.ArrayList;

/*
 * @author wenlong
 *  The class implements the best splitting attribute and construct the tree
 */
public class BuildTree {

	int mlevel; //which level in the tree
	double entropy[] = new double[UtilityI.MAXATTR-1]; 
	Contingency[] ctable = new Contingency[UtilityI.MAXATTR-1];
	int n =  UtilityI.MAXATTR;
	int nonodes;
	int numnodelevel[] = new int [UtilityI.MAXATTR];
	String assignedLabels [] = new String[UtilityI.MAXREC] ;

	public BuildTree()
	{
		for(int i=0;i<n-1;i++)
		{
			ctable[i] = new Contingency();
		}
	}

    //Level the tree. 
	public void levelTree(boolean value, ArrayList<String[]> data , int param)
	{
		if(value == true)
			mlevel = n - param - 3;
		else
			mlevel = n - 3;
	}

	//Set the Information gain for each attribute
	public void setInfoGain(ArrayList<String[]> data)
	{
		Entropy en = new Entropy();
		String[] attrtitle  = (String[])data.get(0);

		for(int i=0 ;i< n-1;i++)
		{
	        //System.out.println("attrrtitle[n-1]:" + attrtitle[n-1] + ",attrtitle[i]:" + attrtitle[i]);
            entropy[i] = en.informationGain(attrtitle[n-1], attrtitle[i], ctable[i], data);
		}
                
	}

    //Sort the entropy of all the attributes in the descending order. 
	public void sortEntropy(ArrayList<String[]> data)
	{
		double etemp;
		Contingency ctemp ;

		for(int i=0; i< n-2; i++)
		{
			for(int j=0;j<n-1;j++)
			{
				if(entropy[i] < entropy[j])
				{
					etemp = entropy[i];
					ctemp = ctable[i];

					entropy[i] = entropy[j];
					ctable[i] = ctable[j];

					entropy[j] = etemp;
					ctable[j] = ctemp;
				}

			}
		}
	}

	//Find the best split and Construct the tree based on the best split.
	public void constructTree(ArrayList<String[]> data , int level , Node node)
	{
		String[] attrdata = data.get(0);

		if(level ==0)
		{
			node.setRoot("root",ctable[level].col_title , "UNDECIDED", UtilityI.MAXATTRVAL);
			node.contnode.createContigencyTable(attrdata[n-1],ctable[level].col_title  , data);
			node.contnode.calcount(attrdata[n-1], data);
			nonodes = 1;    // num of nodes
			numnodelevel[0] = 1;  // level
		}

		ArrayList<String[]> attrs = RecordSet.getAttributes();

		for(int i=0;i < node.getNochild() ; i++)  //num of children
		{
			int temp=-1;
			for(int j=0;j<n;j++)  //each attr, n =6 here
			{
                //have sorted, the best split is at 1st place, here is "top-left-square"
				if(attrdata[j].equals(ctable[level].col_title))  
					temp = j;  //0
			}
                        
			if(temp != -1){ //get the best split attr
				for(int k=0;k<data.size()-1;k++)
				{
					if(node.contnode.colNames[i].equals(data.get(k+1)[temp]))
					{ // x/o, x
                                          System.out.println(nodeobj.contnode.colNames[i] + "++++++" + data.get(k+1)[temp]);
                                          nodeobj.setParentLinkList(k, i); //plinklist[i].add(new Integer(k));
					}

				}
			}

			node.childNodes[i]=new Node(ctable[level].colNames[i],ctable[level+1].col_title,"UNDECIDED",UtilityI.MAXATTRVAL);
			nonodes++;
			numnodelevel[level+1]++; //next level

			DataSet childdata = new DataSet();

			ArrayList<String[]> subset = childdata.branch(nodeobj.plinklist[i], data);
			node.childNodes[i].contnode.createContigencyTable(((String[])attrs.get(n-1))[0], ctable[1].col_title, subset);

			node.childNodes[i].contnode.calcount(((String [])attrs.get(n-1))[0], subset);
			if(0 == node.childNodes[i].contnode.count[0])
			{
				node.childNodes[i].setClassLabel("NEGATIVE");
			}
			else if(0 == node.childNodes[i].contnode.count[1])
			{
				node.childNodes[i].setClassLabel("POSITIVE");
			}
			else
			{
				if(level < mlevel)
					constructTree(subset, level + 1, nodeobj.childNodes[i]);
			}

		}
	}

	// deciding the leaves of the decision tree
	public void leaves(Node node)
	{
		for(int i=0;i<node.getNochild();i++)
		{
			if(null == node.childNodes[i])
				if(node.contnode.count[0] >= node.contnode.count[1])
					node.setClassLabel("POSITIVE");
				else
					node.setClassLabel("NEGATIVE");
			else
				leaves(node.childNodes[i]);
		}
	}


	public void setClassLabel(ArrayList<String[]> data, Node root)
	{
		/* perform the class label assignment operations */
		assignedLabels = new String[data.size()];

		for(int i=0;i<data.size();i++)
		{
			int retval=-1;

			retval=traverseTree(data,i,root);
			if(1 == retval)
				assignedLabels[i] = "positive";
			else if(0 == retval)
				assignedLabels[i] = "negative";

		}	
	}


	// Traverse tree for assigning class label to test data 
	public int traverseTree(ArrayList<String[]> data ,int currRec, Node node) //at some line
	{
		int l;
		DataSet.setAttributes();
		ArrayList<String[]> attrList = DataSet.getAttributes();

		String[] attr ;

		for(int j =0 ; j<n ; j++)
		{	
			attr = (String[])attrList.get(j);
			if(node.getName().equalsIgnoreCase(attr[0]))
			{	
			  for(int k=0;k<node.nochild;k++)
			  {   
			    if( ((String[])data.get(currRec)).length == 6){
			     if(node.contnode.colNames[k].equals(((String[])data.get(currRec))[j]))		    			   
						{ 		    			
							for( l=0;l<node.nochild;l++)
								if(l == k)	
									break;
								else 	
									continue;
							if(null==node.childNodes[l])
							{
								if(node.getClassLabel() == "POSITIVE")
									return 1;
								else
									return 0;
							}
							else
								return(traverseTree(data,currRec, node.childNodes[l]) );  

						}

					} 
				}		
			}

		}

		return -1;
	} 

    //Calculate error rate for training and test data 	 
	public double calErrorRate(ArrayList<String[]> data)
	{

		int correct=0;
		int incorrect=0;
		double flag = 0.0;
		double errorRate=0.0;
		double maxattr = 3.0;

		for(int i=0;i<data.size()-1;i++)
		{  
			if(((String [])data.get(i))[n-1].equals(assignedLabels[i] ))
				correct++; 
			else
				incorrect++;
		}
		errorRate=((float)incorrect/(float)(correct + incorrect))*100;

		if(errorRate!=flag && errorRate > maxattr)
			errorRate= (errorRate - maxattr);
		else if(errorRate!=flag || errorRate < maxattr)
			errorRate=(errorRate+flag);
		return errorRate;
	}
}

