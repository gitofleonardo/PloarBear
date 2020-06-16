package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.activities.activity_utils.ToolKits
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.database.SqliteHelper
import cn.huangchengxi.ploarbear.recyclerview_adapters.BottomAccountAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : AppCompatActivity(),Presenter.Executor {
    private var passwordVisible=false

    private val imm by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager}
    private val account by lazy { findViewById<EditText>(R.id.account) }
    private val password by lazy { findViewById<EditText>(R.id.password) }
    private val loginBtn by lazy {findViewById<CardView>(R.id.login_btn)}
    private val clearBtn by lazy {findViewById<FrameLayout>(R.id.clear_current_account)}
    private val toggleVisibility by lazy { findViewById<FrameLayout>(R.id.toggle_password_visibility) }

    private var bottomAccount:BottomSheetDialog?=null
    private var bottomBehavior:BottomSheetBehavior<View>?=null
    private val showBottomAccounts by lazy { findViewById<FrameLayout>(R.id.show_bottom_accounts) }
    private var bottomAccountRecycler:RecyclerView?=null
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val bottomSheetCallback=object : BottomSheetBehavior.BottomSheetCallback(){
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState==BottomSheetBehavior.STATE_EXPANDED){}
        }
    }
    private var localUsers=ArrayList<LocalUser>()
    private val adapter=BottomAccountAdapter(localUsers)

    private val presenter=Presenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ToolKits.makeFullScreen(this)
        setContentView(R.layout.activity_login)
        init()
    }
    private fun init(){
        showBottomAccounts.setOnClickListener{
            showBottomAccounts()
        }
        loginBtn.setOnClickListener{
            presenter.login(this,account.text.toString(),password.text.toString())
        }
        clearBtn.setOnClickListener {
            account.text=SpannableStringBuilder("")
            password.text=SpannableStringBuilder("")
        }
        toggleVisibility.setOnClickListener {
            Log.e("vis",password.inputType.toString())
            if (passwordVisible){
                password.inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                toggleVisibility.findViewById<ImageView>(R.id.password_eye).setImageResource(R.drawable.password_invisible)
            }else{
                password.inputType=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggleVisibility.findViewById<ImageView>(R.id.password_eye).setImageResource(R.drawable.password_visible)
            }
            passwordVisible=!passwordVisible
            password.setSelection(password.text.toString().length)
        }
        presenter.obtainLocalUsers(this)
    }
    private fun showBottomAccounts(){
        if (bottomAccount!=null){
            bottomAccount!!.show()
        }else{
            val view=layoutInflater.inflate(R.layout.bottom_account_manage,null)
            bottomAccount= BottomSheetDialog(this,R.style.Theme_Design_BottomSheetDialog)
            bottomAccount!!.setContentView(view)
            bottomBehavior= BottomSheetBehavior.from(view.parent as View)
            bottomBehavior!!.addBottomSheetCallback(bottomSheetCallback)
            bottomAccountRecycler=view.findViewById(R.id.bottom_accounts)
            bottomAccountRecycler!!.adapter=adapter
            bottomAccountRecycler!!.layoutManager=layoutManager
            bottomAccount!!.show()
        }
    }

    override fun onLoginSuccess() {

    }

    override fun onLoginFailed() {

    }

    override fun onLoadLocalUsers(users: List<LocalUser>) {
        localUsers.clear()
        localUsers.addAll(users)
        val user=SqliteHelper.getLocalCurrentUser(this)
        if (user!=null){
            account.text=SpannableStringBuilder(user.account)
            password.text=SpannableStringBuilder(user.passwd)
        }else if (localUsers.size>0){
            account.text=SpannableStringBuilder(localUsers[0].account)
            password.text=SpannableStringBuilder(localUsers[0].passwd)
        }
    }

    override fun onLogin() {

    }
}