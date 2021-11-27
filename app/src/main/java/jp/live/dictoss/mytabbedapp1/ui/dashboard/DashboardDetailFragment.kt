package jp.live.dictoss.mytabbedapp1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardDetailBinding

class DashboardDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardDetailFragment()
    }

    private lateinit var viewModel: DashboardDetailViewModel
    private var _binding: FragmentDashboardDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var item: MyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.viewModel =
            ViewModelProvider(this).get(DashboardDetailViewModel::class.java)

        this._binding = FragmentDashboardDetailBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.fragment_dashboard_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        this.item = bundle?.getParcelable("item")

        val titleTextView: TextView? = view.findViewById(R.id.fragmentDetailTitleTextView)
        titleTextView?.text = this.item?.title

        val updatedateTextView: TextView? = view.findViewById(R.id.fragmentDetailUpdateDateTextView)
        updatedateTextView?.text = String.format("%s:%s",
            getString(R.string.fragment_notifications_static_update_date),
            this.item?.update_date)

        val contentTextView: TextView? = view.findViewById(R.id.fragmentDetailContentTextView)
        contentTextView?.text = this.item?.content

        val imageView: ImageView = view.findViewById(R.id.fragmentDetailImageView)
        if (this.item?.image_1?.isEmpty() == true) {
            // 画像がないため、ない画像をセット
            imageView.setImageResource(android.R.drawable.ic_menu_report_image)
        }
        else{
            // 読み込み中の画像をセット
            imageView.setImageResource(android.R.drawable.stat_notify_sync_noanim)

            // 非同期で画像をダウンロードして表示します。
            var w: Int = imageView.width
            w = 320
            var h: Int = imageView.height
            h = 240

            Picasso.get().load(this.item?.image_1)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .resize(w, h)
                .centerCrop()
                .into(imageView)
        }
    }
}