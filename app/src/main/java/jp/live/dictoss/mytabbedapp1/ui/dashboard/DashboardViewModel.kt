package jp.live.dictoss.mytabbedapp1.ui.dashboard

import android.icu.util.TimeUnit
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.live.dictoss.mytabbedapp1.MyItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import okhttp3.*
import okhttp3.internal.wait
import okio.IOException

@Serializable
data class MyJsonData (
    val c_id: Int,
    val title: String,
    val content: String,
    val create_date: String,
    val update_date: String
)

class DashboardViewModel : ViewModel() {

    private val _items : MutableLiveData<List<MyItem>> by lazy {loadItems()}

    val items : LiveData<List<MyItem>> = _items

    private val connectTimeoutMill : Long = 10 * 1000
    private val readTimeoutMill : Long = 10 * 1000

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(
            connectTimeoutMill,
            java.util.concurrent.TimeUnit.MILLISECONDS
        )
        .readTimeout(
            readTimeoutMill,
            java.util.concurrent.TimeUnit.MILLISECONDS)
        .build()

    private fun loadItems() :
        MutableLiveData<List<MyItem>>
    {
        // Do an asynchronous operation to fetch users.
        var dataset : MutableList<MyItem>? = mutableListOf<MyItem>()

        //for (i in 0..5) {
        //    var data : MyItem = MyItem("title" + i, "datetime" + i)
        //    dataset?.add(data)
        //}
        //return MutableLiveData(dataset)

        try {
            // Requestを作成
            val targetUri : String = "http://www.pcdennokan.wjg.jp/proto/ajaxtest_list.json"
            //val targetUri : String = "http://www.pcdennokan.wjg.jp/proto/ajaxtest_1.json"
            val request = Request.Builder()
                .url(targetUri)
                .build()

            var call = httpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    // Responseの読み出し
                    val responseBody = response.body?.string().orEmpty()
                    // 必要に応じてCallback
                    // レスポンスデータの応答
                    val jsonObj = Json.decodeFromString<List<MyJsonData>>(responseBody)
                    for (i in jsonObj) {
                        var data : MyItem = MyItem(i.title, i.update_date)
                        dataset?.add(data)
                    }
                    //var data : MyItem = MyItem(jsonObj.title, jsonObj.update_date)
                    //dataset?.add(data)
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Error", e.toString())
                    // 必要に応じてCallback
                }
            })
        } catch (e: Exception) {
            Log.w("WARN", e.message ?: "")
        } finally {
            //
        }

        return MutableLiveData(dataset)
    }
}