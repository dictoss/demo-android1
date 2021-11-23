package jp.live.dictoss.mytabbedapp1

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.live.dictoss.mytabbedapp1.databinding.FragmentHomeBinding
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardDetailBinding
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardBinding
import jp.live.dictoss.mytabbedapp1.MainActivity
import jp.live.dictoss.mytabbedapp1.placeholder.PlaceholderContent
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class JsonMyItem (
    val c_id: Int,
    val title: String,
    val content: String,
    val image_1: String,
    val create_date: String,
    val update_date: String
)

@Parcelize
data class MyItem (
    val c_id: Int,
    val title: String,
    val content: String,
    val image_1: String,
    val create_date: String,
    val update_date: String
) : Parcelable

class MyItemAdapter (private val dataSet: List<MyItem>, private val context: Context, private val parentFragment: Fragment) :

    RecyclerView.Adapter<MyItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        val titleView: TextView
        val datetimeView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            imageView = view.findViewById(R.id.imageViewMyItemListCol)
            titleView = view.findViewById(R.id.textViewTitleMyItemListCol)
            datetimeView = view.findViewById(R.id.textViewUpdatedateMyItemListCol)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.myitem_list_col, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item: MyItem? = dataSet[position]

        viewHolder.titleView.text = item?.title
        viewHolder.datetimeView.text = item?.update_date

        if (item?.image_1?.isEmpty() == true) {
            // 画像がないため、ない画像をセット
            viewHolder.imageView.setImageResource(android.R.drawable.ic_menu_report_image)
        }
        else{
            // 読み込み中の画像をセット
            viewHolder.imageView.setImageResource(android.R.drawable.stat_notify_sync_noanim)

            // 非同期で画像をダウンロードして表示します。
            var w: Int = viewHolder.imageView.width
            w = 320
            var h: Int = viewHolder.imageView.height
            h = 240

            Picasso.get().load(item?.image_1)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .resize(w, h)
                .centerCrop()
                .into(viewHolder.imageView)
        }

        viewHolder.itemView.setOnClickListener { itemView ->
            // 行をタップしたときに詳細画面を開きます
            Log.i("TAG", "clicked item (%d:%S)".format(position, item?.title))

            val bundle = Bundle()
            bundle.putParcelable("item", item)

            parentFragment.findNavController().navigate(R.id.dashboardDetailFragment2, bundle)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}