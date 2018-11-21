package com.socket.io.socketData;

/**
 * Created by hemendrag on 11/02/2018.
 */
public class ApiUtils {


    // static variable msApiUtilsObj of type Singleton
    private static ApiUtils msApiUtilsObj = null;

    // static method to create instance of Singleton class
    public static ApiUtils getInstance(){
        if (msApiUtilsObj == null)
            msApiUtilsObj = new ApiUtils();

        return msApiUtilsObj;
    }

    /**
     * Socket URL
     */
    private final String SOCKET_BASE_URL = "http://192.16.5.35:"; // your server url
    public  final String SOCKET_AMEND_DATA_PORT = SOCKET_BASE_URL + "4001";

}
