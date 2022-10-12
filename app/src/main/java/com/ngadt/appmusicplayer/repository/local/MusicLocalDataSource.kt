package com.ngadt.appmusicplayer.repository.local

import MyMusicPlayer
import Song
import android.content.Context
import com.ngadt.appmusicplayer.repository.source.MusicDataSource
import java.lang.Exception

class MusicLocalDataSource : MusicDataSource.Local {

    override fun getDataLocal(context: Context, listener: OnResultListener<MutableList<Song>>) {
        val songList = MyMusicPlayer.fetchSongFromStorage(context)
        if (songList.size > 0) {
            listener.onSuccess(songList)
        } else {
            listener.onError()
        }
    }

    companion object {
        private var instance: MusicLocalDataSource? = null
        fun getInstance() = synchronized(this) {
            instance ?: MusicLocalDataSource().also { instance = it }
        }
    }
}
