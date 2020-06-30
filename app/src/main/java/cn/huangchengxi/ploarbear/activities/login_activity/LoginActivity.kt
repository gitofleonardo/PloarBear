package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.activities.activity_utils.ToolKits
import cn.huangchengxi.ploarbear.activities.main_activity.MainActivity
import cn.huangchengxi.ploarbear.activities.signup_activity.SignUpActivity
import cn.huangchengxi.ploarbear.application.PolarApplication
import cn.huangchengxi.ploarbear.comm_views.ModalWaitingDialog
import cn.huangchengxi.ploarbear.comm_views.PolarToast
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.database.SqliteHelper
import cn.huangchengxi.ploarbear.database.TextValidator
import cn.huangchengxi.ploarbear.recyclerview_adapters.BottomAccountAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_sign_up.view.*

class LoginActivity : AppCompatActivity(),Presenter.Executor {
    private var passwordVisible=false

    private val imm by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager}
    private val account by lazy { findViewById<EditText>(R.id.account) }
    private val password by lazy { findViewById<EditText>(R.id.password) }
    private val loginBtn by lazy {findViewById<CardView>(R.id.login_btn)}
    private val clearBtn by lazy {findViewById<FrameLayout>(R.id.clear_current_account)}
    private val toggleVisibility by lazy { findViewById<FrameLayout>(R.id.toggle_password_visibility) }
    private val signUp by lazy { findViewById<TextView>(R.id.create_account) }

    private var bottomAccount:BottomSheetDialog?=null
    private var bottomBehavior:BottomSheetBehavior<View>?=null
    private val showBottomAccounts by lazy { findViewById<FrameLayout>(R.id.show_bottom_accounts) }
    private var bottomAccountRecycler:RecyclerView?=null
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private var modalDialog:ModalWaitingDialog?=null

    private val bottomSheetCallback=object : BottomSheetBehavior.BottomSheetCallback(){
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        override fun onStateChanged(bottomSheet: View, newState: Int) {}
    }
    private var localUsers=ArrayList<LocalUser>()
    private val adapter=BottomAccountAdapter(localUsers)

    private val presenter=Presenter(this,this)

    private var currentUserName:String?=null
    private var currentUserPassword:String?=null

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
            if (!TextValidator.checkAccount(account.text.toString())){
                showToast(resources.getText(R.string.account_format_error).toString())
                return@setOnClickListener
            }
            if (!TextValidator.checkPassword(password.text.toString())){
                showToast(resources.getText(R.string.password_format_error).toString())
                return@setOnClickListener
            }
            currentUserName=account.text.toString()
            currentUserPassword=password.text.toString()
            presenter.login(this,account.text.toString(),password.text.toString())
        }
        clearBtn.setOnClickListener {
            account.text=SpannableStringBuilder("")
            password.text=SpannableStringBuilder("")
        }
        signUp.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        toggleVisibility.setOnClickListener {
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
            adapter.setDelListener {
                presenter.removeLocalUser(localUsers[it].account)
            }
            adapter.setContainerListener {
                currentUserName=localUsers[it].account
                currentUserPassword=localUsers[it].passwd
                account.text=SpannableStringBuilder(currentUserName)
                password.text=SpannableStringBuilder(currentUserPassword)
                if (bottomAccount!=null){
                    bottomAccount!!.dismiss()
                }
            }
            val view=layoutInflater.inflate(R.layout.bottom_account_manage,null)
            bottomAccount= BottomSheetDialog(this,R.style.BottomSheetAnimationDialog)
            bottomAccount!!.setContentView(view)
            bottomBehavior= BottomSheetBehavior.from(view.parent as View)
            bottomBehavior!!.addBottomSheetCallback(bottomSheetCallback)
            bottomAccountRecycler=view.findViewById(R.id.bottom_accounts)
            bottomAccountRecycler!!.adapter=adapter
            bottomAccountRecycler!!.layoutManager=layoutManager
            bottomAccount!!.show()
        }
    }
    private fun showToast(message:String){
        PolarToast.show(this,R.drawable.error,message,Toast.LENGTH_SHORT)
    }
    private fun showLoading(message: String){
        modalDialog= ModalWaitingDialog(this)
        modalDialog!!.setLoadingText(message)
        modalDialog!!.setCancelable(false)
        modalDialog!!.show()
    }
    private fun hideLoading(){
        modalDialog?.dismiss()
    }
    override fun onLoginSuccess(session: String) {
        val localUser=LocalUser(currentUserName!!,currentUserPassword!!)
        presenter.updateLocalCurrentUser(localUser)
        storeToApplication(currentUserName!!,currentUserPassword!!,session)
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun storeToApplication(username:String,password:String,session:String){
        (application as PolarApplication).sessionId=session
        (application as PolarApplication).username=username
        (application as PolarApplication).password=password
    }
    override fun onLoginFailed(reason:String) {
        hideLoading()
        showToast(reason)
    }
    override fun onLoadLocalUsers(users: List<LocalUser>) {
        localUsers.clear()
        localUsers.addAll(users)
        adapter.notifyDataSetChanged()
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
        showLoading(resources.getText(R.string.on_login).toString())
    }

    override fun removeLocalUserSuccess() {
        presenter.obtainLocalUsers(this)
    }

    override fun onDestroy() {
        modalDialog?.dismiss()
        super.onDestroy()
    }
}