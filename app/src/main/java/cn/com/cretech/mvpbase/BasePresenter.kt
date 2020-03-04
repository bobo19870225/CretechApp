package cn.com.cretech.mvpbase

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.R
import cn.com.cretech.adapter.LivePagerStripAdapter
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.MicroPublishedBean
import cn.com.cretech.bean.UserMessageBean
import cn.com.cretech.childfragment.*
import cn.com.cretech.databinding.DialogExistUserBinding
import cn.com.cretech.pullToRefresh.PullToRefreshScrollView
import cn.com.cretech.widget.PagerSlidingTabStrip
import java.text.SimpleDateFormat
import java.util.*

import  cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.DensityUtils
import cn.com.cretech.util.NetWorkSign
import cn.com.cretech.util.SkipTo
import cn.com.cretech.util.SpUtils
import cn.com.cretech.widget.CustomPopupDialog
import cn.com.cretech.widget.SelectImageListener
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer
import java.io.File


open class BasePresenter : IPresenter {
    /*override fun onStop(owner: LifecycleOwner) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume(owner: LifecycleOwner) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    lateinit var map: MutableMap<String, String>
    var topTitle: MutableList<String>
    var fragmentList: MutableList<Fragment>
    var manager: FragmentManager

    override fun onCreate(owner: LifecycleOwner) {
        map = BaseApplication.map
        if (BaseApplication().isUserLogin()) {
            BaseApplication.map[BaseLink().TOKEN] = BaseApplication().getUserMessage().oauth_token
            BaseApplication.map[BaseLink().OAUTH_TOKEN_SECRET] =
                BaseApplication().getUserMessage().oauth_token_secret
        }
    }

    fun setLoginData(){
        if (BaseApplication().isUserLogin()) {
            BaseApplication.map[BaseLink().TOKEN] = BaseApplication().getUserMessage().oauth_token
            BaseApplication.map[BaseLink().OAUTH_TOKEN_SECRET] =
                BaseApplication().getUserMessage().oauth_token_secret
        }
    }

    override fun onStart(owner: LifecycleOwner) {

    }

    override fun onDestory(owner: LifecycleOwner) {
    }

    override fun onLifecycleChange(owner: LifecycleOwner) {
    }

    constructor(manager: FragmentManager) {
        this.manager = manager

        topTitle = mutableListOf()
        fragmentList = mutableListOf()
    }

    /**
     * 显示空白界面
     */
    fun setVisibility(
        slidingTabStrip: FrameLayout?,
        swipeRefreshLayout: ViewGroup,
        ll_net_work: View,
        tv_net_work: TextView?,
        netWorkSign: NetWorkSign?
    ) {
        setVisibility(
            slidingTabStrip,
            swipeRefreshLayout,
            null,
            ll_net_work,
            tv_net_work,
            netWorkSign
        )
    }

    fun setVisibility(
        swipeRefreshLayout: ViewGroup,
        ll_net_work: View,
        tv_net_work: TextView?,
        netWorkSign: NetWorkSign?
    ) {
        setVisibility(null, swipeRefreshLayout, null, ll_net_work, tv_net_work, netWorkSign)
    }

    fun setVisibility(
        slidingTabStrip: FrameLayout?,
        swipeRefreshLayout: ViewGroup,
        tv_count: TextView?,
        ll_net_work: View,
        tv_net_work: TextView?,
        netWorkSign: NetWorkSign?
    ) {

        var value = R.string.net_connect_data_empty
        when (netWorkSign) {
            NetWorkSign.NET_CONNECT_SUCCEED -> value = R.string.net_connect_succeed
            NetWorkSign.NET_CONNECT_DATA_EMPTY -> value = R.string.net_connect_data_empty
            NetWorkSign.NET_NOT_CONNECTED -> value = R.string.net_not_connected
            NetWorkSign.NET_SERVER_CONNECTED_ERROR -> value = R.string.net_server_connected_error
            NetWorkSign.NET_SERVER_ERROR -> value = R.string.net_server_error
            NetWorkSign.NET_SERVER_PARAM_ERROR -> value = R.string.net_server_param_error
            NetWorkSign.NET_UNKNOWN_ERROR -> value = R.string.net_unknown_error
        }
        if (netWorkSign == NetWorkSign.NET_CONNECT_SUCCEED) {
            //有值显示界面
            if (ll_net_work != null) {
                ll_net_work.visibility = View.GONE
            }
            if (slidingTabStrip != null) {
                slidingTabStrip.visibility = View.VISIBLE
            }
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.visibility = View.VISIBLE
            }
            if (tv_count != null) {
                tv_count.visibility = View.VISIBLE
            }
        } else {
            //网络错误或其他原因
            if (slidingTabStrip != null) {
                slidingTabStrip.visibility = View.GONE
            }
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.visibility = View.GONE
            }
            if (tv_count != null) {
                tv_count.visibility = View.GONE
            }
            if (ll_net_work != null) {
                ll_net_work.visibility = View.VISIBLE
            }
        }
        tv_net_work!!.setText(value)
    }

    /**
     * 取消刷新
     */
    fun setClearPullRefresh(pullToRefresh: PullToRefreshScrollView) {
        pullToRefresh.onPullDownRefreshComplete()
        pullToRefresh.onPullUpRefreshComplete()
    }

    /**
     * 直播横栏
     */
    fun setLiveViewData(pagerStrip: PagerSlidingTabStrip, view_pager: ViewPager) {
        var date = Date()
        var sf = SimpleDateFormat("MM/dd")
        val sf1 = SimpleDateFormat("yyyyMMdd")
        for (i in 0..6) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -i)
            date = calendar.time
            val bundle = Bundle()
            if (i == 0) {
                topTitle.add("今日直播")
                bundle.putBoolean("is_today_live", true)
                bundle.putString("flag", sf1.format(calendar.time))
            } else {
                topTitle.add(sf.format(date.time))
                bundle.putBoolean("is_today_live", false)
                bundle.putString("flag", sf1.format(date.time))
            }
            var childFragment = ChildLiveFragment()
            childFragment.arguments = bundle
            fragmentList.add(childFragment)
        }

        pagerStrip.tabPaddingLeftRight = DensityUtils().dip2px(16f)
        setViewPagerStrip(pagerStrip, view_pager)

        pagerStrip.shouldExpand = false
    }

    /**
     * 微册横栏
     */
    fun setMicroViewPager(pagerStrip: PagerSlidingTabStrip, view_pager: ViewPager) {
        topTitle.add("最新动态")
        topTitle.add("微册")
        topTitle.add("我们的学校")
        topTitle.add("荣誉墙")
        //fragmentList.add(NewMicroFragment())//最新动态
        fragmentList.add(NewDynamicMicroFragment())//最新动态
        fragmentList.add(ChildMicroFragment())//微册馆
        fragmentList.add(WeSchoolFragment())//我们的学校
        fragmentList.add(WallFameFragment())//荣誉墙
        setViewPagerStrip(pagerStrip, view_pager)
        pagerStrip.shouldExpand = true
    }

    /**
     * 微册管横栏
     */
    fun setMicroPublishedViewPager(
        pagerStrip: PagerSlidingTabStrip,
        view_pager: ViewPager,
        list: MutableList<MicroPublishedBean.DataBean>
    ) {

        for (i in 0..(list.size - 1)) {
            var bundle = Bundle()
            topTitle.add(list.get(i).navname)
            var childMicroFragment = ChildMicroPublishedFragment()
            bundle.putInt("id", list.get(i).id)
            childMicroFragment.arguments = bundle
            fragmentList.add(childMicroFragment)
        }
        pagerStrip.tabPaddingLeftRight = DensityUtils().dip2px(16f)
        setViewPagerStrip(pagerStrip, view_pager)
        pagerStrip.shouldExpand = false
    }

    var binDing: DialogExistUserBinding? = null
    var userDialog: CustomPopupDialog<DialogExistUserBinding>? = null
    fun isVerifyToken(context: Context,  code: Int) {

        if (code == 403) {
            setVerifyExist(context)
        }

    }
    private fun setVerifyExist(context: Context) {

        if (binDing == null) {
            binDing = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_exist_user,
                null,
                true
            )

        }

        if (userDialog == null) {
            userDialog = CustomPopupDialog<DialogExistUserBinding>(context, binDing, false)
            binDing!!.tvLogin.text = context.resources.getString(R.string.is_verify)

            userDialog!!.setCanceledOnTouchOutside(false)
        }
        binDing!!.tvDismiss.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                //SpUtils.removeObject(context , UserMessageBean.UserMessage::class.java)
                userDialog!!.dismiss()
                SkipTo(context).skipLogin()
                (context as FragmentActivity).finish()

            }
        })
    }

    fun setViewPagerStrip(pagerStrip: PagerSlidingTabStrip, view_pager: ViewPager) {

        view_pager.adapter = LivePagerStripAdapter(manager, topTitle, fragmentList)
        pagerStrip.setViewPager(view_pager)

    }

    fun initFragment(fragment: Int) {
        for (i in 0..1) {
            var bundle = Bundle()
            bundle.putInt("location", i)
            var webViewAbout = AboutWeWebViewFragment()
            webViewAbout.arguments = bundle
            fragmentList.add(webViewAbout)
        }
        manager.beginTransaction().add(fragment, fragmentList.get(0)).commit()
    }

    /**
     * 判断是否有权限
     */
    fun isCheckSelfPermission(ctx: FragmentActivity, permission: Array<String>): Boolean {
        for (i in permission.indices) {
            val checkSelfPermission = ContextCompat.checkSelfPermission(ctx, permission[i])
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//已经授权
                ActivityCompat.requestPermissions(ctx, permission, BaseLink.CONFIRM_PERMISSION)
                return false
            }
        }
        return true
    }

    /**
     * 显示是否拍照弹窗
     */
    var dialog: Dialog? = null
    var layout: View? = null
    lateinit var photoImage: SelectImageListener
    lateinit var cameraFile: File
    lateinit var uri: Uri
    lateinit var file: File
    fun getPhoneDialog(context: FragmentActivity, listData: MutableList<String>) {
        if (dialog == null) {
            dialog = Dialog(context, R.style.my_dialog)
        }
        if (layout == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.dialog_phone_photo, null)
            dialog!!.addContentView(
                layout!!, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            dialog!!.setCanceledOnTouchOutside(true)
            //点击弹出床主体内容之外消失Dialog
            val main = layout!!.findViewById<FrameLayout>(R.id.ll_popup)
            main.setOnTouchListener(View.OnTouchListener { v, event ->
                cancel(context, v)
                false
            })

            val listView = layout!!.findViewById<ListView>(R.id.listView)
            listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                cancel(context, view)
                onItemClickListener.setOnClick(position)
            })
            val adapter = ArrayAdapter(
                context, R.layout.item_dialog_textview,
                listData
            )
            listView.setAdapter(adapter)
        }
        dialog!!.setContentView(layout!!)
        show()
    }

    lateinit var onItemClickListener: OnItemClickListener

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun setOnClick(position: Int)
    }


    /**
     * 显示dialog窗口
     */
    fun show() {
        if (!dialog!!.isShowing()) {
            dialog!!.show()
        }
    }

    /**
     * 隐藏窗口
     */
    fun cancel() {


        dialog!!.dismiss()
    }

    /**
     * 隐藏窗口
     */
    fun cancel(context: Context, v: View) {
        dialog!!.dismiss()

        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.umeng_xp_zoom_out))
    }

    /**
     * 拍照
     */
    fun getPhone(ctx: FragmentActivity) {

        cameraFile = File(
            Environment.getExternalStorageDirectory(),
            BaseLink.CACHE_PATH
        )
        if (!cameraFile.exists()) {
            cameraFile.mkdirs()
        }
        cameraFile = File(cameraFile, System.currentTimeMillis().toString() + ".jpg")
        ctx.startActivityForResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(cameraFile)
            ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION), BaseLink.PHONE_PICTURE
        )
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    /**
     * 初始化UltraViewPager
     */
    fun initUltraViewPager(context: Context, ultra_viewpager: UltraViewPager) {
        ultra_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        ultra_viewpager.setMultiScreen(0.6f)
        ultra_viewpager.setItemRatio(1.0)
        //内置indicator初始化
        ultra_viewpager.initIndicator()
        //设置indicator样式
        ultra_viewpager.setPageTransformer(true, UltraDepthScaleTransformer())
        ultra_viewpager.indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(Color.CYAN)
            .setNormalColor(R.color.white)
            .setRadius(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    2f,
                    context.resources.displayMetrics
                ).toInt()
            )
        //设置indicator对齐方式
        ultra_viewpager.indicator.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        ultra_viewpager.indicator.setMargin(0, 0, 0, 20)
        //构造indicator,绑定到UltraViewPager
        ultra_viewpager.indicator.build()
    }
}