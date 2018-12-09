package com.loopmoth.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_blank.*
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BlankFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class BlankFragment : Fragment() {

    var activityCallback: Listener? = null

    interface Listener{
        fun getMusicList(): MusicPlayer
        fun getMediaPlayer(): MediaPlayer
    }

    val listmusic = mutableListOf<String>()
    var pathlist = mutableListOf<String>()

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

    companion object {

        fun newInstance(): BlankFragment {
            return BlankFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater?.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        /*try{
            activityCallback = context as Listener
            pathlist=activityCallback!!.getMusicList().songs
            //val centerlist = resources.getStringArray(R.array.region2)
            //var lv = view.findViewById<ListView>(R.layout.MusicList)

            val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, pathlist)
            MusicList.adapter = adapter
        }catch(e: ClassCastException){
            throw ClassCastException(context?.toString()+" must implement Listener")
        }*/
    }

    fun setList(list: List<String>){
        MusicList.setOnItemClickListener { parent, view, position, id ->
            id.toInt()
            Toast.makeText(context, id.toString(), Toast.LENGTH_LONG).show()
        }
        var songs = mutableListOf<String>()

        //for(i in 0..list.size){
        //    songs.add(list[i].substringAfterLast("/"))
        //}

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        MusicList.adapter = adapter
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
