package com.loopmoth.musicplayer

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_music_player.*
import java.io.File
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.PersistableBundle
import java.util.concurrent.TimeUnit
import android.util.Log
import android.widget.*


class MainActivity : AppCompatActivity(), music_player.Listener, BlankFragment.Listener {

    private var musicliststate = MusicListState.HIDDEN
    var musicplayerstate = PlayerState.NEW
    private lateinit var mediaplayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()
    var art: ByteArray? = null
    var musicPlayer = MusicPlayer()
    val listmusic = mutableListOf<String>()
    val pathlist = mutableListOf<String>()

    var player = music_player()
    var musicList = BlankFragment()
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val transaction = manager.beginTransaction()
        //transaction.add(R.id.fragmentContainer, player, "mp")
        //transaction.commit()

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
            musicPlayer.NextSong()
            musicplayerstate=PlayerState.SONGCHANGED
            playSong()
        }

        bPrevious.setOnClickListener {
            musicPlayer.PrevSong()
            musicplayerstate=PlayerState.SONGCHANGED
            playSong()
        }

        fragmentContainer.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                musicPlayer.NextSong()
                musicplayerstate=PlayerState.SONGCHANGED
                playSong()
            }
            override fun onSwipeRight() {
                musicPlayer.PrevSong()
                musicplayerstate=PlayerState.SONGCHANGED
                playSong()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        player = music_player()
        musicList = BlankFragment()

        getPlayList(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())
        //getPlayList(Environment.getExternalStorageDirectory().toString())
        //getPlayList(Environment.getExternalStorageDirectory().toString())
        musicPlayer.songs=pathlist

        if ((fragmentContainer as FrameLayout).childCount > 0)
            (fragmentContainer as FrameLayout).removeAllViewsInLayout()

        val transaction = manager.beginTransaction()
        if(musicliststate==MusicListState.HIDDEN){
            transaction.add(R.id.fragmentContainer, player, "mp")
        }
        else{
            transaction.add(R.id.fragmentContainer, musicList, "ml")
        }
        transaction.commit()
        transaction.runOnCommit {
            if(musicliststate==MusicListState.HIDDEN){
                val test = supportFragmentManager.findFragmentByTag("mp") as music_player?

                if(test!=null){
                    test.changeText(musicPlayer.getTitle(), musicPlayer.getArtist(), musicPlayer.getAlbum())
                    val icon = BitmapFactory.decodeResource(resources, R.mipmap.default_cover)
                    test.changeCover(icon)
                    try{
                        test.changeCover(musicPlayer.getCover())
                    }
                    catch (e: java.lang.Exception){

                    }
                }
            }else{
                val test = supportFragmentManager.findFragmentByTag("ml") as BlankFragment?

                if(test!=null){
                    try{
                        test.setList(listmusic)
                    }
                    catch (e: java.lang.Exception){

                    }
                }
            }
        }

        playSong()
        playSong()

        val test = supportFragmentManager.findFragmentByTag("mp") as music_player?
        if(test!=null){
            test.changeText(musicPlayer.getTitle(), musicPlayer.getArtist(), musicPlayer.getAlbum())
            val icon = BitmapFactory.decodeResource(resources, R.mipmap.default_cover)
            test.changeCover(icon)
            try{
                test.changeCover(musicPlayer.getCover())
            }
            catch (e: java.lang.Exception){

            }
        }
    }

    override fun getMusicList():MusicPlayer{
        //val frag = supportFragmentManager.findFragmentById(R.id.mtrl_child_content_container) as music_player
        //frag.changeText("test")
        return musicPlayer
    }

    override fun getMediaPlayer(): MediaPlayer {
        return mediaplayer
    }

    override fun setMusicPlayerState(state: PlayerState) {
        musicplayerstate = state
    }

    override fun removeHandler() {
        handler.removeCallbacks(runnable)
    }

    override fun setSong(index: Long) {
        musicPlayer.songindex=index.toInt()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_expand -> {
                if(musicliststate==MusicListState.HIDDEN){
                    val transaction = supportFragmentManager.beginTransaction()

                    musicliststate=MusicListState.EXPANDED
                    item.setTitle("Hide playlist")

                    transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top)
                    transaction.remove(player)
                    transaction.add(R.id.fragmentContainer, musicList, "ml")
                    transaction.commit()
                    transaction.runOnCommit {
                        val test = supportFragmentManager.findFragmentByTag("ml") as BlankFragment?

                        if(test!=null){
                            try{
                                test.setList(listmusic)
                            }
                            catch (e: java.lang.Exception){

                            }
                        }
                    }
                }
                else{
                    val transaction = supportFragmentManager.beginTransaction()

                    musicliststate=MusicListState.HIDDEN
                    item.setTitle("Expand playlist")

                    transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top)
                    transaction.remove(musicList)
                    transaction.add(R.id.fragmentContainer, player, "mp")
                    transaction.commit()
                    transaction.runOnCommit {
                        val test = supportFragmentManager.findFragmentByTag("mp") as music_player?

                        if(test!=null){
                            test.changeText(musicPlayer.getTitle(), musicPlayer.getArtist(), musicPlayer.getAlbum())
                            val icon = BitmapFactory.decodeResource(resources, R.mipmap.default_cover)
                            test.changeCover(icon)
                            try{
                                test.changeCover(musicPlayer.getCover())
                            }
                            catch (e: java.lang.Exception){

                            }
                        }
                    }
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
//        if(outState!=null){
//
//            outState.putInt("songindex", musicPlayer.songindex)
//            outState.putString("musicliststate", musicliststate.toString())
//            outState.putString("musicplayerstate", musicplayerstate.toString())
//            outState.putInt("currsec", mediaplayer.currentPosition)
//            mediaplayer.release()
//            handler.removeCallbacks(runnable)
//        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        if(savedInstanceState!=null){
//            getPlayList(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())
//            musicPlayer.songs=pathlist
//
//            musicPlayer.songindex = savedInstanceState.getInt("songindex")
//
//            playSong()
//            musicliststate=MusicListState.valueOf(savedInstanceState.getString("musicliststate"))
//            musicplayerstate=PlayerState.valueOf(savedInstanceState.getString("musicplayerstate"))
//            //musicPlayer.songindex = savedInstanceState.getInt("songindex")
//            mediaplayer.seekTo(savedInstanceState.getInt("currsec"))
//            //mediaplayer.currentPosition=savedInstanceState.getInt("currsec")
//            //playSong()
//            playSong()
//
//            val test = supportFragmentManager.findFragmentByTag("mp") as music_player?
//            if(test!=null){
//                test.changeText(musicPlayer.getTitle(), musicPlayer.getArtist(), musicPlayer.getAlbum())
//                val icon = BitmapFactory.decodeResource(resources, R.mipmap.default_cover)
//                test.changeCover(icon)
//                try{
//                    test.changeCover(musicPlayer.getCover())
//                }
//                catch (e: java.lang.Exception){
//
//                }
//            }
//        }
        super.onRestoreInstanceState(savedInstanceState)
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

    fun getPlayList(rootPath: String):MutableList<String> {

        var fileList = mutableListOf<String>()
        try {
            val rootFolder = File(rootPath)
            val files =
                rootFolder.listFiles() //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (file in files!!) {
                if (file.isDirectory) {
                    if (getPlayList(file.absolutePath) != mutableListOf("No items")) {
                        fileList.addAll(listOf(getPlayList(file.absolutePath).toString()))
                    } else {
                        break
                    }
                } else if (file.name.endsWith(".mp3")) {
                    listmusic.add(file.name)
                    pathlist.add(file.absolutePath)
                    val song = HashMap<String, String>()
                    //listmusic.add(file.name.toString())
                    song.put("file_name", file.name)
                    song.put("file_path", file.absolutePath)
                    fileList.add(song.toString())
                    //listmusic.add(song.toString())
                    /*for ((file_name, s) in song) {
                        //listmusic.add(s)
                       // fileList.add(s)           //te pętle wykonują się dwa razy...
                    }
                    for ((file_path, s) in song) {
                        pathlist.add(s)
                    }*/
                }
            }
            return fileList
            //tu filelist jest do przekazywania ścieżki
        } catch (e: Exception) {
            fileList=mutableListOf("No items")
            return fileList
        }
    }

    fun GetAbsolutePathOfSong(index: Int): String{
        val path=pathlist[index]
        return path
    }

    override fun playSong(){
        //Play song
        if(musicplayerstate==PlayerState.NEW){
            mediaplayer = MediaPlayer.create(applicationContext, musicPlayer.getUri())
            mediaplayer.start()
            InitializeSeekBar()
            bPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp)
            musicplayerstate = PlayerState.PLAYING
        }
        //Change song
        else if(musicplayerstate==PlayerState.SONGCHANGED){
            if(this::mediaplayer.isInitialized){
                mediaplayer.release()
            }
            musicplayerstate = PlayerState.NEW
            playSong()
        }
        //Pause song
        else if(musicplayerstate==PlayerState.PLAYING){
            mediaplayer.pause()
            musicplayerstate=PlayerState.PAUSED
            bPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp)
        }
        //Resume song
        else if(musicplayerstate==PlayerState.PAUSED){
            mediaplayer.start()
            musicplayerstate=PlayerState.PLAYING
            bPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp)
        }
        //if end of song
        mediaplayer.setOnCompletionListener {
            bPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp)
            musicPlayer.NextSong()
            musicplayerstate=PlayerState.SONGCHANGED
            mediaplayer.release()
            musicplayerstate=PlayerState.NEW
            handler.removeCallbacks(runnable)
            playSong()
        }

        val test = supportFragmentManager.findFragmentByTag("mp") as music_player?
        if(test!=null && test.isVisible){
            test.changeText(musicPlayer.getTitle(), musicPlayer.getArtist(), musicPlayer.getAlbum())
            val icon = BitmapFactory.decodeResource(resources, R.mipmap.default_cover)
            test.changeCover(icon)
            try{
                test.changeCover(musicPlayer.getCover())
            }
            catch (e: java.lang.Exception){

            }
        }
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

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
