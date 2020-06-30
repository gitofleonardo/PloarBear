package cn.huangchengxi.ploarbear.comm_views

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import cn.huangchengxi.ploarbear.R

class SimplePolarDialog(context: Context):AlertDialog(context,R.style.Theme_AppCompat_Dialog) {
    private val title by lazy { findViewById<TextView>(R.id.title) }
    private val content by lazy { findViewById<TextView>(R.id.content_text) }
    private val positiveBtn by lazy { findViewById<Button>(R.id.positive_btn) }
    private val negativeBtn by lazy { findViewById<Button>(R.id.negative_btn) }
    private val neutralBtn by lazy { findViewById<Button>(R.id.neutral_btn) }

    private var positiveListener:(()->Unit)?=null
    private var negativeListener:(()->Unit)?=null
    private var neutralListener:(()->Unit)?=null

    private var positiveName:String?=null
    private var negativeName:String?=null
    private var neutralName:String?=null

    private var titleText:String?="TITLE"
    private var contentText:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_polar_simple)
        init()
    }
    private fun init(){
        window?.setBackgroundDrawableResource(R.color.transparent)
        window?.setGravity(Gravity.CENTER)
        val m=DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(m)
        val param=window?.attributes
        param?.width=(m.widthPixels*0.5).toInt()
        window?.attributes=param

        setupButtons()
        setupContent()
    }
    fun setTitleText(title:String){
        titleText=title
    }
    fun setTitleText(@StringRes title:Int){
        setTitleText(context.resources.getText(title).toString())
    }
    fun setContentText(text:String){
        contentText=text
    }
    fun setContentText(@StringRes content:Int){
        setContentText(context.resources.getText(content).toString())
    }
    fun setupContent(){
        title!!.text=SpannableStringBuilder(titleText)
        if (contentText!=null){
            content!!.visibility=View.VISIBLE
            content!!.text=SpannableStringBuilder(contentText)
        }

    }
    fun setupButtons(){
        if (negativeListener!=null){
            negativeBtn!!.visibility=View.VISIBLE
            negativeBtn!!.text=SpannableStringBuilder(negativeName)
            negativeBtn!!.setOnClickListener {
                dismiss()
                negativeListener?.invoke()
            }
        }
        if (positiveListener!=null){
            positiveBtn!!.visibility=View.VISIBLE
            positiveBtn!!.text=SpannableStringBuilder(positiveName)
            positiveBtn!!.setOnClickListener {
                dismiss()
                positiveListener?.invoke()
            }
        }
        if (neutralListener!=null){
            neutralBtn!!.visibility=View.VISIBLE
            neutralBtn!!.text=SpannableStringBuilder(neutralName)
            neutralBtn!!.setOnClickListener {
                dismiss()
                neutralListener?.invoke()
            }
        }
    }
    fun setNegativeButton(message:String,listener:()->Unit){
        this.negativeName=message
        this.negativeListener=listener
    }
    fun setNegativeButton(@StringRes message:Int,listener: () -> Unit){
        setNegativeButton(context.resources.getText(message).toString(),listener)
    }
    fun setPositiveButton(message: String,listener: () -> Unit){
        this.positiveName=message
        this.positiveListener=listener
    }
    fun setPositiveButton(@StringRes message: Int,listener: () -> Unit){

        setPositiveButton(context.resources.getText(message).toString(),listener)
    }
    fun setNeutralButton(message: String,listener: () -> Unit){
        this.neutralName=message
        this.neutralListener=listener
    }
    fun setNeutralButton(@StringRes message: Int, listener: () -> Unit){
        setNeutralButton(context.resources.getText(message).toString(),listener)
    }
}