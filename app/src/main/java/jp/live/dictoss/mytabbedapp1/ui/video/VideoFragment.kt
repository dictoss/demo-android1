package jp.live.dictoss.mytabbedapp1.ui.video

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.google.android.exoplayer2.MediaItem
import jp.live.dictoss.mytabbedapp1.R
import jp.live.dictoss.mytabbedapp1.databinding.FragmentHomeBinding
import jp.live.dictoss.mytabbedapp1.databinding.FragmentVideoBinding
import jp.live.dictoss.mytabbedapp1.ui.home.HomeViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import jp.live.dictoss.mytabbedapp1.MainActivity


class VideoFragment : Fragment() {

    private lateinit var videoViewModel: VideoViewModel
    private var _binding: FragmentVideoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: VideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_video, container, false)

        videoViewModel =
            ViewModelProvider(this).get(VideoViewModel::class.java)

        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val playerView: PlayerView = binding.playerView

        val player = SimpleExoPlayer.Builder(this.requireContext()).build()

        // MPEG-DASH
        //var streamingUri : String = "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"
        // HLS
        var streamingUri : String = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"

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