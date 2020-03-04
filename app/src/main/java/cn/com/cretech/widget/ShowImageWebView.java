package cn.com.cretech.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 可以实现点击图片进行保存的 WebView
 */
public class ShowImageWebView extends WebView {

    private List<String> listImgSrc = new ArrayList<String>();
    // 获取img标签正则
    private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMAGE_URL_CONTENT = "http:\"?(.*?)(\"|>|\\s+)";

    private String url;
    private String longClickUrl;
    private Context context;

    public ShowImageWebView(Context context) {
        super(context);
        init(context);
    }

    public ShowImageWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowImageWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context=context;
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                 setWebImageLongClickListener(v);
                return false;
            }
        });


        WebSettings mSettings = this.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mSettings.setJavaScriptEnabled(true);//开启javascript
        mSettings.setDomStorageEnabled(true);//开启DOM
        mSettings.setDefaultTextEncodingName("utf-8");//设置字符编码
        //设置web页面
        mSettings.setAllowFileAccess(true);//设置支持文件流
        mSettings.setSupportZoom(true);// 支持缩放
        mSettings.setBuiltInZoomControls(true);// 支持缩放
        mSettings.setUseWideViewPort(true);// 调整到适合webview大小
        mSettings.setLoadWithOverviewMode(true);// 调整到适合webview大小
        mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
        //提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片
        mSettings.setBlockNetworkImage(false);
        //不使用缓存:
        mSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            mSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        mSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; HUAWEI MLA-AL10 Build/HUAWEIMLA-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/56.0.2924.87 Mobile Safari/537.36");
        mSettings.setAppCacheEnabled(true);
        mSettings.setDomStorageEnabled(true);
        mSettings.supportMultipleWindows();
        mSettings.setAllowContentAccess(true);
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setSavePassword(true);
        mSettings.setSaveFormData(true);
        mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mSettings.setLoadsImagesAutomatically(true);


        //载入js
        this.addJavascriptInterface(new MyJavascriptInterface(context), "allimage");
        //获取 html
        //this.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
    }

    /**
     * 响应长按点击事件
     * @param v
     */
    private void setWebImageLongClickListener(View v) {
        // 长按事件监听（注意：需要实现LongClickCallBack接口并传入对象）
        final HitTestResult htr = getHitTestResult();//获取所点击的内容
        if (htr.getType() == HitTestResult.IMAGE_TYPE
                || htr.getType() == HitTestResult.IMAGE_ANCHOR_TYPE
                || htr.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            //判断被点击的类型为图片
            if (mCallBack!=null) {
                longClickUrl = htr.getExtra();
                mCallBack.onLongClickCallBack(longClickUrl);
            }
        }
    }

    /**
     * 解析 HTML 该方法在 setWebViewClient 的 onPageFinished 方法中进行调用
     * @param view
     */
    public void parseHTML(WebView view) {
        view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    /**
     * 注入 js 函数监听，这段 js 函数的功能就是，遍历所有的图片
     */
    public void setAllImage() {
        // 这段js函数的功能就是，遍历所有的img节点

        this.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                " var array=new Array(); " +
                " for(var j=0;j<objs.length;j++){ array[j]=objs[j].src; }"+
                " window.allimage.getImage(array);"+
                "})()");
    }
    List<String> imgUrl;
    // js 通信接口，定义供 JavaScript 调用的交互接口
    private class MyJavascriptInterface {
        private Context context;
        public MyJavascriptInterface(Context context) {
            this.context = context;
        }
        @android.webkit.JavascriptInterface
        public void getImage(String[] imgs) {
            Log.i("ss","ss");
           /* if (imgUrl == null){
                if (imgs.length>0){
                    imgUrl = new ArrayList<>();
                    for (int i= 0 ; i<imgs.length ;i++){
                        imgUrl.add(imgs[i]);
                    }
                    callBack.onListValueCallBack(imgUrl);
                }
            }else {

            }*/

           for (int i= 0 ; i < imgs.length ;i ++ ){
               listImgSrc.add(imgs[i]);
           }
           callBack.onListValueCallBack(listImgSrc);
        }
    }

    private class InJavaScriptLocalObj {
        /**
         * 获取要解析 WebView 加载对应的 Html 文本
         *
         * @param html WebView 加载对应的 Html 文本
         */
        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            //从 Html 文件中提取页面所有图片对应的地址对象
            getAllImageUrlFromHtml(html);
        }
    }

    /***
     * 获取页面所有图片对应的地址对象，
     * 例如 <img src="http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e" />
     *
     * @param html WebView 加载的 html 文本
     * @return
     */
    private List<String> getAllImageUrlFromHtml(String html) {
        Matcher matcher = Pattern.compile(IMAGE_URL_TAG).matcher(html);
        Document doc = Jsoup.parse(html);

        Elements elem_img = doc.getElementsByTag("img");
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        //从图片对应的地址对象中解析出 src 标签对应的内容
        getAllImageUrlFormSrcObject(listImgUrl);
        return listImgUrl;
    }
    /***
     * 从图片对应的地址对象中解析出 src 标签对应的内容,即 url
     * 例如 "http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e"
     * @param listImageUrl 图片地址对象，
     *                     例如 <img src="http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e" />
     */
    private List<String> getAllImageUrlFormSrcObject(List<String> listImageUrl) {
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMAGE_URL_CONTENT).matcher(image);
            while (matcher.find()) {
                listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        callBack.onListValueCallBack(listImgSrc);
        return listImgSrc;
    }
    private ListValuesCallBack callBack;

    public void setListValuesCallBack(ListValuesCallBack callBack) {
        this.callBack = callBack;
    }

    public interface ListValuesCallBack{
        void onListValueCallBack(List<String> imgUrl);
    }
    /**
     * 设置长按回调
     * @param mCallBack
     */
    private LongClickCallBack mCallBack;

    public void setLongClickCallBack(LongClickCallBack mCallBack){
        this.mCallBack=mCallBack;
    }
    /**
     * 长按事件回调接口，传递图片地址
     * @author LinZhang
     */
    public interface LongClickCallBack{
        /**用于传递图片地址*/
        void onLongClickCallBack(String imgUrl);
    }
    /**
     * 开始下载图片
     */
    private void downloadImage(String url) {
        //ImageLoaderUtils.downLoadImage(url,Environment.getExternalStorageDirectory().getAbsolutePath() + "/ImagesFromWebView",context);
    }

}
