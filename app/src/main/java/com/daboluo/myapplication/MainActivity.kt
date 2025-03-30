package com.daboluo.myapplication

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.daboluo.myapplication.databinding.ActivityMainBinding

const val PREFS_NAME = "MyWidgetPrefs"
const val KEY_MESSAGE = "message"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            // 创建桌面组件
            createAppWidget()
        }

        binding.button2.setOnClickListener {
            // 更新 SharedPreferences
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                putString(KEY_MESSAGE, binding.editTextText.text.toString())
                apply()
            }
            // 更新小部件
            updateWidget()
        }
    }

    private fun createAppWidget() {
        try {
            // 获取 AppWidgetManager 实例
            val appWidgetManager = AppWidgetManager.getInstance(this)
            // 获取 AppWidgetProvider 的 ComponentName
            val componentName = ComponentName(this, MyWidget::class.java)
            // 检查是否有可用的 AppWidget 提供者
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                // 请求 Pin AppWidget
                appWidgetManager.requestPinAppWidget(componentName, null, null)
                Toast.makeText(this, "桌面组件添加成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "桌面组件添加失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // 捕获并记录异常
            Toast.makeText(this, "操作失败:${e.message.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, MyWidget::class.java))
        for (appWidgetId in appWidgetIds) {
            Log.d("MainActivity", "updateWidget: appWidgetId=$appWidgetId")
            val views = RemoteViews(packageName, R.layout.widget_layout)
            val message = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_MESSAGE, "Default Message")
            views.setTextViewText(R.id.widget_text, message)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}


