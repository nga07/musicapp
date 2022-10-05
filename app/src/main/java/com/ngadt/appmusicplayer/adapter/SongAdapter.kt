import MyMusicPlayer.getBitmapSong
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ngadt.appmusicplayer.R
import com.ngadt.appmusicplayer.databinding.SongItemLayoutBinding

class SongAdapter(
    private val clickInterface: (Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var listSong = mutableListOf<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SongItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listSong[position])
    }

    override fun getItemCount(): Int = listSong.size

    fun setDatatoList(list: MutableList<Song>) {
        listSong.clear()
        listSong.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemBinding: SongItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindView(song: Song) {
            itemBinding.apply {
                tvTitleSong.text = song.nameSong
                tvAuthor.text = song.author
                try {
                    imgSong.setImageBitmap(getBitmapSong(song.linkSong))
                } catch (e: java.lang.Exception) {
                    imgSong.setImageResource(R.drawable.ic_baseline_music_note_24)
                }
                songItem.setOnClickListener {
                    clickInterface(adapterPosition)
                }
            }
        }
    }
}
