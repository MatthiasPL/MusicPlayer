package com.loopmoth.musicplayer

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.sip.SipSession
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_music_player.*
import kotlinx.android.synthetic.main.fragment_music_player.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [music_player.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [music_player.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class music_player : Fragment() {

    var activityCallback: Listener? = null

    interface Listener{
        fun getMusicList(): MusicPlayer
    }

    private var root: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_music_player, container, false)  // initialize it here
        /*try{
            activityCallback = context as Listener
        }catch(e: ClassCastException){
            throw ClassCastException(context?.toString()+" must implement Listener")
        }*/
        return root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    fun changeText(name: String, artist: String, album: String){
        tvTitle.text=name
        tvArtist.text=artist
        tvAlbum.text=album
    }

    fun changeCover(songCover: Any){
            if(songCover is Bitmap){
                cover.setImageBitmap(songCover)
            }
            else{
                cover.setImageResource(R.mipmap.default_cover)
            }
    }
}
