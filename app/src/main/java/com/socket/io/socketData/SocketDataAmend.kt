package com.socket.io.socketData

import android.app.Activity
import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.engineio.client.Transport
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.socket.io.MainActivity
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask
import com.github.nkzawa.socketio.client.Manager

/**
 * Created by hemendrag on 11/02/2018.
 * This class is used for update frequently data to server
 */
class SocketDataAmend
/**
 * declare constructor of class
 */
private constructor() {

    private val TAG = "SocketDataAmend"
    private var mSocket: Socket? = null
    private var mCallBackActivity: Activity? = null
    private var mID: String? = null
    private var isFetchingData = false
    private var isSocketConnected = false
    private val TIMER_DELAY = 1000
    private var timerTask: TimerTask? = null
    private var timer: Timer? = null

    companion object {
        /**
         * get instance of class
         */
        private var msSocketDataAmend: SocketDataAmend? = null
        val socketConnection: SocketDataAmend
            get() {
                if (msSocketDataAmend == null) {
                    msSocketDataAmend = SocketDataAmend()
                }
                return msSocketDataAmend!!
            }
    }


    fun setUpConnectAndFetchData(activity: Activity, id: String) {

        mCallBackActivity = activity
        mID = id

        /**
         * create connection for socket
         */
        var options = IO.Options()
        options.reconnection = false
        options.forceNew = true
        try {
           mSocket = IO.socket(ApiUtils.getInstance().SOCKET_AMEND_DATA_PORT, options)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
       // mSocket = IO.socket("http://chat.socket.io")

        IO.setDefaultHostnameVerifier { hostname, session -> true }

        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.connect()


        mSocket!!.io().on(Manager.EVENT_TRANSPORT) { args ->
            val transport = args[0] as Transport
            transport.on(Transport.EVENT_ERROR) { args ->
                val e = args[0] as Exception
                Log.e(TAG, "Transport error $e")
                e.printStackTrace()
                e.cause!!.printStackTrace()
            }
        }

        startFetchingDataConnection()

    }

    private fun startFetchingDataConnection() {
        timerTask = SocketDataPullTask()
        timer = Timer(true)
        timer!!.scheduleAtFixedRate(timerTask, 300, TIMER_DELAY.toLong())
    }

    private inner class SocketDataPullTask : TimerTask() {
        override fun run() {
            getDataFromServer()
        }
    }


    private fun getDataFromServer() {

        if (isSocketConnected && !isFetchingData) {
            isFetchingData = true
            try {
                val jsonObj = JSONObject()
                jsonObj.put("id", "" + mID!!)
                mSocket!!.emit("message", jsonObj)
            } catch (e: Exception) {
            }

        }
    }


    /************** listeners of socket  ***************/
    private val onConnect = Emitter.Listener { args ->
        Log.e("TAG", "connect = $args")
        isSocketConnected = true
        mSocket!!.on("message", getData_listener)
    }


    private val onDisconnect = Emitter.Listener { args ->
        Log.e("TAG", "disconnect = $args")
        isSocketConnected = false
    }

    private val onConnectError = Emitter.Listener { args ->
        Log.e("TAG", "error = $args")
        isSocketConnected = false
    }


    private val getData_listener = Emitter.Listener { args ->
        try {
            val strResponse = args[0].toString()
            mCallBackActivity!!.runOnUiThread {
                try {
                    /**
                     * After getting data update into view
                     */
                    (mCallBackActivity as MainActivity).updateDataOnViews(strResponse)
                    isFetchingData = false

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /************** disconnect socket  ***************/

    @Throws(Exception::class)
    fun disconnectSocket() {

        if (timerTask != null) {
            timerTask!!.cancel()
        }
        if (timer != null) {

            timer!!.cancel()
        }
        if (mSocket != null) {
            mSocket!!.disconnect()
            mSocket!!.off(Socket.EVENT_CONNECT, onConnect)
            mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        }
    }

}
