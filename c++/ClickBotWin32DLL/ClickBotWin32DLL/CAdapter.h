#using <mscorlib.dll>
#using "KeyboardAdapter.netmodule"

using namespace System;

public __gc class CAdapter
{
public:
	// Provide .NET interop and garbage collecting to the pointer.
	KeyboardAdapter __gc *t;
	CAdapter() {
		t = new KeyboardAdapter();
		// Assign the reference a new instance of the object
	}

	// This inline function is called from the C++ Code
	int sendKeysToWindowByProcessName() {
		t->displayHelloWorld();
	}
};