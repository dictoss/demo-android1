package jp.live.dictoss.mytabbedapp1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.app.AlertDialog
import android.content.Intent
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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

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

        // set up Bottom Navigation
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // set NavigationDrawer on actionbar.
        // https://developer.android.com/guide/navigation/navigation-ui?hl=ja#add_a_navigation_drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        this.appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, this.appBarConfiguration)
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

    public fun onClickButtonDesu(view: View) {
        Log.i("TAG","abcd.")

        val builder: AlertDialog.Builder? = AlertDialog.Builder(this)
        builder?.setTitle(R.string.mainactivity_button_msgbox_desu_title)
        builder?.setMessage(R.string.mainactivity_button_msgbox_desu_msg)

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }
}