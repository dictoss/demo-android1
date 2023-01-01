package jp.live.dictoss.mytabbedapp1.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.*
import com.squareup.picasso.Picasso
import jp.live.dictoss.mytabbedapp1.MyItem
import jp.live.dictoss.mytabbedapp1.R
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardDetailBinding

class DashboardDetailFragment : Fragment(), View.OnClickListener, PurchasesUpdatedListener {

    private lateinit var viewModel: DashboardDetailViewModel
    private var _binding: FragmentDashboardDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var item: MyItem? = null

    private var billingClient : BillingClient? = null
    private val testProductId = "jp.live.dictoss.app_0001"

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
    ): View {
        this.viewModel =
            ViewModelProvider(this).get(DashboardDetailViewModel::class.java)

        this._binding = FragmentDashboardDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonProductQuery = binding.fragmentProductQueryButton
        buttonProductQuery.setOnClickListener(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = this.arguments
        if (Build.VERSION.SDK_INT >= 33) {
            this.item = bundle?.getParcelable("item", MyItem::class.java)
        }
        else{
            this.item = bundle?.getParcelable("item")
        }

        val titleTextView: TextView? = view.findViewById(R.id.fragmentDetailTitleTextView)
        titleTextView?.text = this.item?.title

        val updateDateTextView: TextView? = view.findViewById(R.id.fragmentDetailUpdateDateTextView)
        updateDateTextView?.text = String.format("%s:%s",
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
            Picasso.get().load(this.item?.image_1)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .resize(320, 240)
                .centerCrop()
                .into(imageView)
        }

        // 課金アイテムの取得処理
        val billingClient: BillingClient = BillingClient.newBuilder(this.requireContext())
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i("TAG", String.format("onBillingSetupFinished() response code: %d", billingResult.responseCode))
                } else {
                    Log.w("TAG", String.format("onBillingSetupFinished() error code: %d", billingResult.responseCode))
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w("TAG", "onBillingServiceDisconnected()")
            }
        })

        this.billingClient = billingClient
    }

    override fun onClick(view: View) {
        Log.i("TAG","DashboardDetailFragment.onClick()")

        when (view.id) {
            R.id.fragmentProductQueryButton -> {
                this.querySkuDetails()
            }
        }
    }

    private fun querySkuDetails()
    {
        if (this.billingClient == null){
            return
        }

        try {
            val skuList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(this.testProductId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build())
            val params = QueryProductDetailsParams.newBuilder().setProductList(skuList)

            this.billingClient?.queryProductDetailsAsync(params.build()) {
                billingResult,
                productDetailsList -> run {
                    var s = ""

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.i("TAG", String.format("callback queryProductDetailsAsync() response code: %d", billingResult.responseCode))

                        s += "Success callback queryProductDetailsAsync()\n"
                        if (0 < productDetailsList.size) {
                            // it : productDetails
                            productDetailsList.forEach {
                                s += it.toString()
                                s += "\n"
                            }
                        } else {
                            s += "productDetailsList is zero.\n"
                        }
                    } else {
                        val errMsg: String = String.format("callback queryProductDetailsAsync() error code: %d", billingResult.responseCode)
                        Log.w("TAG", errMsg)
                        s = errMsg
                    }

                    this.activity?.runOnUiThread {
                        val multilineTextView: TextView? = this.view?.findViewById(R.id.fragmentProductInfoEditTextMultiLine)
                        multilineTextView?.text = s
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", String.format("ERROR on callback queryProductDetailsAsync() : %s ", e.message))
        } finally {
            //
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, list: MutableList<Purchase>?) {
    }
}