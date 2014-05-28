//: genRules.cpp
#include "genRules.h"
void display(IntSet s){
	std::cout<<'{';
	if(s.size()){
		IntSetCI i=s.begin();
		std::cout<<*(i++);
		for(;i != s.end();++i)
			std::cout<<','<<*i;
	}
	std::cout<<'}';
}
void display(SuperSet ss){
	std::cout<<'{';
	if(ss.size()){
		SuperSetCI i=ss.begin();
		display(*(i++));
		for(;i != ss.end();++i){
			std::cout<<',';
			display(*i);
		}
	}
	std::cout<<'}';
}

bool isConfidence(IntSet ss, IntSet s)
{
    double ssfrequency,sfrequency;
    
    int ssitemSet=ss.begin();
    int sslength=ss.size();
    int sitemSet=s.begin();
    int slength=s.size();
    
    double ssfreq = searchItemSupport(int &ssitemSet, int sslength, double &ssfrequency);
    double sfreq  = searchItemSupport(int &sitemSet,  int slength,  doulbe &sfrquency);

    if( sfreq/ssfreq >= MIN_CON)
    {
        return true;
        
    }

    return false;
        
}

// B = X - A
void candidateGen(SuperSet xs,IntSet x)
{
    IntSet xa,xb; // xa is like A, and xb is like B
    
    if(xs.size())  // xs is like {{2,4},{2,5},{4,5}}
    {
       for( SuperSetCI i=xs.begin();i != xs.end();++i)
        {
            IntSet xa= *i; // xa is like {2,4}
            // calculate the confidence
            //todo:int searchItemSupport(IN int *ItemSet,IN int length,OUT double* frequency);
            // if (isConfidence(xa,x))
            //  {
                
            set_difference(x.begin(),x.end(),xa.begin(),xa.end(),inserter(xb,xb.begin()));
            // for (IntSetCI j=xa.begin();j!= xa.end();++j)
            // { //IntSet xas = *j;  //xas is 2 or 4 in this case
            //     std::cout<<"xas:"<<*j<<";"<<std::endl;
                
            //  }
            
            display(xa);
            std::cout<<"=>";
            display(xb);
            std::cout<<std::endl;
            
            xb.clear();
            //  }
            
        }
                
    }
    
}


SuperSet genSubset(IntSet s){
	SuperSet ss;
	if(s.size() < 2)
		return ss;
	IntSetCI front,back;
	IntSet temp;
	front=s.begin();back=s.end();
	--back;
	temp.insert(front,back);
	ss.insert(temp);
	temp.clear();
	++front;++back;
	temp.insert(front,back);
	ss.insert(temp);
	temp.clear();
	if(s.size() == 2)
		return ss;
	back=++(s.begin());++front;
	while(front != s.end()){
		temp.insert(front,s.end());
		temp.insert(s.begin(),back);
		ss.insert(temp);
		temp.clear();
		front++;++back;
	}
	return ss;
}
IntSet allButLast(IntSet s){
	IntSet temp;
	if(s.size())
		temp.insert(s.begin(),--(s.end()));
	return temp;
}
IntSet last(IntSet s){
	IntSet temp;
	if(s.size())
		temp.insert(--(s.end()),s.end());
	return temp;
}
Matrix::Matrix(unit r,unit c)
{
    this->r=r;
    this->c=c;

    mat= new bool*[r];
    for (int i=0;i<r; i++)
        mat[i] = new bool[c];
        }

Matrix::~Matrix()
{
    for(int i=0; i<r; i++)
        delete[] mat[i];
    delete[] mat;
    
    }

unit Matrix::fetchValue(unit x,unit y)
{
    return mat[x][y];
    
}
void Matrix::getData()
{
    // mat = new bool*[r];
    
    for(int i=0;i<r;i++)
    {
        //  mat[i]= new bool[c];
        
        for(int j=0;j<c;j++)
        {
            // mat[i][j] =(i+1)*(j+1);
                                
            
            scanf("%d",&mat[i][j]);
            
        }

        std::cout<<std::endl;
        
    }
    
}

void Matrix::display()
{
    for(int i=0;i <r;i++)
    {
        std::cout<<std::endl;
        for(int j=0;j<c;j++)
        std::cout<<mat[i][j]<<' ';
    }
    
}

unit Matrix::rnum()
{
    return r;
    
}

unit Matrix::cnum()
{
    return c;
    
}


IntSet getSet(IntSet s)
{
    IntSet temp;
    if(s.size())
        temp.insert(s.begin(),s.end());
    return temp;
        
}


