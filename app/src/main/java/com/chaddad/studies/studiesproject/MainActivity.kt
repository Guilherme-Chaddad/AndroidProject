package com.chaddad.studies.studiesproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var clickedBackTwice : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.exit == item.itemId){
            this.finishAndRemoveTask()
        }
        return true
    }

    override fun onBackPressed() {
        if(clickedBackTwice){
            super.onBackPressed()
            return
        }
        clickedBackTwice = true
        Toast.makeText(this, R.string.doubleClickToExitMessage, Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { clickedBackTwice = false }, 2000)
    }
}
