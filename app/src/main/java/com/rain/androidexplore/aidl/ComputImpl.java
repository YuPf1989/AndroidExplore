package com.rain.androidexplore.aidl;

import android.os.RemoteException;

import com.rain.androidexplore.ICompute;

/**
 * Author:rain
 * Date:2018/8/2 10:10
 * Description:
 */
public class ComputImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
