package com.example.piano

import Note
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.example.piano.databinding.FragmentPianoLayputBinding
import kotlinx.android.synthetic.main.fragment_piano_layput.view.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class PathIsNullException(message: String) : Exception(message)

class PianoLayoutFragment : Fragment() {
    private val LOG_TAG: String = "piano:PianoLayoutFragment"

    private var _binding:FragmentPianoLayputBinding? = null
    private val binding get() = _binding!!

    private val whiteKeyTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")
    private val blackKeyTones = listOf("C#", "D#", "?", "F#", "G#", "A#", "?", "C2#", "D2#", "?", "F2#", "G2#")

    private var scoreSheet: MutableList<Note> = mutableListOf<Note>()

    var onSave:((file:Uri) -> Unit)? = null

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

        var startTime: Long = 0


        for (tone in whiteKeyTones) {
            val whiteKey = WhiteKeyFragment.newInstance(tone)

            whiteKey.onKeyDown = { note: String ->
                startTime = System.nanoTime()

                Log.d(LOG_TAG, "Piano key down $note")
            }

            whiteKey.onKeyUp = { note: String ->
                // saves note played with how long it was pressed
                val playedNote = Note(note, System.nanoTime() - startTime)
                scoreSheet.add(playedNote)

                Log.d(LOG_TAG, "Piano key Up $note")
            }

            fragmentTransaction.add(view.whiteKeysLayout.id, whiteKey, "note_$tone")
        }


        for (tone in blackKeyTones) {
            if (tone == "?") {  // Adds spacing where there shouldn't be a black key
                val emptyHalfTonePianoKey = SpacingKey.newInstance()
                fragmentTransaction.add(view.blackKeysLayout.id, emptyHalfTonePianoKey, "note_$tone")
            } else {
                val blackKey = BlackKeyFragment.newInstance(tone)

                blackKey.onKeyDown = { note: String ->
                    startTime = System.nanoTime()

                    Log.d(LOG_TAG, "Piano key down $note")
                }

                blackKey.onKeyUp = { note: String ->
                    // saves note played with how long it was pressed
                    val playedNote = Note(note, System.nanoTime() - startTime)
                    scoreSheet.add(playedNote)

                    Log.d(LOG_TAG, "Piano key Up $note")
                }

                fragmentTransaction.add(view.blackKeysLayout.id, blackKey, "note_$tone")
            }
        }
        fragmentTransaction.commit()

        view.saveNotesBtn.setOnClickListener {
            val fileName = view.filenNameTextEdit.text.toString()
            if (scoreSheet.count() > 0) {
                if (validateFileName(fileName)) {
                    // map converts list to String. Reduce converts list to a string
                    val content = scoreSheet.map { it.toString() }.reduce{ acc, item -> acc + item + "\n" }

                    try {
                        saveScoreSheet(fileName, content)
                    } catch (e : PathIsNullException) {
                        Toast.makeText(activity,"Exception saving file: $e", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity,"Invalid filename", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity,"scoreSheet is empty", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun saveScoreSheet(fileName: String, content: String) {
        val fileSuffix: String = ".notes"

        // Throws exception if path is null
        val path = this.activity?.getExternalFilesDir(null) ?: throw PathIsNullException("path is null")

        val file = File(path, "$fileName$fileSuffix")
        if (!file.exists()) {
            // Opens file stream that appends if file already exists
            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.write(content)
                Log.d(LOG_TAG, "scoreSheet saved: $content")
            }

            this.onSave?.invoke(file.toUri())
        } else {
            Toast.makeText(activity,"File already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFileName(fileName: String): Boolean {
        return when {
            fileName.isEmpty() -> false
            fileName.length > 40 -> false
            fileName.contains("=") -> false
            fileName.contains("/") -> false
            fileName.contains(" ") -> false
            else -> true
        }
    }
}