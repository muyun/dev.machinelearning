#include "error.h"

#ifndef DATABASE_H
#define DATABASE_H

#define MAX_DIGIT	10

int dataBaseInit();
int getMaxTransLength();
int getMaxItem();
int searchItemSupport(IN int *ItemSet,IN int length,OUT double* frequency);

typedef struct data_line{
	int data;
	data_line * next;
}dataLine;

#endif
