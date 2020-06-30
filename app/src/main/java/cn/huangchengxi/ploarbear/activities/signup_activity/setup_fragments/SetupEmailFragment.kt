package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Bundle
import android.os.Message
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.comm_views.ModalWaitingDialog
import cn.huangchengxi.ploarbear.comm_views.SimplePolarDialog
import cn.huangchengxi.ploarbear.handler.CommonHandler
import java.lang.Exception
import java.util.regex.Pattern

class SetupEmailFragment(private val iSetupEmail: ISetupEmail) : Fragment(),EmailViewModel.View ,CommonHandler.Executor{
    private var nextButton:Button?=null
    private var getCodeBtn:Button?=null
    private var emailInp:EditText?=null
    private var codeInp:EditText?=null
    private var messageDialog: SimplePolarDialog?=null
    private var waitingDialog:ModalWaitingDialog?=null

    private val model=EmailViewModel(this)
    private val handler=CommonHandler(this)

    private var currentEmail=""
    private var currentCode=""

    private val BTN_COUNTDOWN=0

    private var t:Thread?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setup_email, container, false)
        nextButton=view.findViewById(R.id.next_button)
        getCodeBtn=view.findViewById(R.id.get_validation_code)
        emailInp=view.findViewById(R.id.email_input)
        codeInp=view.findViewById(R.id.validation_code)
        init()
        return view
    }
    private fun init(){
        nextButton!!.setOnClickListener {
            val code=codeInp!!.text.toString()
            val mail=emailInp!!.text.toString()
            val cf=checkCodeFormat(code)
            val ef=checkEmailFormat(mail)
            if (!ef){
                showMessageDialog(resources.getText(R.string.mail_format_error))
            }else if (!cf){
                showMessageDialog(resources.getText(R.string.code_format_error))
            }else{
                model.validateEmailCode(mail,code)
                currentCode=code
                currentEmail=mail
            }
        }
        getCodeBtn!!.setOnClickListener {
            val mail=emailInp!!.text.toString()
            if (checkEmailFormat(mail)){
                model.sendEmailValidationCode(mail)
            }else{
                showMessageDialog(resources.getText(R.string.mail_format_error))
            }
        }
    }
    private fun showMessageDialog(msg:CharSequence){
        /**
        val builder=AlertDialog.Builder(requireContext(),R.style.Theme_AppCompat_Light_Dialog)
        builder.setMessage(msg)
            .setNegativeButton(resources.getText(R.string.confirm)) { p0, p1 ->
                p0.dismiss()
            }
        messageDialog=builder.show()
        */
        messageDialog=SimplePolarDialog(requireContext())
        messageDialog!!.setPositiveButton(R.string.confirm){

        }
        messageDialog!!.setTitleText(msg.toString())
        messageDialog!!.show()
    }
    private fun checkCodeFormat(code:String):Boolean{
        val pattern= Pattern.compile("^[0-9]{6}$")
        val m=pattern.matcher(code)
        return m.find()
    }
    private fun checkEmailFormat(mail:String):Boolean{
        val pattern= Pattern.compile("^[\\S]+@[\\S]+\$")
        val m=pattern.matcher(mail)
        return m.find()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun countDownGetBtn(){
        val r= Runnable {
            try {
                for (i in 60 downTo 0){
                    val msg=handler.obtainMessage()
                    msg.what=BTN_COUNTDOWN
                    msg.arg1=i
                    handler.sendMessage(msg)

                    Thread.sleep(1000)
                }
            }catch (e:Exception){
                Log.e("Exception",e.stackTrace.toString())
            }
        }
        t=Thread(r)
        t?.start()
    }
    interface ISetupEmail{
        fun onNext(email:String,code:String)
    }

    override fun onDestroy() {
        messageDialog?.dismiss()
        t?.interrupt()
        waitingDialog?.dismiss()
        super.onDestroy()
    }
    override fun onSendingEmail() {
        getCodeBtn!!.isEnabled=false
        getCodeBtn!!.text=SpannableStringBuilder(resources.getText(R.string.sending_email_code))
    }

    override fun onEmailAlreadyInUse() {
        getCodeBtn!!.isEnabled=true
        getCodeBtn!!.text=SpannableStringBuilder(resources.getText(R.string.get_email_code))
        showMessageDialog(resources.getText(R.string.email_already_in_use))
    }

    override fun onErrorSending() {
        getCodeBtn!!.isEnabled=true
        getCodeBtn!!.text=SpannableStringBuilder(resources.getText(R.string.get_email_code))
        showMessageDialog(resources.getText(R.string.email_code_sent_failed))
    }

    override fun onSuccessSending() {
        getCodeBtn!!.isEnabled=false
        countDownGetBtn()
    }

    override fun onErrorValidation() {
        waitingDialog?.dismiss()
        showMessageDialog(resources.getText(R.string.email_code_not_correct))
    }

    override fun onSuccessValidation() {
        waitingDialog?.dismiss()
        iSetupEmail.onNext(currentEmail,currentCode)
    }

    override fun onValidating() {
        waitingDialog= ModalWaitingDialog(requireContext())
        waitingDialog!!.setLoadingText(resources.getText(R.string.validating).toString())
        waitingDialog!!.setCancelable(false)
        waitingDialog!!.show()
    }

    override fun handleMessage(msg: Message) {
        when(msg.what){
            BTN_COUNTDOWN->{
                if (msg.arg1>0){
                    getCodeBtn!!.text=SpannableStringBuilder(resources.getText(R.string.email_code_sent).toString()+msg.arg1)
                }else{
                    getCodeBtn!!.isEnabled=true
                    getCodeBtn!!.text=SpannableStringBuilder(resources.getText(R.string.get_email_code))
                }
            }
        }
    }
}