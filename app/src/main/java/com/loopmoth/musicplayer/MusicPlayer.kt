package com.loopmoth.musicplayer

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import kotlinx.android.synthetic.main.fragment_music_player.*
import java.io.File
import java.lang.Exception


class MusicPlayer {
    //var songtitle: String="Unknown Title"
    var songartist: String = "Unknown Artist"
    var songalbum: String = "Unknown Album"
    lateinit var songcover: BitmapFactory

    var songindex: Int = 0
    //var songs: MutableList<String> = mutableListOf("metro.mp3", "song.mp3")
    var songs = mutableListOf<String>()

    fun getTitle(): String{

        try{
            val metaRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(getRealPathFromURI(getUri()))
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        }
        catch (e: Exception){
            return getSongName().substringAfterLast("/")
        }

    }
    fun getArtist(): String{

        try{
            val metaRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(getRealPathFromURI(getUri()))
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

        }
        catch (e: Exception){
            return songartist
        }
    }
    fun getAlbum(): String{
        try{
            val metaRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(getRealPathFromURI(getUri()))
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        }
        catch(e:Exception){
            return songalbum
        }
    }
    fun getCover():Bitmap{
        val metaRetriever = MediaMetadataRetriever()
        var art: ByteArray? = null
        metaRetriever.setDataSource(getRealPathFromURI(getUri()))

        art = metaRetriever.getEmbeddedPicture()
        if(art!=null){
            val songImage = BitmapFactory.decodeByteArray(art, 0, art!!.size)
            return songImage
        }
        else{
            val bm = BitmapFactory.decodeResource(Resources.getSystem(),R.mipmap.default_cover)
            return bm
        }
    }

    fun NextSong(){
        if(songindex>=songs.size-1){
            songindex=0
        }
        else{
            songindex++
        }
        //(activity as MainActivity).musicplayerstate=PlayerState.SONGCHANGED
        //(activity as MainActivity).playSong()
    }

    fun PrevSong(){
        if(songindex<=0){
            songindex=songs.size-1
        }
        else{
            songindex--
        }
        //(activity as MainActivity).musicplayerstate=PlayerState.SONGCHANGED
        //(activity as MainActivity).playSong()
    }
    fun getSongName():String{
        return songs[songindex]
    }

    fun getUri():Uri{
        //val completePath = Environment.getExternalStorageDirectory().toString() + "/" + getSongName()
        val completePath = getSongName()
        val uri = Uri.fromFile(File(completePath))
        return uri
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val myFile = File(uri.path!!.toString())
        return myFile.getAbsolutePath()
    }
}