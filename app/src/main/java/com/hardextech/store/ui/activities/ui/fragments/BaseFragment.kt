package com.hardextech.store.ui.activities.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hardextech.store.R

open class BaseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mProgressDialogue:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showProgressDialogue(text:String){
        mProgressDialogue = Dialog(requireActivity())
        //  val inflate = (this as Activity).layoutInflater.inflate(R.layout.progress_dialogue, null)
        val view =LayoutInflater.from(requireActivity()).inflate(R.layout.progress_dialogue, null)
        view.findViewById<TextView>(R.id.tvProgressDialogue).text = text
        mProgressDialogue.setContentView(view)
        mProgressDialogue.setCancelable(false)
        mProgressDialogue.setCanceledOnTouchOutside(false)
        mProgressDialogue.show()
    }

    fun dismissProgressDialogue(){
        mProgressDialogue.dismiss()
    }
} // end of the fragment class