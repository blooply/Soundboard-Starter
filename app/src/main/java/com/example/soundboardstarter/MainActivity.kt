package com.example.soundboardstarter

import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.soundboardstarter.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var soundPool: SoundPool
    var aNote = 0
    var bbNote = 0
    var bNote = 0
    var cNote = 0
    var csNote = 0
    var dNote = 0
    var dsNote = 0
    var eNote = 0
    var fNote = 0
    var fsNote = 0
    var gNote = 0
    var gsNote = 0

    private var playing = false
    private var stop = false
    
    private val noteMap = HashMap<String, Int>()

    private lateinit var binding: ActivityMainBinding

    private val songString = "A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500 A 500"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gson = Gson()
        val inputStream = resources.openRawResource(R.raw.song)
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        val type = object : TypeToken<List<Note>>(){}.type
        val notes = gson.fromJson<List<Note>>(jsonString, type)
        Log.d(TAG, "onCreate: $notes")

        initializeSoundPool()
        setListeners()
    }

    private fun convertString(song : String) : ArrayList<Note> {
        val shortSong = song.split(" ")
        val noteList = ArrayList<Note>()

        for(i in shortSong.indices step 2) {
            noteList.add(Note(shortSong[i + 1].toLong(), shortSong[i]))
        }

        return noteList
    }

    private suspend fun playSong(song: List<Note>) {
        for (note in song) {
            if (stop) break
            playNote(note.note)
            delay(note.duration)
        }
    }

    private suspend fun playSimpleSong() {
        playNote(aNote)
        delay(500)
        playNote(bNote)
        delay(500)
        playNote(bNote)
        delay(500)
        playNote(aNote)
        delay(500)
        playNote(bNote)
        playNote(aNote)
        delay(500)
        playNote(aNote)
        delay(500)
        playNote(bNote)
    }

    private fun delay(time: Long) {
        try {
            Thread.sleep(time)
        }
        catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun initializeSoundPool() {

        this.volumeControlStream = AudioManager.STREAM_MUSIC
        soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
//        soundPool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { soundPool, sampleId, status ->
//           // isSoundPoolLoaded = true
//        })
        aNote = soundPool.load(this, R.raw.scalea, 1)
        bbNote = soundPool.load(this, R.raw.scalebb, 1)
        bNote = soundPool.load(this, R.raw.scaleb, 1)
        cNote =  soundPool.load(this, R.raw.scalec, 1)
        csNote = soundPool.load(this, R.raw.scalecs, 1)
        dNote = soundPool.load(this, R.raw.scaled, 1)
        dsNote = soundPool.load(this, R.raw.scaleds, 1)
        eNote =  soundPool.load(this, R.raw.scalee, 1)
        fNote = soundPool.load(this, R.raw.scalef, 1)
        fsNote = soundPool.load(this, R.raw.scalefs, 1)
        gNote = soundPool.load(this, R.raw.scaleg, 1)
        gsNote =  soundPool.load(this, R.raw.scalegs, 1)

        noteMap["A"] = aNote
        noteMap["Bb"] = bbNote
        noteMap["B"] = bNote
        noteMap["C"] = cNote
        noteMap["Cs"] = csNote
        noteMap["D"] = dNote
        noteMap["Ds"] = dsNote
        noteMap["E"] = eNote
        noteMap["F"] = fNote
        noteMap["Fs"] = fsNote
        noteMap["G"] = gNote
        noteMap["Gs"] = gsNote
    }

    private fun setListeners() {
        val soundBoardListener = SoundBoardListener()

        binding.buttonMainA.setOnClickListener(soundBoardListener)
        binding.buttonMainBb.setOnClickListener(soundBoardListener)
        binding.buttonMainB.setOnClickListener(soundBoardListener)
        binding.buttonMainC.setOnClickListener(soundBoardListener)
        binding.buttonMainCs.setOnClickListener(soundBoardListener)
        binding.buttonMainD.setOnClickListener(soundBoardListener)
        binding.buttonMainDs.setOnClickListener(soundBoardListener)
        binding.buttonMainE.setOnClickListener(soundBoardListener)
        binding.buttonMainF.setOnClickListener(soundBoardListener)
        binding.buttonMainFs.setOnClickListener(soundBoardListener)
        binding.buttonMainG.setOnClickListener(soundBoardListener)
        binding.buttonMainGs.setOnClickListener(soundBoardListener)

        binding.buttonMainPlaysong.setOnClickListener {
            if (!playing) {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        binding.buttonMainPlaysong.text = "Stop Song"
                        playing = true
                        stop = false
                    }

                    playSong(convertString(songString))

                    withContext(Dispatchers.Main) {
                        binding.buttonMainPlaysong.text = "Play Song"
                        playing = false
                    }
                }
            }
            else {
                stop = true
                playing = false
            }
        }
    }

    private fun playNote(note: String) {
        playNote(noteMap[note] ?: 0)
    }

    private fun playNote(noteId : Int) {
        soundPool.play(noteId, 1f, 1f, 1, 0, 1f)
    }

    private inner class SoundBoardListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.button_main_a -> playNote(aNote)
                R.id.button_main_bb -> playNote(bbNote)
                R.id.button_main_b -> playNote(bNote)
                R.id.button_main_c -> playNote(cNote)
                R.id.button_main_cs -> playNote(csNote)
                R.id.button_main_d -> playNote(dNote)
                R.id.button_main_ds -> playNote(dsNote)
                R.id.button_main_e -> playNote(eNote)
                R.id.button_main_f -> playNote(fNote)
                R.id.button_main_fs -> playNote(fsNote)
                R.id.button_main_g -> playNote(gNote)
                R.id.button_main_gs -> playNote(gsNote)
            }
        }
    }
}
