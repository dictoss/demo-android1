package jp.live.dictoss.mytabbedapp1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyItem (_title: String, _datetime: String){
    var title : String? = ""
    var datetime : String? = ""

    init {
        title = _title
        datetime = _datetime
    }
}

class MyItemAdapter (private val dataSet: List<MyItem>) :
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
        viewHolder.datetimeView.text = item?.datetime

        viewHolder.itemView.setOnClickListener {
            // 行をタップしたときに詳細画面を開きます
            Log.i("TAG", "clicked item")
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}