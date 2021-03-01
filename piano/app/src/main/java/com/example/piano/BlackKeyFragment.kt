package com.example.piano

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentBlackKeyBinding
import kotlinx.android.synthetic.main.fragment_black_key.view.*

class BlackKeyFragment : Fragment() {
    private lateinit var _binding: FragmentBlackKeyBinding
    private val binding get() = _binding
    private var note: String? = null

    private val onKeyUp: (note:String) -> Unit = { println("Piano key Up $it") }
    private val onKeyDown: (note:String) -> Unit = { println("Piano key down $it") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.getString("NOTE") == null || it.getString("NOTE") == "?") {
                note = null
            } else {
                note = it.getString("NOTE")!!
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlackKeyBinding.inflate(inflater, container, false)
        val view = binding.root

        if (note != null) {
            view.blackKey.setOnTouchListener { _, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> this@BlackKeyFragment.onKeyDown.invoke(note!!)
                    MotionEvent.ACTION_UP -> this@BlackKeyFragment.onKeyUp.invoke(note!!)
                }
                true
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            BlackKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}