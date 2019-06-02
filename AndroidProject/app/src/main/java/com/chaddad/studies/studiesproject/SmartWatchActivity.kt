package com.chaddad.studies.studiesproject

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.IntentFilter
import android.graphics.Color


class SmartWatchActivity : AppCompatActivity() {

    private val BLUETOOTH_REQUEST = 10

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> setButtonsVisibility(false)
                    BluetoothAdapter.STATE_ON -> setButtonsVisibility(true)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_watch)

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val bluetoothAdapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val isBluetoothEnabled = bluetoothAdapter.isEnabled

        setButtonsVisibility(isBluetoothEnabled)

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(BLUETOOTH_REQUEST == requestCode) {
            setButtonsVisibility(resultCode == Activity.RESULT_OK)
        }
    }

    private fun setButtonsVisibility(isBluetoothEnabled: Boolean) {
        val btnUpdate: Button = findViewById(R.id.swBtnUpdate);
        val btnBluetooth: Button = findViewById(R.id.swBtnBluetooth);
        if (isBluetoothEnabled) {
            btnBluetooth.visibility = View.INVISIBLE
            btnUpdate.visibility = View.VISIBLE
        } else {
            btnBluetooth.visibility = View.VISIBLE
            btnUpdate.visibility = View.INVISIBLE
        }
    }

    fun turnOnBluetooth(view: View){
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST)
    }

    fun updateInfo(view : View) {

    }

    override fun onDestroy() {
        // Unregister broadcast listeners
        unregisterReceiver(bluetoothReceiver);
        super.onDestroy()
    }
}
