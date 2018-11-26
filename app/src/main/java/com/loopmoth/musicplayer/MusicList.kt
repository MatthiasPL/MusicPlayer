package com.loopmoth.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class MusicList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
    }

    override fun onResume() {
        super.onResume()

        val MusicList=findViewById<ListView>(R.id.MusicList)
        //val textView=findViewById<TextView>(R.id.textView)

        /*val numbers: MutableList<Int> = mutableListOf(1, 2, 3)
        val readOnlyView: List<Int> = numbers
        println(numbers)        // prints "[1, 2, 3]"
        numbers.add(4)
        println(readOnlyView)   // prints "[1, 2, 3, 4]"
        textView.text=numbers.toString()*/

        val listItems: MutableList<String> = mutableListOf("song1", "song2", "song3")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        MusicList.adapter = adapter
    }
}
