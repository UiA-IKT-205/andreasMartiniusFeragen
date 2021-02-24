package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentPianoLayputBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano_layput.view.*


class PianoLayoutFragment : Fragment() {

    private var _binding:FragmentPianoLayputBinding? = null
    // double bang, forced unboxing, krever at _binding finnes
    private val binding get() = _binding!!

    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View {
        _binding = FragmentPianoLayputBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        for (tone in fullTones) {
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(tone)

            fullTonePianoKey.onKeyDown = {
                println("Piano key down $it")
            }

            fullTonePianoKey.onKeyUp = {
                println("Piano key up $it")
            }

            ft.add(view.pianoKeys.id, fullTonePianoKey, "note_$tone")
        }

        ft.commit()

        return view
    }
}