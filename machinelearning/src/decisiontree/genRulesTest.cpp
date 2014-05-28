//: genRulesTest.cpp
// Test of genRules
#include "genRules.h"
//#include "Apriori.h"
//#include "dataBase.h"
using namespace std;

int main()
{
    //from Li Yi
    // MaxTransLength = getMaxTransLength();
    
    // for ( int k = 2; k <= MaxTransLength; k++)
    // {      
        // for each  layer l
    // for(struct itemset* Lk_temp=(L_pts+k-1)->next;Lk_temp!=NULL;Lk_temp=Lk_temp->next)
    // { todo:
       //here Lk_temp is the trans, we add a count to get the trans?
      //items begin add one from 2 to K
     
     //}

       // I assume the following trans and items for the test
    unit trans=3,items=3;
     // SuperSet like l2={{1,3},{2,5}} or l3 = {{2,4,5},{1,3,4},{2,3,4}}
             
      // get the dataSet
    // std::cout<<"Input:...\n";

        IntSet temp;
        SuperSet lx;
         
      Matrix db(trans,items);
        db.getData();
       db.display();

      for (int i=0;i<db.rnum();i++)
       {
           for(int j=0;j<db.cnum();j++)
           {
               int k=db.fetchValue(i,j);
               temp.insert(k);

               // std::cout<<k<<" ";
               
           }
           
           // display(temp);
           // std::cout<<"\n---------"<<std::endl;
                    
           SuperSet lxsubset=genSubset(temp);
           
           // display(lxsubset);
 
           std::cout<<"\nDisplaying ...\n";

           candidateGen(lxsubset,temp);
                                 
           lx.insert(temp);
           temp.clear();
           
       }
       
        //}
         
}
