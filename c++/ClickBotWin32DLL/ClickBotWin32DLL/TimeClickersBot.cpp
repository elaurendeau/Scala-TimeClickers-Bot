#include "stdafx.h"

using namespace std;


std::wstring Java_To_WStr(JNIEnv *env, jstring string)
{
	std::wstring value;

	const jchar *raw = env->GetStringChars(string, 0);
	jsize len = env->GetStringLength(string);

	value.assign(raw, raw + len);

	env->ReleaseStringChars(string, raw);

	return value;
}

int getScanCode(char c)
{
	return MapVirtualKey((int)c, 0x04);
}

LPARAM getLParam(INT16 repeatCount, char c, byte extended, byte contextCode, byte previousState, byte transitionState)
{
	LPARAM lParam = (UINT)repeatCount;
	UINT scanCode = MapVirtualKey((int)c, 0x04) + 0x80;
	lParam += scanCode * 0x10000;
	lParam += (UINT)((extended) * 0x1000000);
	lParam += (UINT)((contextCode * 2) * 0x10000000);
	lParam += (UINT)((previousState * 4) * 0x10000000);
	lParam += (UINT)((transitionState * 8) * 0x10000000);
	return lParam;
}

bool ForegroundKeyDown(char c)
{

	INPUT input;
	input.type = INPUT_KEYBOARD;
	input.ki.wScan = 0;
	input.ki.time = 0;
	input.ki.wVk = (int)c;
	input.ki.dwFlags = 0; // there is no KEYEVENTF_KEYDOWN
	SendInput(1, &input, sizeof(INPUT));

	return true;
}

bool ForegroundKeyUp(char c)
{

	INPUT input;
	input.type = INPUT_KEYBOARD;
	input.ki.wScan = 0;
	input.ki.time = 0;
	input.ki.wVk = (int)c;
	input.ki.dwFlags = 0;
	input.ki.dwFlags = KEYEVENTF_KEYUP;

	SendInput(1, &input, sizeof(INPUT));

	return true;
}

bool ForegroundKeyDown(HWND handle, char c)
{
	if (GetForegroundWindow() != handle)
	{
		if (!SetForegroundWindow(handle))
		{
			return false;
		}
	}

	return ForegroundKeyDown(c);
}


bool ForegroundKeyUp(HWND handle, char c)
{
	if (GetForegroundWindow() != handle)
	{
		if (!SetForegroundWindow(handle))
		{
			return false;
		}
	}

	return ForegroundKeyUp(c);
}

bool SendBackgroundKey(HWND handle, char c)
{
	while ((GetKeyState(0x12) & 0xF000) == 0xF000 ||
		(GetKeyState(0x11) & 0xF000) == 0xF000 ||
		(GetKeyState(0x10) & 0xF000) == 0xF000)
	{
		Sleep(1);
	}

	SendMessage(handle, WM_KEYDOWN, (int)c, getLParam(1, c, 0, 0, 0, 0));

	Sleep(10);

	SendMessage(handle, WM_CHAR, (int)c, getLParam(1, c, 0, 0, 0, 0));

	Sleep(10);

	SendMessage(handle, WM_KEYUP, (int)c, getLParam(1, c, 0, 0, 1, 1));

	Sleep(10);

	return true;
}

UINT GetLParam(int x, int y)
{
	return (UINT)((y << 16) | (x & 0xFFFF));
}

void  SetApplicationFictiveMousePosition(HWND handle, int x, int y)
{
	SendMessage(handle, WM_MOUSEMOVE, 0, GetLParam(x, y));
}

void BackgroundMouseClick(HWND hWnd, UINT key, int x, int y, int delay = 5)
{
	if (key == MK_LBUTTON)
	{
		SendMessage(hWnd, WM_LBUTTONDOWN, MK_LBUTTON, GetLParam(x, y));
		Sleep(delay);
		SendMessage(hWnd, WM_LBUTTONUP, MK_LBUTTON, GetLParam(x, y));

	}
	else if (key == MK_RBUTTON)
	{
		SendMessage(hWnd, WM_RBUTTONDOWN, MK_RBUTTON, GetLParam(x, y));
		Sleep(delay);
		SendMessage(hWnd, WM_RBUTTONUP, MK_RBUTTON, GetLParam(x, y));
	}
	else if (key == MK_LBUTTON)
	{
		SendMessage(hWnd, WM_MBUTTONDOWN, MK_MBUTTON, GetLParam(x, y));
		Sleep(delay);
		SendMessage(hWnd, WM_MBUTTONUP, MK_MBUTTON, GetLParam(x, y));
	}

}



bool SendKey(HWND handle, char c)
{
	if (handle == NULL)
	{
		return false;
	}
	else
	{

		if (!IsIconic(handle))
		{
			if (GetForegroundWindow() != handle)
			{
				SendMessage(handle, WM_SYSCOMMAND, SC_MINIMIZE, 0);
			}
			else
			{
				if (ForegroundKeyDown(handle, c))
				{
					Sleep(10);
					return ForegroundKeyUp(handle, c);
				}
				else
				{
					return false;
				}

			}
		}

		return SendBackgroundKey(handle, c);

	}
}

JNIEXPORT void JNICALL Java_com_elliot_clicker_jni_JNIAdapter_sendKeysToWindow
(JNIEnv * env, jobject, jstring messageValue)
{
	std::wstring message = Java_To_WStr(env, messageValue);
	GameConstants GC = GameConstants();

	// Getting the target window handle, from its name
	
	if (GC.ApplicationHandle != NULL) {

		std::string str(message.begin(), message.end());

		for (char& c : str) {
			SendKey(GC.ApplicationHandle, c);
		}

	}

}

JNIEXPORT jobject JNICALL Java_com_elliot_clicker_jni_JNIAdapter_getGameValues
(JNIEnv * env, jobject)
{
	GameConstants GC = GameConstants();
	DWORD serverdllBaseAddress = 0;
	DWORD pID = 0;

	GetWindowThreadProcessId(GC.ApplicationHandle, &pID);

	MemoryReader Memory = MemoryReader(GC.ApplicationHandle);

	UINT_PTR baseAddr = Memory.DwGetModuleBaseAddress(pID, GC.ApplicationNameTChar);

	HANDLE handle = OpenProcess(PROCESS_ALL_ACCESS, FALSE, pID);

	double Level = Memory.ReadDoubleOffset(GC.OffsetCountLevel, baseAddr + GC.StaticOffSetLevel, GC.OffsetListLevel);
	double WarpCubes = Memory.ReadDoubleOffset(GC.OffsetCountWarpCubes, baseAddr + GC.StaticOffSetWarpCubes, GC.OffsetListWarpCubes);

	CloseHandle(handle);

	jobject obj = NULL;

	jclass clazz = env->FindClass("com/elliot/clicker/entity/GameValues");
	// If this class does not exist then return null.

	if (clazz != NULL)
	{
		obj = env->AllocObject(clazz);

		jfieldID fid = env->GetFieldID(clazz, "level", "D");
		if (fid != 0)
		{
			env->SetDoubleField(obj, fid, Level);
		}

		jfieldID fid2 = env->GetFieldID(clazz, "currentWarpTimeCubes", "D");
		// If this field does not exist then return null.
		if (fid2 != 0)
		{
			// Set the minor field to the operating system's minor version.
			env->SetDoubleField(obj, fid2, WarpCubes);
		}
	
		
	}

	return obj;

}

JNIEXPORT void JNICALL Java_com_elliot_clicker_jni_JNIAdapter_sendClicksToWindowByButtonAndPositionAndAmountAndDelay
(JNIEnv * env, jobject, jint button, jint x, jint y, jint amt, jlong interval)
{

	GameConstants GC = GameConstants();

	SetApplicationFictiveMousePosition(GC.ApplicationHandle, (int) x, (int) y);

	for (int i = 0; i < (int) amt; i = i + 1)
	{
		BackgroundMouseClick(GC.ApplicationHandle, (UINT) button, (int) x, (int) y);
		Sleep((long) interval);
	}

}
