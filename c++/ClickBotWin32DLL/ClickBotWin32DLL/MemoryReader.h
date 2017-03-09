#include <iostream>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

using namespace std;

class MemoryReader
{
public:
	MemoryReader(HWND handle);
	int ReadIntPointer(unsigned int Pointer);
	double ReadDoublePointer(unsigned int Pointer);
	int ReadIntOffset(int offset, int baseaddr, int offsets[]);
	double ReadDoubleOffset(int offset, int baseaddr, int offsets[]);
	string ReadStringPointer(unsigned int Pointer);
	DWORD_PTR DwGetModuleBaseAddress(DWORD dwProcID, TCHAR *szModuleName);
private:
	DWORD PROC_ID;
	HANDLE PROC_HANDLE;

};
