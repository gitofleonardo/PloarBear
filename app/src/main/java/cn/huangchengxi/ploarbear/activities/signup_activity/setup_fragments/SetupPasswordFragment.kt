package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.comm_views.ModalWaitingDialog
import java.util.regex.Pattern

class SetupPasswordFragment(private val iSetupPassword: ISetupPassword) : Fragment(),PasswordViewModel.View {
    private var doneButton: Button?=null
    private var passwordInp:EditText?=null
    private val model=PasswordViewModel(this)

    private var messageDialog:AlertDialog?=null
    private var waitingDialog:ModalWaitingDialog?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setup_password, container, false)
        doneButton=view.findViewById(R.id.done_button)
        passwordInp=view.findViewById(R.id.password_input)
        init()
        return view
    }
    private fun init(){
        doneButton!!.setOnClickListener {
            val passwd=passwordInp!!.text.toString()
            if (checkPassword(passwd)){
                val email=iSetupPassword.currentEmail()
                val code=iSetupPassword.currentCode()
                model.signUp(email,passwd,code)
            }else{
                showMessageDialog(resources.getText(R.string.password_format_error))
            }
        }
    }
    private fun showMessageDialog(msg:CharSequence){
        val builder= AlertDialog.Builder(requireContext(),R.style.Theme_AppCompat_Light_Dialog)
        builder.setMessage(msg)
            .setNegativeButton(resources.getText(R.string.confirm)) { p0, p1 ->
                p0.dismiss()
            }
        messageDialog=builder.show()
    }
    override fun onDestroy() {
        messageDialog?.dismiss()
        waitingDialog?.dismiss()
        super.onDestroy()
    }
    private fun checkPassword(password:String):Boolean{
        val pattern=Pattern.compile("^[\\w]{10,20}$")
        val m=pattern.matcher(password)
        return m.find()
    }
    interface ISetupPassword{
        fun onSuccess()
        fun onFailure()
        fun onPrevious()
        fun currentEmail():String
        fun currentCode():String
    }

    override fun onChecking() {
        waitingDialog= ModalWaitingDialog(requireContext())
        waitingDialog!!.setLoadingText(resources.getText(R.string.validating).toString())
        waitingDialog!!.setCancelable(false)
        waitingDialog!!.show()
    }

    override fun onSuccess() {
        iSetupPassword.onSuccess()
    }

    override fun onFailure() {
        iSetupPassword.onFailure()
    }
}