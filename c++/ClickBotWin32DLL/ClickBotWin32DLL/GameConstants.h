#pragma once

class GameConstants
{
public:
	HWND ApplicationHandle = FindWindow(NULL, L"Time Clickers");
	TCHAR * ApplicationNameTChar = _T("TimeClickers.exe");
	const UINT_PTR StaticOffSetLevel = 0x00E0CC78;
	static int OffsetListLevel[];
	const int OffsetCountLevel = 5;

	const UINT_PTR StaticOffSetWarpCubes = 0x00DD62B0;
	static int OffsetListWarpCubes[];
	const int OffsetCountWarpCubes = 5;
};
