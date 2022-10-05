package com.ngadt.appmusicplayer.repository

import Song
import android.content.Context
import com.ngadt.appmusicplayer.repository.local.OnResultListener
import com.ngadt.appmusicplayer.repository.source.MusicDataSource

class MusicRepository private constructor(private val local: MusicDataSource.Local) :
    MusicDataSource.Local {

    override fun getDataLocal(context: Context, listener: OnResultListener<MutableList<Song>>) {
        local.getDataLocal(context, listener)
    }

    companion object {
        private var instance: MusicRepository? = null
        fun getInstance(local: MusicDataSource.Local) = synchronized(this) {
            instance ?: MusicRepository(local).also { instance = it }
        }
    }
}
