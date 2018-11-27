package com.loopmoth.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import java.io.File
import java.nio.file.Files.isDirectory




class MusicList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
    }

    private val listmp3 = ArrayList<String>()
    var extensions = arrayOf("mp3")

    private fun loadmp3(YourFolderPath: String) {

        val file = File(YourFolderPath)
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null && files.size > 0) {
                for (f in files) {
                    if (f.isDirectory) {
                        loadmp3(f.absolutePath)
                    } else {
                        for (i in extensions.indices) {
                            if (f.absolutePath.endsWith(extensions[i])) {
                                listmp3.add(f.absolutePath)
                            }
                        }
                    }
                }
            }
        }

    }

    fun getPlayList(rootPath: String): MutableList<String>? {
        val fileList : MutableList<String> = mutableListOf()

        try {
            val rootFolder = File(rootPath)
            val files =
                rootFolder.listFiles() //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (file in files!!) {
                if (file.isDirectory) {
                    if (getPlayList(file.absolutePath) != null) {
                        fileList.addAll(listOf(getPlayList(file.absolutePath).toString()))
                    } else {
                        break
                    }
                } else if (file.name.endsWith(".mp3")) {
                    val song = HashMap<String,String>()
                    song.put("file_path", file.absolutePath)
                    song.put("file_name", file.name)
                    fileList.add(song.toString())
                }
            }
            return fileList
        } catch (e: Exception) {
            return null
        }

    }

    override fun onResume() {
        super.onResume()

        val MusicList=findViewById<ListView>(R.id.MusicList)



        /*val listItems: MutableList<String> = mutableListOf("song1", "song2", "song3","song4", "song5", "song6","song7", "song8", "song9",
            "song10","song11", "song12", "song13","song14", "song15", "song16","song17", "song18", "song19","song1", "song2", "song3","song4", "song5", "song6","song7", "song8", "song9",
            "song10","song11", "song12", "song13","song14", "song15", "song16","song17", "song18", "song19","song1", "song2", "song3","song4", "song5", "song6","song7", "song8", "song9",
            "song10","song11", "song12", "song13","song14", "song15", "song16","song17", "song18", "song19","song1", "song2", "song3","song4", "song5", "song6","song7", "song8", "song9",
            "song10","song11", "song12", "song13","song14", "song15", "song16","song17", "song18", "song19","song1", "song2", "song3","song4", "song5", "song6","song7", "song8", "song9",
            "song10","song11", "song12", "song13","song14", "song15", "song16","song17", "song18", "song19")*/

        //loadmp3("/mnt/sdcard/")
        //getPlayList(getExternalStorageDirectory().getAbsolutePath())

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getPlayList(getExternalStorageDirectory().getAbsolutePath()))
        MusicList.adapter = adapter
    }
}
