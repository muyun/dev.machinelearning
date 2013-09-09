#ifndef ERROR_H
#define ERROR_H

#define IN
#define OUT
#define MAX_ITEM	50

#define IF_ERR_RETURN(a,b)	do{b=a;if(b)return b;}while(0)
typedef enum
{
	ERR_NO_ERROR=0,
	ERR_BAD_PARM,
	ERR_OPEN_FILE_FAILED,
	ERR_FILE_LOADED,
	ERR_NO_MEMORY
}status;


#endif
