package jp.live.dictoss.mytabbedapp1.ui.video

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ui.StyledPlayerView

class VideoViewModel : ViewModel() {
    private val _video = MutableLiveData<StyledPlayerView>().apply {
        //value = "This is notifications Fragment"
        //value = MediaStore.Video()
    }
    val video: LiveData<StyledPlayerView> = _video
}