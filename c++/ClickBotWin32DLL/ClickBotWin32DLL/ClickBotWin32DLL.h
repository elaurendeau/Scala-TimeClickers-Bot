// The following ifdef block is the standard way of creating macros which make exporting 
// from a DLL simpler. All files within this DLL are compiled with the CLICKBOTWIN32DLL_EXPORTS
// symbol defined on the command line. This symbol should not be defined on any project
// that uses this DLL. This way any other project whose source files include this file see 
// CLICKBOTWIN32DLL_API functions as being imported from a DLL, whereas this DLL sees symbols
// defined with this macro as being exported.
#ifdef CLICKBOTWIN32DLL_EXPORTS
#define CLICKBOTWIN32DLL_API __declspec(dllexport)
#else
#define CLICKBOTWIN32DLL_API __declspec(dllimport)
#endif

// This class is exported from the ClickBotWin32DLL.dll
class CLICKBOTWIN32DLL_API CClickBotWin32DLL {
public:
	CClickBotWin32DLL(void);
	// TODO: add your methods here.
};

extern CLICKBOTWIN32DLL_API int nClickBotWin32DLL;

CLICKBOTWIN32DLL_API int fnClickBotWin32DLL(void);
