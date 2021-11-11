package jp.live.dictoss.mytabbedapp1.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.live.dictoss.mytabbedapp1.MyItem
import jp.live.dictoss.mytabbedapp1.MyItemAdapter
import jp.live.dictoss.mytabbedapp1.R
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // RecyclerView
        var rv : RecyclerView? = binding.dashboardRecyclerView

        rv?.setHasFixedSize(true)
        rv?.layoutManager = LinearLayoutManager(this.requireContext())

        dashboardViewModel.items.observe(viewLifecycleOwner, Observer<List<MyItem>> { items ->
            // set data RecyclerView.
            //rv?.adapter = MyItemAdapter(emptyList())
            rv?.adapter = MyItemAdapter(items)
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}