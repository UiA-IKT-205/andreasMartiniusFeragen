package com.example.piano

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentWhiteKeyBinding
import kotlinx.android.synthetic.main.fragment_white_key.view.*

class WhiteKeyFragment : Fragment() {
    private lateinit var _binding: FragmentWhiteKeyBinding
    private val binding get() = _binding
    private lateinit var note: String

    private val onKeyUp: (note:String) -> Unit = { println("Piano key Up $it") }
    private val onKeyDown: (note:String) -> Unit = { println("Piano key down $it") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWhiteKeyBinding.inflate(inflater, container, false)
        val view = binding.root

        view.whiteKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {this@WhiteKeyFragment.onKeyDown.invoke(note)}
                    MotionEvent.ACTION_UP -> {this@WhiteKeyFragment.onKeyUp.invoke(note)}
                }
                return true
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            WhiteKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}