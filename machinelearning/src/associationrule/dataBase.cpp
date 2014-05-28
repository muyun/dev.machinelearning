#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "error.h"
#include "dataBase.h"
#define MASK	0xffffffff

FILE *fp=NULL;
int *pIndex=NULL;
char * pData=NULL;
int *pDataInt=NULL;
static int transCount=0;
static int maxTransLength=0;
static int maxItem=0;
static int allItemCount=0;

int getMaxTransLength()
{
	return maxTransLength;
}

int getMaxItem()
{
	return maxItem;
}
int findMaxTransLength()
{
	int i,lineCount=0,count=0;
	int *temp=pDataInt;
	
	for(i=0;i<allItemCount;i++,temp++)
	{
		if(MASK==*temp)
		{	
			lineCount++;
			if(count>maxTransLength)
			{
				maxTransLength=count;
			}	
			count=0;	
		}
		else
		{
			count++;
		}
	}
	transCount=lineCount;
	return ERR_NO_ERROR;
}
int fineMaxItem()
{
	int i;
	int *temp=pDataInt;
	for(i=0;i<allItemCount;i++,temp++)
	{
		if(maxItem<*temp)
		{
			maxItem=*temp;
		}
	}
	return ERR_NO_ERROR;
}
int getfileParam(OUT int *lineNum,OUT int*fileLengthNum)
{
	int line=1,charCount=0;
	char ch;
	int ret=0;
	
	rewind(fp);
	while((ch=fgetc(fp))!=EOF)
	{
		if(ch=='\n')
		{
			line++;
		}
		charCount++;	
	}
	*lineNum=line;
	*fileLengthNum=charCount;
	return ERR_NO_ERROR;
}

int storeFile(IN int fileLength)
{
	rewind(fp);
	fread(pData,sizeof(char),sizeof(char)*fileLength,fp);
	return ERR_NO_ERROR;
}

int insertIndex(IN int maxLine,IN int fileLength)
{
	int i,line=0;
	pIndex[line++]=(int)&pDataInt[0];
	for(i=0;i<fileLength && line<maxLine;i++)
	{
		if(pDataInt[i]=='\n')
		{
			pIndex[line++]=(int)&pDataInt[i];
		}
	}
	return ERR_NO_ERROR;
}
int serchItemCountInLine(IN int line,IN int maxLine,IN int *ItemSet,IN int length,OUT int* contain)
{
	int *temp=pIndex;
	int *tempData=NULL;
	int i,count=0;
	if(line <=0 || line >maxLine)
	{
		printf("err line Number!\n");
		return ERR_BAD_PARM;
	}
	
	tempData=(int*)temp[line-1];
	while(*tempData!=MASK)
	{
		for(i=0;i<length;i++)
		{
			if(*tempData==ItemSet[i])
			{
				count++;
			}
		}
		tempData++;
	}
	if(count==length)
	{
		*contain=1;
	}
	else
	{
		*contain=0;
	}
	return ERR_NO_ERROR;
}

int searchItemSupport(IN int *ItemSet,IN int length,OUT double* frequency)
{
	int i=1,ret;
	int contain;
	int count=0;
	
	for(i=1;i<=transCount;i++)
	{
		IF_ERR_RETURN(serchItemCountInLine(i,transCount,ItemSet,length,&contain),ret);
		if(contain)
		{
			count++;
		}
	}
	*frequency=(double)count/(double)transCount;
	return ERR_NO_ERROR;
}
int printLine(IN int line,IN int maxLine)
{
	int *temp=pIndex;
	int *tempData=NULL;
	
	if(line <=0 || line >maxLine)
	{
		printf("err line Number!\n");
		return ERR_BAD_PARM;
	}
	printf("line=%d\n",line);
	tempData=(int*)temp[line-1];
	while(*tempData!=MASK)
	{
		printf("%d ",*tempData);
		tempData++;
	}
	return ERR_NO_ERROR;
}

int printAll()
{
	int i,lineCount=1;
	int *temp=pDataInt;
	printf("\n");
	for(i=0;i<allItemCount;i++)
	{
		if(*temp==MASK)
		{	
			printf("\n");
			printf("line:%d ",lineCount);
			lineCount++;
		}
		else
		{
			printf("%d ",*temp);
		}
		temp++;
	}
}
int getNumber(int fileLength)
{
	char *tempData=pData;
	int *tempDataInt=pDataInt;
	int *tempIndex=pIndex;
	char tempNum[MAX_DIGIT]={0};
	char ch;
	int i=0,j=0,k=0;
	bool transfering=0,newLine=1;
	
	*tempIndex=(int)tempDataInt;
	while((ch=*tempData)!=EOF)
	{
		k++;
		if(k>=fileLength)
		{
			break;
		}
		switch(ch)
		{
			case ' ':
				i=0;
				break;
			case ',':
				i=0;
				newLine=0;
				if(0==transfering)
				{
					transfering=1;
					for(j=0;j<MAX_DIGIT;j++)
					{
						tempNum[j]=0;
					}
				}
				else
				{	
					*tempDataInt=atoi(tempNum);
					allItemCount++;
					tempDataInt++;
				}
				break;
			case '\n':
				i=0;
				transfering=0;
				newLine=1;
				*tempDataInt=atoi(tempNum);
				allItemCount++;
				tempDataInt++;
				*tempDataInt=MASK;
				allItemCount++;
				tempDataInt++;
				tempIndex++;
				*tempIndex=(int)tempDataInt;
				break;
			default:
				if(1==newLine)
				{
					break;
				}
				tempNum[i++]=ch;
				break;
		}		
		tempData++;
	}
	
	*tempDataInt=atoi(tempNum);
	allItemCount++;
	tempDataInt++;
	*tempDataInt=MASK;
	allItemCount++;
	return ERR_NO_ERROR;
}
int loadFile()
{
	
	int ret,ch,lineCount,fileLength=0;
	int i,temp;
	fp=fopen("asso.csv","r");
	if(NULL == fp)
	{
		printf("Can not open asso.csv\r\nPlease check it!\r\n");
		return ERR_OPEN_FILE_FAILED;
	}
	if(NULL != pData)
	{
		printf("The file has been load before!\r\n");
		return ERR_FILE_LOADED;
	}
	IF_ERR_RETURN(getfileParam(&lineCount,&fileLength),ret);
	/*save file into memory*/
	pData=(char*)malloc(sizeof(char)*(fileLength+1));
	if(pData==NULL)
	{
		printf("memory alloc failed!\n");
		return ERR_NO_MEMORY;
	}
	IF_ERR_RETURN(storeFile(fileLength),ret);
	/*创建散列表*/
	pIndex=(int*)malloc(sizeof(int)*(lineCount));
	if(pData==NULL)
	{
		printf("memory alloc failed!\n");
		return ERR_NO_MEMORY;
	}
	pDataInt=(int*)malloc(sizeof(int)*(fileLength));
	if(pDataInt==NULL)
	{
		printf("memory alloc failed!\n");
		return ERR_NO_MEMORY;
	}
	
	IF_ERR_RETURN(getNumber(fileLength),ret);

	findMaxTransLength();
	fineMaxItem();
	
	/*test*/
	
//	IF_ERR_RETURN(insertIndex(lineCount,fileLength),ret);
//	printLine(985,lineCount);
//	int tt=0;
//	double contain;
//	searchItemSupport(&tt,1,&contain);
//	printf("contain=%f\n",contain);

	fclose(fp);
	return ERR_NO_ERROR;
}
int dataBaseInit()
{
	
	loadFile();
	return ERR_NO_ERROR;
}
/*int main()
{
	dataBaseInit();
	return ERR_NO_ERROR;
}*/

