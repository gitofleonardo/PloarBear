package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Looper
import org.junit.Test

import org.junit.Assert.*

class EmailViewModelTest {

    @Test
    fun sendEmailValidationCode() {
        Looper.prepare()
        val model=EmailViewModel(object : EmailViewModel.View{
            override fun onSendingEmail() {
            }

            override fun onErrorSending() {
            }

            override fun onSuccessSending() {
            }

            override fun onErrorValidation() {
            }

            override fun onSuccessValidation() {
            }

        })
        model.sendEmailValidationCode("971840889@qq.com")
    }

    @Test
    fun validateEmailCode() {
    }
}