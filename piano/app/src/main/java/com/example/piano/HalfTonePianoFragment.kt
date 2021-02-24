package com.example.piano

import android.os.Bundle
import android.view.KeyEvent.ACTION_DOWN
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentHalfTonePianoKeyBinding
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.view.*

class HalfTonePianoFragment : Fragment() {
    private lateinit var _binding: FragmentHalfTonePianoKeyBinding
    private val binding get() = _binding
    private var note: String? = null

    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null


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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHalfTonePianoKeyBinding.inflate(inflater)
        val view = binding.root

        if (note != null) {
            view.halfToneKey.setOnTouchListener(object: View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when(event?.action) {
                        MotionEvent.ACTION_DOWN -> this@HalfTonePianoFragment.onKeyDown?.invoke(note!!)
                        MotionEvent.ACTION_UP -> this@HalfTonePianoFragment.onKeyUp?.invoke(note!!)
                    }
                    return true
                }
            })
        } else {
            view.halfToneKey.setBackgroundColor( 0x00000000.toInt() )
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
                HalfTonePianoFragment().apply {
                    arguments = Bundle().apply {
                        putString("NOTE", note)
                    }
                }
    }
}