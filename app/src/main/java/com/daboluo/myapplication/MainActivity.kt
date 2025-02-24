package com.daboluo.myapplication

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.daboluo.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            // 创建桌面组件
            createAppWidget()
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
}