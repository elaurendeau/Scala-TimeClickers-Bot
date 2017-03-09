// ClickBotWin32DLL.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "ClickBotWin32DLL.h"


// This is an example of an exported variable
CLICKBOTWIN32DLL_API int nClickBotWin32DLL=0;

// This is an example of an exported function.
CLICKBOTWIN32DLL_API int fnClickBotWin32DLL(void)
{
    return 42;
}

// This is the constructor of a class that has been exported.
// see ClickBotWin32DLL.h for the class definition
CClickBotWin32DLL::CClickBotWin32DLL()
{
    return;
}
