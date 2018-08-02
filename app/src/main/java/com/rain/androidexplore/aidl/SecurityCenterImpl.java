package com.rain.androidexplore.aidl;

import android.os.RemoteException;

import com.rain.androidexplore.ISecurityCenter;

/**
 * Author:rain
 * Date:2018/8/2 10:05
 * Description:
 * 该方法运行在服务端binder连接池中
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {
    private static final char SECRET_CODE  = '^';
    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
