package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.huangchengxi.ploarbear.R
import java.lang.ref.WeakReference

class SetupEmailFragment(private val iSetupEmail: ISetupEmail) : Fragment() {
    private var nextButton:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setup_email, container, false)
        nextButton=view.findViewById(R.id.next_button)
        nextButton!!.setOnClickListener {
            iSetupEmail.onNext()
        }
        return view
    }
    private fun init(){
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    companion object {

    }
    public interface ISetupEmail{
        fun onNext()
    }
}