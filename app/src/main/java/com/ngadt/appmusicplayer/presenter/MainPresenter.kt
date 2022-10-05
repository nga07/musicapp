import android.content.Context
import com.ngadt.appmusicplayer.repository.MusicRepository
import com.ngadt.appmusicplayer.repository.local.OnResultListener
import java.lang.Exception

class MainPresenter(
    private val view: MainActivityContract.View,
    private val musicRepository: MusicRepository
) : MainActivityContract.Presenter {

    override fun getLocalSongs(context: Context) {
        musicRepository.getDataLocal(context, object : OnResultListener<MutableList<Song>> {
            override fun onSuccess(data: MutableList<Song>) {
                view.getAllSongList(data)
            }

            override fun onError() {
                view.showErrorMessage()
            }

        })
    }

    override fun playMusic(status: Boolean) {
        if (status) view.setPlayButton() else view.setPauseButton()
    }

}
