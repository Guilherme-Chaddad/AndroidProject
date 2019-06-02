package com.chaddad.studies.studiesproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private var clickedBackTwice : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white)
        }

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        //to prevent current item select over and over
        if (menuItem.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        when ( menuItem.itemId ) {
            R.id.master_activities_menu -> {

            }
            R.id.watch_resources_menu -> {
                startActivity(Intent(applicationContext, SmartWatchActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.exit -> {
                this.finishAndRemoveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        if(drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
        } else {
            if (clickedBackTwice) {
                super.onBackPressed()
                return
            }
            clickedBackTwice = true
            Toast.makeText(this, R.string.doubleClickToExitMessage, Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { clickedBackTwice = false }, 2000)
        }
    }
}
