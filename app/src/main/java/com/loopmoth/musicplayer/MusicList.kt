package com.loopmoth.musicplayer

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_music_list.*
import java.io.File
import java.nio.file.Files.isDirectory
import android.provider.MediaStore
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.provider.MediaStore.Audio
import android.widget.AdapterView
import android.app.Activity
import android.widget.AdapterView.OnItemClickListener












class MusicList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
    }


    val listmusic = mutableListOf<String>()
    val pathlist = mutableListOf<String>()

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



        override fun onResume() {
            super.onResume()

            val MusicList = findViewById<ListView>(R.id.MusicList)

            val content=getPlayList("/storage/emulated/0/Download/")

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listmusic)
            MusicList.adapter = adapter

            MusicList.setOnItemClickListener { parent, view, position, id ->
                GetAbsolutePathOfSong(id.toInt())
                // Tu zmiana utworu
            }
        }


    }
