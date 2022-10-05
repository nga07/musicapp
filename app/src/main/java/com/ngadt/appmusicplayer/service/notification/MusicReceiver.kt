package com.ngadt.appmusicplayer.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ngadt.appmusicplayer.R

class MusicReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {
        p0?.sendBroadcast(
            Intent(p0?.getString(R.string.intent_action)).putExtra(
                p0.getString(R.string.intent_data_extra),
                intent?.action
            )
        )
    }

}
