package com.firatnet.wts.classes;

import android.content.Context;

public class StaticMethod {


  public   static  boolean ConnectChecked(Context context)
    {

        //                &&  ConnectionStatus.isOnline();
        return ConnectionStatus.isNetworkAvailable( context );
    }



}
