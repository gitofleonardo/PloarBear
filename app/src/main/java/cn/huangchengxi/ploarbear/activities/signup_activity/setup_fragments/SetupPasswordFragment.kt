package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.huangchengxi.ploarbear.R
class SetupPasswordFragment(private val iSetupPassword: ISetupPassword) : Fragment() {
    private var doneButton: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setup_password, container, false)
        doneButton=view.findViewById(R.id.done_button)
        doneButton!!.setOnClickListener {
            iSetupPassword.onSuccess()
        }
        return view
    }

    companion object {
    }
    public interface ISetupPassword{
        fun onSuccess()
        fun onPrevious()
    }
}