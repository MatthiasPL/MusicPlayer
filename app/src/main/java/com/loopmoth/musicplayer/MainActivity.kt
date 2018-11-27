package com.loopmoth.musicplayer

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var musicliststate = MusicListState.HIDDEN
    private var musicplayerstate = PlayerState.NEW
    private lateinit var mediaplayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()

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
            if(musicplayerstate==PlayerState.NEW){
                mediaplayer = MediaPlayer.create(applicationContext, R.raw.song)
                mediaplayer.start()
                InitializeSeekBar()
                tvTime.text = mediaplayer.duration.toString()
                bPlay.text="Pause"
                musicplayerstate=PlayerState.PLAYING
            }
            else if (musicplayerstate==PlayerState.PLAYING){
                mediaplayer.pause()
                musicplayerstate=PlayerState.PAUSED
                bPlay.text="Play"
            }
            else if(musicplayerstate==PlayerState.PAUSED){
                mediaplayer.start()
                musicplayerstate=PlayerState.PLAYING
                bPlay.text="Pause"
            }
            mediaplayer.setOnCompletionListener {
                bPlay.text="Play"
                musicplayerstate=PlayerState.NEW
                handler.removeCallbacks(runnable)
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaplayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

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
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_expand -> {
                if(musicliststate==MusicListState.HIDDEN){
                    val musicList = BlankFragment()
                    val transaction = supportFragmentManager.beginTransaction()

                    musicliststate=MusicListState.EXPANDED
                    item.setTitle("Hide playlist")

                    transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top)
                    transaction.replace(R.id.fragmentContainer, musicList)
                    transaction.commit()
                }
                else{
                    val player = music_player()
                    val transaction = supportFragmentManager.beginTransaction()

                    musicliststate=MusicListState.HIDDEN
                    item.setTitle("Expand playlist")

                    transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top)
                    transaction.replace(R.id.fragmentContainer, player)
                    transaction.commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(musicliststate==MusicListState.HIDDEN){
            menu?.findItem(R.id.menu_expand)?.setTitle("Expand Playlist")
        }
        else{
            menu?.findItem(R.id.menu_expand)?.setTitle("Hide Playlist")
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun InitializeSeekBar(){
        seekBar.max = mediaplayer.duration/1000
        runnable = Runnable {
            seekBar.progress = mediaplayer.currentPosition/1000
            handler.postDelayed(runnable, 1000)
        }
        handler
            .postDelayed(runnable,1000)
    }
}
