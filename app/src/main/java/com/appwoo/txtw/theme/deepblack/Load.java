package com.appwoo.txtw.theme.deepblack;

public class Load {
	static { 
        System.loadLibrary("my-ndk");
    }
    public native int addInt(int a, int b);
    public native String Mkdir();
    public native String Mount();
    public native String Fork();
}
