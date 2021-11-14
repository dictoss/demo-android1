package jp.live.dictoss.mytabbedapp1.ui.dashboard

import android.icu.util.TimeUnit
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.live.dictoss.mytabbedapp1.MyItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.*
import okhttp3.internal.wait
import okio.IOException

@Serializable
data class MyJsonData (
    val c_id: Int,
    val title: String,
    val content: String,
    val image_1: String,
    val create_date: String,
    val update_date: String
)

class DashboardViewModel : ViewModel() {

    private val _items : MutableLiveData<List<MyItem>> = MutableLiveData<List<MyItem>>()
    val items : LiveData<List<MyItem>> = _items

    private val connectTimeoutMill : Long = 10 * 1000
    private val readTimeoutMill : Long = 10 * 1000

    fun beginLoadItems()
    {
        try {
            // Requestを作成
            val targetUri: String = "http://www.pcdennokan.wjg.jp/proto/ajaxtest_list.json"

            val httpClient = OkHttpClient.Builder()
                .connectTimeout(
                    connectTimeoutMill,
                    java.util.concurrent.TimeUnit.MILLISECONDS
                )
                .readTimeout(
                    readTimeoutMill,
                    java.util.concurrent.TimeUnit.MILLISECONDS)
                .build()

            val request = Request.Builder()
                .url(targetUri)
                .build()

            // 非同期処理でHTTP通信を行う
            var call = httpClient.newCall(request).enqueue(object : Callback {

                // 正常応答時
                override fun onResponse(call: Call, response: Response) {
                    // Responseの読み出し
                    val responseBody = response.body?.string().orEmpty()

                    // レスポンスデータの取得
                    val jsonInst = Json {
                        ignoreUnknownKeys = true
                    }
                    val jsonObj = jsonInst.decodeFromString<List<MyJsonData>>(responseBody)
                    var dataset = mutableListOf<MyItem>()

                    for (i in jsonObj) {
                        var data: MyItem = MyItem(
                            i.c_id,
                            i.title,
                            i.content,
                            i.image_1,
                            i.update_date,
                            i.update_date)
                        dataset?.add(data)
                    }

                    _items.postValue(dataset)
                }

                // エラー応答時
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
    }
}