package com.tpro.simpleapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tpro.simpleapp.R
import com.tpro.simpleapp.data.entity.AudioResponse

class AudioAdapter(private val audioClickListener: AudioClickListener) :
    ListAdapter<AudioResponse, AudioAdapter.AudioViewHolder>(AudioDiffCallback()) {

    private var playingAudio: AudioResponse? = null
    private var isPlaying = false
    private var currentPosition = 0
    private var duration = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view, audioClickListener)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            playingAudio == getItem(position),
            isPlaying,
            currentPosition,
            duration
        )
    }

    class AudioViewHolder(itemView: View, private val audioClickListener: AudioClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.audio_name)
        @VisibleForTesting val playPauseButton: ImageButton = itemView.findViewById(R.id.play_pause_button)
        private val seekBar: SeekBar = itemView.findViewById(R.id.seek_bar)

        fun bind(
            audio: AudioResponse,
            isCurrentAudio: Boolean,
            isPlaying: Boolean,
            currentPosition: Int,
            duration: Int
        ){
            nameTextView.text = audio.name

            playPauseButton.setImageResource(
                if (isPlaying && isCurrentAudio) android.R.drawable.ic_media_pause
                else android.R.drawable.ic_media_play
            )

            if (isCurrentAudio) {
                seekBar.max = duration
                seekBar.progress = currentPosition
            } else {
                seekBar.progress = 0
            }

            playPauseButton.setOnClickListener {
                audioClickListener.onPlayPauseClicked(audio, adapterPosition)
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        audioClickListener.onSeekBarChanged(audio, progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    class AudioDiffCallback : DiffUtil.ItemCallback<AudioResponse>() {
        override fun areItemsTheSame(oldItem: AudioResponse, newItem: AudioResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AudioResponse, newItem: AudioResponse): Boolean {
            return oldItem == newItem
        }
    }

    fun updatePlayPauseState(audio: AudioResponse?, isPlaying: Boolean, duration: Int) {
        this.playingAudio = audio
        this.isPlaying = isPlaying
        this.duration = duration
        notifyDataSetChanged()
    }

    fun updateSeekBar(currentPosition: Int) {
        this.currentPosition = currentPosition
        notifyItemChanged(currentList.indexOf(playingAudio))
    }

}

interface AudioClickListener {
    fun onPlayPauseClicked(audio: AudioResponse, position: Int)
    fun onSeekBarChanged(audio: AudioResponse, progress: Int)
}
