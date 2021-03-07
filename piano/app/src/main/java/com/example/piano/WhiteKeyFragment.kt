package com.example.piano

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.piano.databinding.FragmentWhiteKeyBinding
import kotlinx.android.synthetic.main.fragment_white_key.view.*


class WhiteKeyFragment : Fragment() {
    private val LOG_TAG: String = "piano:WhiteKeyFragment"
    private lateinit var _binding: FragmentWhiteKeyBinding
    private val binding get() = _binding
    private lateinit var note: String

    lateinit var onKeyDown: (note:String) -> Unit
    lateinit var onKeyUp: (note:String) -> Unit

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