import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore

object MyMusicPlayer {

    fun getBitmapSong(path: String): Bitmap {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(Uri.parse(path).toString())
        val byteImage = mmr.embeddedPicture
        return BitmapFactory.decodeByteArray(byteImage, 0, byteImage!!.size)
    }

    fun fetchSongFromStorage(context: Context): MutableList<Song> {
        val selection: String = MediaStore.Audio.Media.IS_MUSIC + " != 0 "
        val cursor: Cursor by lazy {
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null,
                null
            )!!
        }

        val songList: ArrayList<Song> = ArrayList()
        while (cursor.moveToNext()) {
            with(cursor) {
                val linkSong: String =
                    if (getColumnIndex(MediaStore.Audio.Media.DATA) < 0) "" else getString(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.DATA
                        )
                    )
                val imageID: Long =
                    if (getColumnIndex(MediaStore.Audio.Media.ALBUM_ID) < 0) 0 else getLong(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ALBUM_ID
                        )
                    )
                val nameSong: String =
                    if (getColumnIndex(MediaStore.Audio.Media.TITLE) < 0) "" else getString(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.TITLE
                        )
                    )
                val author: String =
                    if (getColumnIndex(MediaStore.Audio.Media.ARTIST) < 0) "" else getString(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ARTIST
                        )
                    )
                val duration: Long =
                    if (getColumnIndex(MediaStore.Audio.Media.DURATION) < 0) 0 else getLong(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.DURATION
                        )
                    )

                songList.add(Song(linkSong, imageID, nameSong, author, duration))
            }
        }

        return songList
    }
}
