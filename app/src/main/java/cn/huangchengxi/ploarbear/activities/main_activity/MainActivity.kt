package cn.huangchengxi.ploarbear.activities.main_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.activities.SettingsActivity
import cn.huangchengxi.ploarbear.activities.main_activity.fragments.ContactsFragment
import cn.huangchengxi.ploarbear.activities.main_activity.fragments.HomeBaseFragment
import cn.huangchengxi.ploarbear.activities.main_activity.fragments.MessageFragment
import cn.huangchengxi.ploarbear.activities.main_activity.fragments.ToolkitFragment
import cn.huangchengxi.ploarbear.activities.main_activity.views.HomeBottomItem
import cn.huangchengxi.ploarbear.activities.main_activity.views.UserStatusView
import cn.huangchengxi.ploarbear.activities.main_activity.views.HomeBottomSheetDialog
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val userStatus by lazy { findViewById<UserStatusView>(R.id.home_user_status) }
    private val currentFragmentText by lazy { findViewById<TextView>(R.id.current_fragment_text) }
    private val bottomNav by lazy { findViewById<BottomNavigationView>(R.id.home_navigation) }
    private val messageFragment by lazy {
        val mf=MessageFragment()
        mf.setOnTransaction {
            currentFragmentText.text=SpannableStringBuilder(resources.getText(R.string.message))
        }
        mf
    }
    private val contactsFragment by lazy {
        val cf=ContactsFragment()
        cf.setOnTransaction {
            currentFragmentText.text=SpannableStringBuilder(resources.getText(R.string.contacts))
        }
        cf
    }
    private val toolkitFragment by lazy {
        val tf=ToolkitFragment()
        tf.setOnTransaction {
            currentFragmentText.text=SpannableStringBuilder(resources.getText(R.string.tools))
        }
        tf
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        setupNav()
        setupUserStatus()
    }
    private fun setupUserStatus(){
        userStatus.setOnClickListener {
            showHomeBottom()
        }
    }
    /**
     * setup bottom sheet dialog
     * set listener
     */
    private fun showHomeBottom(){
        val dialog=
            HomeBottomSheetDialog(
                this,
                R.style.BottomSheetAnimationDialog
            )
        val view=layoutInflater.inflate(R.layout.view_home_bottom_sheet,null)
        view.findViewById<HomeBottomItem>(R.id.setting).setOnClickListener {
            val intent=Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }
        dialog.setContentView(view)
        //dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
        //dialog.window!!.setBackgroundDrawableResource(R.drawable.transparent)
        //dialog.behavior.isDraggable=false

        dialog.show()
    }
    private fun setupNav(){
        transactToFragment(messageFragment)
        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.message->{
                    transactToFragment(messageFragment)
                }
                R.id.contacts->{
                    transactToFragment(contactsFragment)
                }
                R.id.tools->{
                    transactToFragment(toolkitFragment)
                }
                else->{
                }
            }
            true
        }
    }
    private fun transactToFragment(fragmentHomeBaseFragment: HomeBaseFragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment,fragmentHomeBaseFragment)
            .commitAllowingStateLoss()
        fragmentHomeBaseFragment.onTransactToFragment()
    }
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}