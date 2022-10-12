import android.content.ContentResolver
import android.content.Context

class MainActivityContract {

    interface Presenter {
        fun getLocalSongs(context: Context)
        fun playMusic(status: Boolean)
    }

    interface View {
        fun getAllSongList(songList: List<Song>)
        fun showErrorMessage()
        fun showMiniPlayer()
        fun setPlayButton()
        fun setPauseButton()
    }
}
