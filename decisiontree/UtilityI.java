package decisiontree;

public interface UtilityI {

        String TOP_LEFT = "top-left"+","+"x"+","+"o"+","+"b";
        String TOP_MIDDLE="top-middle"+","+"x"+","+"o"+","+"b";
        String TOP_RIGHT= "top-right"+","+"x"+","+"o"+","+"b";
        String MIDDLE_LEFT= "middle-left"+","+"x"+","+"o"+","+"b";
        String MIDDLE_MIDDLE="middle-middle"+","+"x"+","+"o"+","+"b";
        String CLASS_LABEL= "Class-Label"+","+"positive"+","+"negative"+","+" ";
        String[] ATTRNAME = 
        {"top-left","top-middle","top-right","middle-left","middle-middle","Class-Label"};

        int MAXATTR = 6;
        int MAXATTRVAL = 3;
        int MAXREC = 1000;   //max record

}
