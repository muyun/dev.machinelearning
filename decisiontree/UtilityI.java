package decisiontree;

public interface UtilityI {

	String TOP_LEFT_SQUARE = "top-left"+","+"x"+","+"o"+","+"b";
	String TOP_MIDDLE_SQUARE="top-middle"+","+"x"+","+"o"+","+"b";
	String TOP_RIGHT_SQUARE = "top-right"+","+"x"+","+"o"+","+"b";
	String MIDDLE_LEFT_SQUARE = "middle-left"+","+"x"+","+"o"+","+"b";
	String MIDDLE_MIDDLE_SQUARE="middle-middle"+","+"x"+","+"o"+","+"b";
	String CLASS_LABEL = "Class-Label"+","+"positive"+","+"negative"+","+" ";
	String[] ATTRNAME = 
	{"top-left-square","top-middle","top-right","middle-left","middle-middle","Class-Label"};

	int MAXATTR = 6;
	int MAXATTRVAL = 3;
	int MAXREC = 100;   //max record

}
