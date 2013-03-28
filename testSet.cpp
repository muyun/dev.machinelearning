#include <iostream>
#include <string>
#include <sstream>
#include <algorithm>
#include <set>
using namespace std;

int main()
{
    int cas, a, b;
    string str1, str2;

    // cin>>cas;
    // getchar();

    //    while(cas--)
        //    {
        getline(cin,str1);
        stringstream ss(str1);
        set<int> s1,s2,s3,s4,s5;

        while(ss>>a)
        {
            s1.insert(a);
            
        }
        
        getline(cin,str2);
        stringstream ss2(str2);
        while(ss2>>b)
        {
            s2.insert(b);
        }
        //    }

    set_intersection(s1.begin(),s1.end(),s2.begin(),s2.end(),inserter(s3,s3.begin()));
    set_union(s1.begin(),s1.end(),s2.begin(),s2.end(),inserter(s4,s4.begin()));
    set_difference(s1.begin(),s1.end(),s2.begin(),s2.end(),inserter(s5,s5.begin()));
     set<int>::const_iterator ii;
     for(ii= s3.begin();ii != s3.end(); ii++)
     {
         if (ii == s3.begin())
         {
             cout<< *ii;
             
         } else 
         {
             cout <<" "<<*ii;
             
         }
         cout<< endl;
                  
     }

     for(ii = s4.begin(); ii != s4.end();ii++) 
      {
          if (ii == s4.begin())
          {
              cout << *ii;
              
          }else 
          {
              cout <<" "<<*ii;
              
          }
       cout<<endl;
          
      }

      for(ii = s5.begin(); ii!= s5.end();ii++) 
      {
      if (ii == s5.begin())
      {
      cout<< *ii;
      }else
      {
      cout<<" "<<*ii;
                           
      }
      cout<<endl;
      }
                   
  return 0;
                   
}
