package jp.live.dictoss.mytabbedapp1.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.live.dictoss.mytabbedapp1.MainActivity
import jp.live.dictoss.mytabbedapp1.MyItemAdapter
import jp.live.dictoss.mytabbedapp1.R
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_dashboard_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // RecyclerView
        val rv : RecyclerView = binding.dashboardRecyclerView
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this.requireContext())

        // When swipe, reload list data.
        binding.swipedLayout.setOnRefreshListener {
            dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

            dashboardViewModel.beginLoadItems(requireContext())
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_dashboard_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home-> {
                        val activity = requireActivity() as MainActivity
                        activity.onSupportNavigateUp()
                    }
                    R.id.menu_reload-> {
                        Log.i("TAG", "IN onOptionsItemSelected(): reload")
                        dashboardViewModel.beginLoadItems(requireContext())
                        binding.progressBar.visibility = android.widget.ProgressBar.VISIBLE
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        dashboardViewModel.items.observe(viewLifecycleOwner, { items ->
            //
            // ここは画面を縦と横を変更するともこの処理が再度実行される。
            //
            binding.progressBar.visibility = android.widget.ProgressBar.INVISIBLE
            binding.swipedLayout.isRefreshing = false

            val rv : RecyclerView = binding.dashboardRecyclerView
            // set data RecyclerView.
            //rv?.adapter = MyItemAdapter(emptyList())
            rv.adapter = MyItemAdapter(items, this.requireContext(), this)

            // notify message
            val text = R.string.dashboard_load_done
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(this.requireContext(), text, duration)
            toast.show()
        })

        if (dashboardViewModel.items.value == null) {
            dashboardViewModel.beginLoadItems(this.requireContext())
            binding.progressBar.visibility = android.widget.ProgressBar.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}