package com.loopmoth.musicplayer

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_music_player.*
import java.io.File
import android.os.Environment
import android.graphics.BitmapFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var musicliststate = MusicListState.HIDDEN
    var musicplayerstate = PlayerState.NEW
    private lateinit var mediaplayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()
    var art: ByteArray? = null
    var musicPlayer = MusicPlayer()

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
                tvTime.text = formatTime(i*1000)
                tvRemainingTime.text = formatTime(mediaplayer.duration - i*1000)
                if (b) {
                    mediaplayer.seekTo(i * 1000)
                }
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
            playSong()
        }

        bNext.setOnClickListener {
            musicPlayer.NextSong(this)
            playSong()
        }

        bPrevious.setOnClickListener {
            musicPlayer.PrevSong(this)
            playSong()
        }
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
                    setTrackInfo(musicPlayer.getUri())
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

    fun setTrackInfo(audioFileUri: Uri) {
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(getRealPathFromURI(audioFileUri))
        try {
            art = metaRetriever.getEmbeddedPicture()
            val songImage = BitmapFactory.decodeByteArray(art, 0, art!!.size)
            cover.setImageBitmap(songImage)
            tvArtist.text = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            tvTitle.text = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            tvAlbum.text = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        } catch (e: Exception) {
            tvAlbum.text = "Unknown Album"
            tvArtist.text = "Unknown Artist"
            tvTitle.text = "Unknown Title"
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val myFile = File(uri.path!!.toString())
        return myFile.getAbsolutePath()
    }

    fun playSong(){
        //Play song
        if(musicplayerstate==PlayerState.NEW){
            mediaplayer = MediaPlayer.create(applicationContext, musicPlayer.getUri())
            mediaplayer.start()
            setTrackInfo(musicPlayer.getUri())
            InitializeSeekBar()
            bPlay.text = "pause"
            musicplayerstate = PlayerState.PLAYING
        }
        //Change song
        else if(musicplayerstate==PlayerState.SONGCHANGED){
            mediaplayer.release()
            musicplayerstate = PlayerState.NEW
            playSong()
        }
        //Pause song
        else if(musicplayerstate==PlayerState.PLAYING){
            mediaplayer.pause()
            musicplayerstate=PlayerState.PAUSED
            bPlay.text = "play"
        }
        //Resume song
        else if(musicplayerstate==PlayerState.PAUSED){
            mediaplayer.start()
            musicplayerstate=PlayerState.PLAYING
            bPlay.text = "pause"
        }
        //if end of song
        mediaplayer.setOnCompletionListener {
            bPlay.text="Pause"
            musicPlayer.NextSong(this)
            mediaplayer.release()
            musicplayerstate=PlayerState.NEW
            handler.removeCallbacks(runnable)
            playSong()
        }

        /*if(musicplayerstate==PlayerState.NEW||musicplayerstate==PlayerState.SONGCHANGED){
            if(musicplayerstate==PlayerState.SONGCHANGED){
                mediaplayer.release()
                //mediaplayer.stop()
                musicplayerstate=PlayerState.NEW
                handler.removeCallbacks(runnable)
            }
            //val fileName = "song.mp3"
            val fileName = musicPlayer.getSongName()
            val completePath = Environment.getExternalStorageDirectory().toString() + "/" + fileName
            //tvTitle.text=completePath

            val file = File(completePath)
            val uri1 = Uri.fromFile(file)

            mediaplayer = MediaPlayer.create(applicationContext, uri1)
            mediaplayer.start()
            setTrackInfo(uri1)

            InitializeSeekBar()

            bPlay.text="Pause"
            musicplayerstate=PlayerState.PLAYING
        }
        else if (musicplayerstate==PlayerState.PLAYING){
            //mediaplayer.pause()
            musicplayerstate=PlayerState.PAUSED
            bPlay.text="Play"
        }
        else if(musicplayerstate==PlayerState.PAUSED){
            //mediaplayer.start()
            musicplayerstate=PlayerState.PLAYING
            bPlay.text="Pause"
        }*/
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(i: Int):String{
        val millis: Long = i.toLong()
        var stringTime = ""
        if(i>3600000){
            stringTime = java.lang.String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
        }else{
            stringTime = java.lang.String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
        }

        return stringTime
    }
}
