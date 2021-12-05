package jp.live.dictoss.mytabbedapp1.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import jp.live.dictoss.mytabbedapp1.JsonMyItem
import jp.live.dictoss.mytabbedapp1.MyItem
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import okhttp3.*
import okio.IOException

class DashboardViewModel : ViewModel() {

    private val _items : MutableLiveData<List<MyItem>> = MutableLiveData<List<MyItem>>()
    val items : LiveData<List<MyItem>> = _items

    fun beginLoadItems(context: Context)
    {
        try {
            // Requestを作成
            val targetUri: String = "http://www.pcdennokan.wjg.jp/proto/ajaxtest_list.json"

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val timeoutConnStr: String = sharedPreferences.getString("edit_text_preference_network_connect_timeout", "10000") ?: "10000"
            val timeoutReadStr: String = sharedPreferences.getString("edit_text_preference_network_read_timeout", "15000") ?: "15000"

            val httpClient = OkHttpClient.Builder()
                .connectTimeout(
                    timeoutConnStr.toLong(),
                    java.util.concurrent.TimeUnit.MILLISECONDS
                )
                .readTimeout(
                    timeoutReadStr.toLong(),
                    java.util.concurrent.TimeUnit.MILLISECONDS)
                .build()

            val request = Request.Builder()
                .url(targetUri)
                .build()

            // 非同期処理でHTTP通信を行う
            httpClient.newCall(request).enqueue(object : Callback {

                // 正常応答時
                override fun onResponse(call: Call, response: Response) {
                    // Responseの読み出し
                    val responseBody = response.body?.string().orEmpty()

                    // レスポンスデータの取得
                    val jsonInst = Json {
                        ignoreUnknownKeys = true
                    }
                    val jsonObj = jsonInst.decodeFromString<List<JsonMyItem>>(responseBody)
                    val dataset = mutableListOf<MyItem>()

                    for (i in jsonObj) {
                        val data = MyItem(
                            i.c_id,
                            i.title,
                            i.content,
                            i.image_1,
                            i.update_date,
                            i.update_date)
                        dataset.add(data)
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