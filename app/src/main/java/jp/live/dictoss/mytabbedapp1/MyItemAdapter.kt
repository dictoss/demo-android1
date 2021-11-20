package jp.live.dictoss.mytabbedapp1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import jp.live.dictoss.mytabbedapp1.databinding.FragmentHomeBinding
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardDetailBinding
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardBinding
import jp.live.dictoss.mytabbedapp1.MainActivity
import jp.live.dictoss.mytabbedapp1.placeholder.PlaceholderContent

class MyItem (
    _c_id: Int,
    _title: String,
    _content: String,
    _image_1: String,
    _create_date: String,
    _update_date: String,
){
    var c_id : Int? = 0
    var title : String? = ""
    var content : String? = ""
    var image_1 : String? = ""
    var create_date : String? = ""
    var update_date : String? = ""

    init {
        c_id = _c_id
        title = _title
        content = _content
        image_1 = _image_1
        create_date = _create_date
        update_date = _update_date
    }
}

class MyItemAdapter (private val dataSet: List<MyItem>, private val context: Context, private val parentFragment: Fragment) :

    RecyclerView.Adapter<MyItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView
        val datetimeView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            titleView = view.findViewById(R.id.textViewMyItemTitle)
            datetimeView = view.findViewById(R.id.textViewMyItemDatetime)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.myitem, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item: MyItem? = dataSet[position]

        viewHolder.titleView.text = item?.title
        viewHolder.datetimeView.text = item?.update_date


        viewHolder.itemView.setOnClickListener { itemView ->
            // 行をタップしたときに詳細画面を開きます
            Log.i("TAG", "clicked item (%d:%S)".format(position, item?.title))

            //val item = itemView.tag as PlaceholderContent.PlaceholderItem
            val bundle = Bundle()
            bundle.putInt(
                "id",
                position
            )
            bundle.putString(
                "title",
                item?.title
            )

            parentFragment.findNavController().navigate(R.id.dashboardDetailFragment2, bundle)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}