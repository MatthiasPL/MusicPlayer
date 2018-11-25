package com.loopmoth.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val player = music_player()
        val manager = supportFragmentManager
        val translation = manager.beginTransaction()

        translation.replace(R.id.fragmentContainer, player)
        translation.commit()

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
                //Toast.makeText(applicationContext, "Next song", Toast.LENGTH_SHORT).show()

            }

            override fun onSwipeRight() {
                //Toast.makeText(applicationContext, "Previous song", Toast.LENGTH_SHORT).show()

            }

            override fun onSwipeBottom() {
                //val animation = AnimationUtils.loadLayoutAnimation(applicationContext, R.anim.blink)

                //Toast.makeText(applicationContext, "List", Toast.LENGTH_SHORT).show()
                val musicList = BlankFragment()
                val transaction = supportFragmentManager.beginTransaction()


                transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.blink)
                transaction.replace(R.id.fragmentContainer, musicList)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            override fun onSwipeTop() {
                //Toast.makeText(applicationContext, "Hide list", Toast.LENGTH_SHORT).show()
                val player = music_player()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.blink)
                transaction.replace(R.id.fragmentContainer, player)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
    }
}
