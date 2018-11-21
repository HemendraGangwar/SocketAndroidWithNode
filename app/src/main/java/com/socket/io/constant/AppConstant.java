package com.socket.io.constant;

import com.socket.io.socketData.SocketDataAmend;

/**
 * AppConstant is the class for holding constant variable which will be used globally wherever needed in the complete application
 * Created by hemendrag on 11/02/2018.
 */

public class AppConstant {


    // static variable msAppConstantObj of type Singleton
    private static AppConstant msAppConstantObj = null;

    // static method to create instance of Singleton class
    public static AppConstant getInstance() {
        if (msAppConstantObj == null)
            msAppConstantObj = new AppConstant();

        return msAppConstantObj;
    }

    /**
     * declare variable used globally
     */
    public final String REPLACED_EMPTY_STRING = "\"\"";
    public final String EMPTY_STRING = "";

    /**
     * Making Socket connection global
     */
    public SocketDataAmend mSocketDataAmend;


}
