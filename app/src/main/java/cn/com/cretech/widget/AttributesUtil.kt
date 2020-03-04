package cn.com.cretech.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import cn.com.cretech.util.StringUtils
import org.jsoup.Jsoup

class AttributesUtil {
    class MJavascriptInterface(private val context: Context) {

        @android.webkit.JavascriptInterface
        fun openImage(img: String, imageUrls: Array<String>, position: Int) {
            Log.i("img", img)
        }
    }

    class MyWebViewClient : WebViewClient() {

        @SuppressLint("NewApi")
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            if (request != null && request.url != null) {
                val scheme = request.url.scheme!!.trim { it <= ' ' }
                if (scheme.equals("http", ignoreCase = true) || scheme.equals(
                        "https",
                        ignoreCase = true
                    )
                ) {
                    return super.shouldInterceptRequest(view, object : WebResourceRequest {
                        override fun getUrl(): Uri {
                            return Uri.parse(injectIsParams(request.url.toString()))
                        }

                        @SuppressLint("NewApi")
                        override fun isForMainFrame(): Boolean {
                            return request.isForMainFrame
                        }

                        override fun isRedirect(): Boolean {
                            return false

                        }

                        @SuppressLint("NewApi")
                        override fun hasGesture(): Boolean {
                            return request.hasGesture()
                        }

                        @SuppressLint("NewApi")
                        override fun getMethod(): String {
                            return request.method
                        }

                        @SuppressLint("NewApi")
                        override fun getRequestHeaders(): Map<String, String> {

                            return request.requestHeaders
                        }
                    })
                }
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()  // 接受所有网站的证书
            super.onReceivedSslError(view, handler, error)
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            Log.i("H5Activity", error.toString())
            super.onReceivedError(view, request, error)
        }

        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {

            if (!StringUtils.isEmpty(url) && Uri.parse(url).scheme != null) {
                val scheme = Uri.parse(url).scheme!!.trim { it <= ' ' }
                if (url.contains("m3u8")) {
                    ListenerHandlers(context).setPlay(url, false)
                    (context as FragmentActivity).finish()

                }
                if (scheme.equals("http", ignoreCase = true) || scheme.equals(
                        "https",
                        ignoreCase = true
                    )
                ) {
                    return super.shouldInterceptRequest(view, injectIsParams(url))
                }
            }

            return super.shouldInterceptRequest(view, url)
        }


        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            if (url.contains("m3u8")) {
                ListenerHandlers(context).setPlay(url, false)
                (context as FragmentActivity).finish()
                return true
            } else {
                view.loadUrl(url)
                return true
            }
        }

        /*override fun onPageFinished(view: WebView, url: String) {
            view.settings.javaScriptEnabled = true
            super.onPageFinished(view, url)
            *//* ToastUtils.toast("加载完成");
            webView.setAllImage();*//*
            addImageClickListener(view)//待网页加载完全后设置图片点击的监听方法
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            view.settings.javaScriptEnabled = true

            super.onPageStarted(view, url, favicon)
        }*/

        override fun onLoadResource(view: WebView, url: String) {
            super.onLoadResource(view, url)
        }

        private fun addImageClickListener(webView: WebView) {
            webView.loadUrl(
                "javascript:(function(){ " + "var objs = document.getElementsByTagName(\"img\");"
                        + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ " + "array[j]=objs[j].src;" + " }  "
                        + "for(var i=0;i<objs.length;i++){"
                        + "objs[i].i=i;"
                        + "objs[i].onclick=function(){  window.imagelistener.openImage(this.src,array,this.i);" + "}  " + "}    })()"
            )

        }
    }

    private class MyWebChormClient(var progressBar: ProgressBar) :
        WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            // TODO 自动生成的方法存根

            if (newProgress == 100) {
                progressBar.visibility = View.GONE//加载完网页进度条消失
            } else {
                progressBar.visibility = View.VISIBLE//开始加载网页时显示进度条
                progressBar.progress = newProgress//设置进度值
            }

        }


        override fun getVideoLoadingProgressView(): View? {
            return super.getVideoLoadingProgressView()
        }
    }

    /**
     * 网页展示
     * @param webView 网页空间
     * @param
     */
    fun HtmlWebViewContent(webView: WebView, url: String) {
        val webSettings = webView.settings

        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.useWideViewPort = true//设置webview推荐使用的窗口
        webSettings.loadWithOverviewMode = true//设置webview加载的页面的模式
        // webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.javaScriptEnabled = true // 设置支持javascript脚本
        webSettings.allowFileAccess = true // 允许访问文件
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.setSupportZoom(true) // 支持缩放

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.loadWithOverviewMode = true
        //WebView加载web资源
        webView.loadUrl(url)
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url)
                return true
            }
        }
    }

    interface onWebViewLoadListener {
        fun onPageStarted()
        fun onPageFinished()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("SetJavaScriptEnabled")
        fun initWebView(context: Context, webview: WebView, url: String, progressBar: ProgressBar) {
            this.context = context
            webview.setInitialScale(28)
            val settings = webview.settings
            settings.useWideViewPort = true
            // settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            settings.loadWithOverviewMode = true
            //settings.setTextSize(WebSettings.TextSize.LARGEST);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                settings.mediaPlaybackRequiresUserGesture = false
            }
            settings.javaScriptEnabled = true
            /*settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);*/
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webview.loadUrl(url)
            webview.webViewClient = MyWebViewClient()
            webview.webChromeClient = (MyWebChormClient(progressBar))
            webview.addJavascriptInterface(MJavascriptInterface(context), "imagelistener")

        }

        fun injectIsParams(url: String?): String? {
            return if (url != null && !url.contains("xxx=")) {
                if (url.contains("?")) {
                    "$url&xxx=1"
                } else {
                    "$url?xxx=1"
                }
            } else {
                url
            }
        }

        @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
        fun appendWebViewContent(webView: WebView, webSetting: WebSettings, content: String) {
            val doc = Jsoup.parse(content)
            // webview的背景颜色设置透明
            webView.setBackgroundColor(0)
            // 设置图片自适应
            webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            // 添加图片点击事件
            webSetting.javaScriptEnabled = true

            webView.loadDataWithBaseURL(
                "http://sns1.thinksns.com",
                doc.toString(),
                "text/html",
                "utf-8",
                ""
            )
        }

        /**
         * 是否存在sd卡
         * @return
         */
        val isExitsSdcard: Boolean
            get() = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                true
            } else
                false
    }
}
