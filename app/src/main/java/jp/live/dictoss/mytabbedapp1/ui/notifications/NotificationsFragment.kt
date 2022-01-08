package jp.live.dictoss.mytabbedapp1.ui.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jp.live.dictoss.mytabbedapp1.R
import jp.live.dictoss.mytabbedapp1.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment(), View.OnClickListener {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.fragmentNotificationsText
        notificationsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val button1 = binding.fragmentNotificationsButton1
        button1.setOnClickListener(this)

        notificationsViewModel.beginSetText(getString(R.string.fragment_notifications_text))
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View) {
        Log.i("TAG","NotificationsFragment.onClick()")

        when (view.id) {
            R.id.fragment_notifications_button1 -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
                builder.setTitle(R.string.mainactivity_button_msgbox_desu_title)
                builder.setMessage(R.string.mainactivity_button_msgbox_desu_msg)

                val dialog: AlertDialog? = builder.create()
                dialog?.show()
            }
        }
    }
}