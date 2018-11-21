package com.socket.io

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.socket.io.constant.AppConstant
import com.socket.io.socketData.SocketDataAmend
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create connection with server
        connectToSocket();
    }

    /**
     * this method is basically for connecting app to socket for live data connection
     */
    private fun connectToSocket(){
        AppConstant.getInstance().mSocketDataAmend = SocketDataAmend.socketConnection
        AppConstant.getInstance().mSocketDataAmend.setUpConnectAndFetchData(this, "1")
    }

    /**
     * when get the data from node, update into the views
     */
    fun updateDataOnViews(responseFromServer : String){
        textViewData.text = responseFromServer
    }

    /**
     * when the activity instance will be removed from stack, socket will be disconnect
     */
    override fun onDestroy() {
        super.onDestroy()
        AppConstant.getInstance().mSocketDataAmend.disconnectSocket()
    }

}
