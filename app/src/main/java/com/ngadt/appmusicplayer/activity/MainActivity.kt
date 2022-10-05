package com.ngadt.appmusicplayer.activity

import Song
import SongAdapter
import MainPresenter
import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ngadt.appmusicplayer.R
import com.ngadt.appmusicplayer.databinding.ActivityMainBinding
import com.ngadt.appmusicplayer.repository.MusicRepository
import com.ngadt.appmusicplayer.repository.local.MusicLocalDataSource
import com.ngadt.appmusicplayer.service.MusicService

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainPresenter: MainPresenter =
        MainPresenter(this, MusicRepository.getInstance(MusicLocalDataSource.getInstance()))
    private var songs = mutableListOf<Song>()
    private var songAdapter = SongAdapter(::clickSong)
    private var currentSong = 0
    private var musicService: MusicService? = null
    private var boundService = false
    private val musicIntent by lazy {
        Intent(this, MusicService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        initData()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            val serviceBinder = binder as MusicService.MusicBinder
            musicService = serviceBinder.getService()
            boundService = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            boundService = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    private fun initData() {
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        initRequest()
        registerReceiver(musicReceiver, IntentFilter(getString(R.string.intent_action)))
    }

    fun initRequest() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                mainPresenter.getLocalSongs(this)
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private var musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.extras?.getString(getString(R.string.intent_data_extra))) {
                getString(R.string.intent_play_music) -> {
                    mainPresenter.playMusic(musicService?.isMusicPlaying() == true)
                    musicService?.pushNotification()
                }
                getString(R.string.intent_next_music) -> nextSongClick()
                getString(R.string.intent_prev_music) -> prevSongClick()
            }
        }

    }

    private fun prevSongClick() {
        currentSong = if (currentSong > 0) currentSong - 1 else songs.size - 1
        musicService?.switchMusic(currentSong)
        updateMusicName()
        musicService?.pushNotification()
    }

    private fun nextSongClick() {
        currentSong = if (currentSong < songs.size - 1) currentSong + 1 else 0
        musicService?.switchMusic(currentSong)
        updateMusicName()
        musicService?.pushNotification()
    }

    private fun updateMusicName() {
        binding.apply {
            tvTitleSong.text = songs[currentSong].nameSong
            tvAuthor.text = songs[currentSong].author
        }
    }

    private fun initViews() {
        binding.rcSong.adapter = songAdapter

        with(binding) {
            btnPlayPauseSong.setOnClickListener {
                mainPresenter?.playMusic(musicService?.isMusicPlaying() == true)
                musicService?.pushNotification()
            }
            btnNextSong.setOnClickListener {
                nextSongClick()
            }
            btnPreviousSong.setOnClickListener {
                prevSongClick()
            }
        }
    }

    override fun getAllSongList(songList: List<Song>) {
        if (songList != null) {
            songs = songList.toMutableList()
        }
        songAdapter.setDatatoList(songList.toMutableList())
        musicService?.setSongList(songList.toMutableList())
    }

    override fun showErrorMessage() {
        Toast.makeText(this, getString(R.string.tvNoSong), Toast.LENGTH_SHORT).show()
    }

    override fun showMiniPlayer() {
        binding.currentSongLayout.visibility = View.VISIBLE
    }

    override fun setPlayButton() {
        musicService?.pauseMusic()
        binding.btnPlayPauseSong.setImageResource(R.drawable.ic_baseline_play_circle_outline_64)
    }

    override fun setPauseButton() {
        musicService?.resumeMusic()
        binding.btnPlayPauseSong.setImageResource(R.drawable.ic_baseline_pause_circle_outline_64)
    }

    private fun clickSong(position: Int) {
        if (position != null) {
            currentSong = position
        }
        showMiniPlayer()
        musicService?.switchMusic(currentSong)
        updateMusicName()
        startService()
    }

    fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(musicService?.getIntent())
        } else {
            startService(musicService?.getIntent())
        }
    }

}
