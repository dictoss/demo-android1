package jp.live.dictoss.mytabbedapp1.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import jp.live.dictoss.mytabbedapp1.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private lateinit var videoViewModel: VideoViewModel
    private var _binding: FragmentVideoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private var viewModel: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return inflater.inflate(R.layout.fragment_video, container, false)

        videoViewModel =
            ViewModelProvider(this).get(VideoViewModel::class.java)

        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val playerView: StyledPlayerView = binding.playerView

        val player = ExoPlayer.Builder(this.requireContext()).build()

        // MPEG-DASH
        //var streamingUri : String = "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"
        // HLS
        val streamingUri = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"

        player.setMediaItem(MediaItem.fromUri(streamingUri))
        player.prepare()
        player.playWhenReady = false
        playerView.player = player

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}