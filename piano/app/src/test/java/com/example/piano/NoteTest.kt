package com.example.piano

import Note
import org.junit.Test
import org.junit.Assert.*


class NoteTest {

    @Test
    fun NoteToStringTest() {
        val listOfTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2", "C#", "D#", "F#", "G#", "A#", "?", "C2#", "D2#", "F2#", "G2#")
        val random = Math.random().toLong()
        val tone = listOfTones.random()
        for (i in 1..20){
            assertEquals("$tone, ${random.toString()}", Note(tone, random).toString())
        }
    }
}