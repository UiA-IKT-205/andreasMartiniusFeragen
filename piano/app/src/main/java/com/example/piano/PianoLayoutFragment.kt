package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentPianoLayputBinding
import kotlinx.android.synthetic.main.fragment_piano_layput.view.*


class PianoLayoutFragment : Fragment() {
    private var _binding:FragmentPianoLayputBinding? = null
    // double bang, forced unboxing, krever at _binding finnes
    private val binding get() = _binding!!

    private val whiteKeyTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")
    private val blackKeyTones = listOf("C#", "D#", "?", "F#", "G#", "A#", "?", "C2#", "D2#", "?", "F2#", "G2#")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPianoLayputBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        for (tone in whiteKeyTones) {
            val whiteKey = WhiteKeyFragment.newInstance(tone)
            fragmentTransaction.add(view.whiteKeysLayout.id, whiteKey, "note_$tone")
        }


        for (tone in blackKeyTones) {
            if (tone == "?") {  // Adds spacing where there shouldn't be a black key
                val emptyHalfTonePianoKey = SpacingKey.newInstance()
                fragmentTransaction.add(view.blackKeysLayout.id, emptyHalfTonePianoKey, "note_$tone")
            } else {
                val blackKey = BlackKeyFragment.newInstance(tone)
                fragmentTransaction.add(view.blackKeysLayout.id, blackKey, "note_$tone")
            }
        }
        fragmentTransaction.commit()
        return view
    }
}