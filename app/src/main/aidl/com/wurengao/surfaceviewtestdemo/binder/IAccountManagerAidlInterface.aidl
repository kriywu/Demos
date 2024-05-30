// IAccountManagerAidlInterface.aidl
package com.wurengao.surfaceviewtestdemo.binder;

// Declare any non-default types here with import statements

interface IAccountManagerAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int signUp(String uid, String pwd);
    int signIn(String uid, String pwd);
    String getAccountInfo(String uid);
}