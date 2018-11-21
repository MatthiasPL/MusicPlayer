package com.loopmoth.musicplayer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_music_player.*

class MusicPlayer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                tvTime.text = i.toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                // Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                // Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })

        bPlay.setOnClickListener {

        }

        bNext.setOnClickListener {

        }

        bPrevious.setOnClickListener {

        }

        layoutBody.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                Toast.makeText(applicationContext, "Next song", Toast.LENGTH_SHORT).show()
            }

            override fun onSwipeRight() {
                Toast.makeText(applicationContext, "Previous song", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
