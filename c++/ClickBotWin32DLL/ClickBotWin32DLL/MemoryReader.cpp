#include "stdafx.h"

MemoryReader::MemoryReader(HWND handle)
{
	GetWindowThreadProcessId(handle, &PROC_ID);
	PROC_HANDLE = OpenProcess(PROCESS_ALL_ACCESS, FALSE, PROC_ID);
};

int MemoryReader::ReadIntPointer(unsigned int Pointer)
{
	int Value;
	ReadProcessMemory(PROC_HANDLE, (LPVOID)Pointer, &Value, sizeof(int), NULL);
	return Value;
};

double MemoryReader::ReadDoublePointer(unsigned int Pointer)
{
	double Value;
	ReadProcessMemory(PROC_HANDLE, (LPVOID)Pointer, &Value, sizeof(double), NULL);
	return Value;
};

int MemoryReader::ReadIntOffset(int offset, int baseaddr, int offsets[])
{
	int Address = baseaddr;
	int value;
	int total = offset;
	for (int i = 0; i < total; i++) //Loop trough the offsets
	{
		ReadProcessMemory(PROC_HANDLE, (LPCVOID)Address, &Address, 4, NULL);
		Address += offsets[i];
	}

	ReadProcessMemory(PROC_HANDLE, (LPCVOID)Address, &value, 4, NULL);

	return value;

};

double MemoryReader::ReadDoubleOffset(int offset, int baseaddr, int offsets[])
{
	int Address = baseaddr;
	double value;
	int total = offset;
	for (int i = 0; i < total; i++) //Loop trough the offsets
	{
		ReadProcessMemory(PROC_HANDLE, (LPCVOID)Address, &Address, 4, NULL);
		Address += offsets[i];
	}

	ReadProcessMemory(PROC_HANDLE, (LPCVOID)Address, &value, 8, NULL);

	return value;

};

string MemoryReader::ReadStringPointer(unsigned int Pointer)
{
	char cValue[25] = "\0";
	ReadProcessMemory(PROC_HANDLE, (LPVOID)Pointer, &cValue, sizeof(cValue), NULL);
	string value = cValue;
	return value;
};

DWORD_PTR MemoryReader::DwGetModuleBaseAddress(DWORD dwProcID, TCHAR *szModuleName)
{
	DWORD_PTR dwModuleBaseAddress = 0;
	HANDLE hSnapshot = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE | TH32CS_SNAPMODULE32, dwProcID);
	if (hSnapshot != INVALID_HANDLE_VALUE)
	{
		MODULEENTRY32 ModuleEntry32;
		ModuleEntry32.dwSize = sizeof(MODULEENTRY32);
		if (Module32First(hSnapshot, &ModuleEntry32))
		{
			do
			{
				if (_wcsicmp(ModuleEntry32.szModule, szModuleName) == 0)
				{
					dwModuleBaseAddress = (DWORD_PTR)ModuleEntry32.modBaseAddr;
					break;
				}
			} while (Module32Next(hSnapshot, &ModuleEntry32));
		}
		CloseHandle(hSnapshot);
	}
	return dwModuleBaseAddress;
};
