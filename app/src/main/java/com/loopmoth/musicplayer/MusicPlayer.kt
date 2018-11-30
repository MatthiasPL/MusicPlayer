package com.loopmoth.musicplayer

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import kotlinx.android.synthetic.main.fragment_music_player.*
import java.io.File


class MusicPlayer {
    var songtitle: String="Unknown Title"
    var songartist: String = "Unknown Title"
    var songalbum: String = "Unknown Album"
    lateinit var songcover: BitmapFactory

    var songindex: Int = 0
    var songs: MutableList<String> = mutableListOf("song.mp3", "metro.mp3")

    fun NextSong(activity: Activity){
        if(songindex>=songs.size-1){
            songindex=0
        }
        else{
            songindex++
        }
        (activity as MainActivity).musicplayerstate=PlayerState.SONGCHANGED
        //(activity as MainActivity).playSong()
    }

    fun PrevSong(activity: Activity){
        if(songindex<=0){
            songindex=songs.size-1
        }
        else{
            songindex--
        }
        (activity as MainActivity).musicplayerstate=PlayerState.SONGCHANGED
        //(activity as MainActivity).playSong()
    }
    fun getSongName():String{
        return songs[songindex]
    }

    fun getUri():Uri{
        val completePath = Environment.getExternalStorageDirectory().toString() + "/" + getSongName()
        val uri = Uri.fromFile(File(completePath))
        return uri
    }
}