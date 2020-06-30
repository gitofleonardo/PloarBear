package cn.huangchengxi.ploarbear.activities.signup_activity

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.activities.activity_utils.ToolKits
import cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments.SetupEmailFragment
import cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments.SetupPasswordFragment
import cn.huangchengxi.ploarbear.comm_views.PolarToast

class SignUpActivity : AppCompatActivity(),SetupEmailFragment.ISetupEmail,SetupPasswordFragment.ISetupPassword {
    private val setupEmailFragment by lazy { SetupEmailFragment(this) }
    private val setupPasswordFragment by lazy { SetupPasswordFragment(this) }
    private val fragmentManager by lazy { supportFragmentManager }
    private val signUpProgressBar by lazy { findViewById<ProgressBar>(R.id.sign_up_progress) }
    private val back by lazy { findViewById<FrameLayout>(R.id.back) }

    private var currentEmail=""
    private var currentCode=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ToolKits.makeFullScreen(this)
        setContentView(R.layout.activity_sign_up)
        init()
    }
    private fun init(){
        addFragment(setupEmailFragment)
        setProgressValue(50)

        back.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setProgressValue(progress:Int){
        val current=signUpProgressBar.progress
        val animator=ValueAnimator.ofInt(current,progress).setDuration(1000)
        animator.interpolator=FastOutSlowInInterpolator()
        animator.addUpdateListener {
            signUpProgressBar.progress=it.animatedValue as Int
        }
        animator.start()
    }
    private fun addFragment(fragment: Fragment){
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.anim_fragment_open_enter,R.anim.anim_fragment_open_exit,R.anim.anim_fragment_close_enter,R.anim.anim_fragment_close_exit)
            .replace(R.id.sign_up_fragment,fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
    private fun popFragment(){
        fragmentManager.popBackStack()
    }
    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount<=1){
            finish()
        }else{
            popFragment()
            onPrevious()
        }
    }

    override fun onNext(email: String, code: String) {
        currentEmail=email
        currentCode=code

        setProgressValue(100)
        addFragment(setupPasswordFragment)
    }

    override fun onPrevious() {
        setProgressValue(50)
    }

    override fun currentEmail(): String {
        return currentEmail
    }

    override fun currentCode(): String {
        return currentCode
    }

    override fun onSuccess() {
        //Toast.makeText(this,resources.getText(R.string.sign_up_success),Toast.LENGTH_SHORT).show()
        showMessage(R.string.sign_up_success)
        finish()
    }

    override fun onFailure() {
        //Toast.makeText(this,resources.getText(R.string.sign_up_failure),Toast.LENGTH_SHORT).show()
        showMessage(R.string.sign_up_failure)
    }
    private fun showMessage(message:Int){
        PolarToast.show(this,R.drawable.success,message,Toast.LENGTH_SHORT)
    }
}