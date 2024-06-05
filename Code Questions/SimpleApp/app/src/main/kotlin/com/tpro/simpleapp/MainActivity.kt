package com.tpro.simpleapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tpro.simpleapp.data.entity.AudioResponse
import com.tpro.simpleapp.ui.AudioAdapter
import com.tpro.simpleapp.ui.AudioClickListener
import com.tpro.simpleapp.ui.AudioListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), AudioClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val audioViewModel: AudioListViewModel by viewModels { viewModelFactory }
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorView: TextView
    private lateinit var adapter: AudioAdapter
    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingAudio: AudioResponse? = null
    private var isMediaPlaying = false
    private val seekBarUpdateHandler = Handler(Looper.getMainLooper())
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                if (isMediaPlaying) {
                    adapter.updateSeekBar(it.currentPosition)
                    seekBarUpdateHandler.postDelayed(this, 1000)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.loading_bar)
        recyclerView = findViewById(R.id.recycler_view)
        errorView = findViewById(R.id.error_view)

        setupAudioListView()

        observeAudioState()
        audioViewModel.fetch()
    }

    private fun setupAudioListView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = AudioAdapter(this)
        recyclerView.adapter = adapter

        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun observeAudioState() {
        audioViewModel.audioLivedata.observe(this) { state ->
            if (state.isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

            if (state.errorMessage.isNotEmpty()) {
                showError(state.errorMessage)
            } else {
                errorView.visibility = View.GONE
            }

            if (state.audioList.isNotEmpty()) {
                showData(state.audioList)
            } else {
                recyclerView.visibility = View.GONE
                showError(getString(R.string.no_data))
            }
        }

    }

    private fun showError(errorMessage: String) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        errorView.text = errorMessage
    }

    private fun showData(audioList: List<AudioResponse>) {
        recyclerView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        adapter.submitList(audioList)
    }

    override fun onPlayPauseClicked(audio: AudioResponse, position: Int) {
        if (currentPlayingAudio == audio) {
            if (isMediaPlaying) {
                pauseAudio()
            } else {
                playAudio(audio)
            }
        } else {
            stopAudio()
            currentPlayingAudio = audio
            playAudio(audio)
        }
    }

    override fun onSeekBarChanged(audio: AudioResponse, progress: Int) {
        if (currentPlayingAudio == audio) {
            mediaPlayer?.seekTo(progress)
        }
    }

    private fun playAudio(audio: AudioResponse) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audio.audioUrl)
            setOnPreparedListener {
                it.start()
                isMediaPlaying = true
                adapter.updatePlayPauseState(currentPlayingAudio, isMediaPlaying, it.duration)
                seekBarUpdateHandler.post(updateSeekBarRunnable)
                progressBar.visibility = View.GONE
            }
            setOnCompletionListener {
                stopAudio()
            }
            prepareAsync()
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
        isMediaPlaying = false
        adapter.updatePlayPauseState(
            currentPlayingAudio,
            isMediaPlaying,
            mediaPlayer?.duration ?: 0
        )
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isMediaPlaying = false
        adapter.updatePlayPauseState(currentPlayingAudio, isMediaPlaying, 0)
        seekBarUpdateHandler.removeCallbacks(updateSeekBarRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
