# AndroidWidget
应黄小贝要求实现安卓一键添加桌面组件。特别强调必须是Widget！Widget！Widget！
# 增加更新组件数据

附上安卓官方开发文档[构建Widget托管应用](https://developer.android.com/develop/ui/views/appwidgets/host?hl=zh-cn)
# 截图效果
<img src="https://github.com/DIABLOSER/AndroidWidget/blob/main/raw/raw.png" alt="截图效果" width="400"/>

# 实现步骤
Step.1

定义一个自定义的 AppWidgetProvider 类 MyWidget。AppWidgetProvider 是一个广播接收器，负责处理与小部件相关的操作，比如更新小部件的内容或响应用户交互。
```
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
            // 更新小部件
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
```
Step.2

在你的Activity/Fragment中实现创建方法

```
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
```
Step.3

创建一个自定组件的xml文件：widget_layout.xml
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/circle_bg"
    xmlns:app="http://schemas.android.com/apk/res-auto">

       <TextView
           android:id="@+id/widget_text"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="这是一个桌面组件"
           android:textSize="18sp"
           android:layout_centerInParent="true" />

</RelativeLayout>
```
Step.4
在xml文件夹下配置widget_info.xml文件

```
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:targetCellHeight="2"
    android:targetCellWidth="2"
    android:updatePeriodMillis="86400000"
    android:initialLayout="@layout/widget_layout"
    android:resizeMode="horizontal|vertical"
    android:widgetCategory="home_screen"
    tools:targetApi="s" />
```
Step.5
声明组件接收器

```
<receiver
  android:name=".MyWidget"
  android:exported="true">
    <intent-filter>
      <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
      android:name="android.appwidget.provider"
      android:resource="@xml/widget_info" />
</receiver>
```
