//: genRules.h
// rule generation of the Apriori algorithm
#ifndef GENRULES_H
#define GENRULES_H
#include <stdio.h>
#include <iostream>
#include <set>
#include <algorithm>
#include <string>
typedef unsigned int unit;
typedef unsigned int DataType;
typedef std::set<DataType> IntSet;
typedef std::set<IntSet> SuperSet;
typedef IntSet::const_iterator IntSetCI;
typedef SuperSet::const_iterator SuperSetCI;

#endif // GENRULES_H ///:~
//#define MIN_CON 0.9;
void display(IntSet s);
void display(SuperSet ss);
IntSet allButLast(IntSet s);
IntSet last(IntSet s);
SuperSet genSubset(IntSet s);
void candidateGen(SuperSet ss,IntSet s );
bool isConfidence(IntSet ss, IntSet s);

class Matrix
{
    bool **mat;
    unit r,c,x,y;
public:
    Matrix(unit r,unit c);
    ~Matrix();
     void getData();
    
    void display();

    unit  fetchValue(unit x,unit y);
    unit  rnum();
    unit  cnum();
    
};








    
