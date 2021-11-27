package jp.live.dictoss.mytabbedapp1

import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import jp.live.dictoss.mytabbedapp1.databinding.ActivityMainBinding
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setting the action bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        // set up Bottom Navigation and NavigationDrawer on actionbar.
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: BottomNavigationView = binding.navView
        val navSideView: NavigationView = binding.navSideView
        navSideView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        this.appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_video
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, this.appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        // アプリバー内の左端のドロワーアイコンをクリックしたときのイベント
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(this.appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main_settings-> {
                Log.i("TAG", "IN onOptionsItemSelected()")
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        return when (item.itemId) {
            R.id.menu_nav_settings-> {
                Log.i("TAG", "IN onNavigationItemSelected() : R.id.menu_nav_settings")
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(Gravity.LEFT)
                false
            }
            R.id.menu_nav_blog-> {
                Log.i("TAG", "IN onNavigationItemSelected() : R.id.menu_nav_blog")

                val tabsIntent : CustomTabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()

                val pageUrl : String = sharedPreferences.getString("edit_text_preference_menu_item_blog_url", "") ?: ""
                val uri : Uri = Uri.parse(pageUrl)
                tabsIntent.intent.setPackage("com.android.chrome")
                tabsIntent.launchUrl(this, uri)

                drawerLayout.closeDrawer(Gravity.LEFT)
                false
            }
            R.id.menu_nav_hardware-> {
                Log.i("TAG", "IN onNavigationItemSelected() : R.id.menu_nav_hardware")
                drawerLayout.closeDrawer(Gravity.LEFT)
                false
            }
            else -> { return false }
        }
    }
}