package com.ngadt.appmusicplayer.repository.source

import Song
import android.content.Context
import com.ngadt.appmusicplayer.repository.local.OnResultListener

interface MusicDataSource {
    interface Local {

        fun getDataLocal(context: Context,listener: OnResultListener<MutableList<Song>>)
    }
}
