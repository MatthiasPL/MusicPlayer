package com.loopmoth.musicplayer

class AudioModel {
    var aPath: String=""
    var aName: String=""
    var aAlbum: String=""
    var aArtist: String=""

    fun getaPath(): String {
        return aPath
    }

    fun setaPath(aPath: String) {
        this.aPath = aPath
    }

    fun getaName(): String {
        return aName
    }

    fun setaName(aName: String) {
        this.aName = aName
    }

    fun getaAlbum(): String {
        return aAlbum
    }

    fun setaAlbum(aAlbum: String) {
        this.aAlbum = aAlbum
    }

    fun getaArtist(): String {
        return aArtist
    }

    fun setaArtist(aArtist: String) {
        this.aArtist = aArtist
    }
}