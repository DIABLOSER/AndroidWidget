package com.daboluo.myapplication

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

/**
 * 作者：daboluo on 2025/2/22 18:19
 * Email:daboluo719@gmail.com
 */
class MyWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // 创建一个 Intent 用于返回应用
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            // 将 Intent 包装为 PendingIntent
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // 为小部件视图设置点击事件
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent)

            // 读取 SharedPreferences
            val message = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_MESSAGE, "Default Message")
            views.setTextViewText(R.id.widget_text, message)

            // 更新小部件
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        Log.d("MyWidget", "onUpdate called")
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("MyWidget", "MyWidget enabled")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("MyWidget", "MyWidget disabled")
    }
}








