package cn.com.cretech.fragment

import android.content.Intent
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentMessageBinding
import cn.com.cretech.presenter.MicroPresenter
import cn.com.cretech.util.AutoUtils
import cn.com.cretech.util.DensityUtils
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.SchoolMessageBean
import cn.com.cretech.childfragment.ChildMicroFragment
import cn.com.cretech.databinding.DialogKeepShareBinding
import cn.com.cretech.iView.IMicroView
import cn.com.cretech.util.Toast
import com.umeng.socialize.UMShareAPI


/**
 * 微册
 */
class MicroFragment : MVPBaseFragment<MicroPresenter, FragmentMessageBinding>() , IMicroView {
    override fun setPagePosition(position: Int) {
        when(position){
            0 -> {
                binDing.ivNewDynamic.setImageResource(R.drawable.checked_new_dynamic)
                binDing.ivMicroPublished.setImageResource(R.drawable.micro_dynamic)
                binDing.ivWeSchool.setImageResource(R.drawable.we_school)
                binDing.ivWallFame.setImageResource(R.drawable.wall_fame)
            }
            1 -> {
                binDing.ivNewDynamic.setImageResource(R.drawable.new_dynamic)
                binDing.ivMicroPublished.setImageResource(R.drawable.checked_micro)
                binDing.ivWeSchool.setImageResource(R.drawable.we_school)
                binDing.ivWallFame.setImageResource(R.drawable.wall_fame)
            }
            2 -> {
                binDing.ivNewDynamic.setImageResource(R.drawable.new_dynamic)
                binDing.ivMicroPublished.setImageResource(R.drawable.micro_dynamic)
                binDing.ivWeSchool.setImageResource(R.drawable.select_we_school)
                binDing.ivWallFame.setImageResource(R.drawable.wall_fame)
            }
            3 -> {
                binDing.ivNewDynamic.setImageResource(R.drawable.new_dynamic)
                binDing.ivMicroPublished.setImageResource(R.drawable.micro_dynamic)
                binDing.ivWeSchool.setImageResource(R.drawable.we_school)
                binDing.ivWallFame.setImageResource(R.drawable.select_wall_fame)
            }
        }
    }

    override fun setCancelWindow() {
        mPopupWindow!!.dismiss()
    }

    override fun setPopupWindow(isKeep : Boolean) {
        popUpMyOverflow(isKeep)
    }



    override fun setLayoutRes(): Int {

        return R.layout.fragment_message
    }
    var is_initialize = false
    override fun initView() {
        super.initView()
        is_initialize = true
        presenter = MicroPresenter(context!!,this , childFragmentManager)
        presenter.setMicroViewPager(binDing.pagerSliding, binDing.viewPager)
        presenter.getShareData()
        tool_bar = binDing.root.findViewById(R.id.tool_bar)
        tool_bar.inflateMenu(R.menu.nav_menu)
        tool_bar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if(BaseApplication().getUid() == 0){
                    Toast(context!!,context!!.resources.getString(R.string.after_login))
                    return true
                }
                if(BaseApplication.company_id == ""){
                    Toast(context!!,context!!.resources.getString(R.string.no_school))
                    return true
                }
                presenter.getKeepStatus()
                return true
            }
        })
    }

    override fun setListener() {
        super.setListener()
        binDing.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setPagePosition(position)
            }
        })
        binDing.ivNewDynamic.setOnClickListener { binDing.viewPager.currentItem = 0 }
        binDing.ivMicroPublished.setOnClickListener { binDing.viewPager.currentItem = 1 }
        binDing.ivWeSchool.setOnClickListener { binDing.viewPager.currentItem = 2 }
        binDing.ivWallFame.setOnClickListener { binDing.viewPager.currentItem = 3 }
    }
    fun onNewIntent(dataBean : SchoolMessageBean.DataBean){

        if (dataBean.column_id > 0 && is_initialize){
            binDing.viewPager.currentItem = 1
            if (presenter.fragmentList[1].isAdded){
                (presenter.fragmentList[1] as ChildMicroFragment).onNewIntent(dataBean.column_id)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 弹出自定义的popWindow
     */
    var mPopupWindow: PopupWindow? = null
    lateinit var mDataBinding: DialogKeepShareBinding
    var w : Int = 0
    private fun popUpMyOverflow(isKeep : Boolean) {
        //状态栏高度+toolbar的高度
        var x8 = DensityUtils().dip2px(8f)
        AutoUtils.setAutoUtil(context as FragmentActivity)
        var width = AutoUtils.currentWidth

        if (mPopupWindow == null) {
            //初始化PopupWindow的布局
           mDataBinding = DataBindingUtil.inflate<DialogKeepShareBinding>(layoutInflater,R.layout.dialog_keep_share,null,false)
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = PopupWindow(
                mDataBinding.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true
            )
            //点击外部关闭。
            mPopupWindow!!.setOutsideTouchable(true)
            //设置一个动画。
            mPopupWindow!!.setAnimationStyle(android.R.style.Animation_Dialog)
            //设置Gravity，让它显示在右上角。
            //设置popupWindow上边控件item的点击监听
            //popView.findViewById(R.id.ll_item1).setOnClickListener(this)
            mDataBinding.root.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
            mPopupWindow!!.contentView = mDataBinding.root
            w = mPopupWindow!!.contentView.measuredWidth
        }
        setBackgroundAlpha((context as FragmentActivity) , 0.4f)
        mDataBinding.isKeep = isKeep
        mDataBinding.presenter = presenter
        mPopupWindow!!.showAsDropDown(tool_bar, width.toInt() - w - x8, x8)
        mPopupWindow!!.setOnDismissListener(
            PopupWindow.OnDismissListener // 在dismiss中恢复透明度
            {
                setBackgroundAlpha((context as FragmentActivity) , 1.0f)
            })
    }

    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    private fun setBackgroundAlpha(activity : FragmentActivity , bgAlpha : Float ) {
        var lp = activity . getWindow ().getAttributes()
        lp.alpha = bgAlpha
        if (bgAlpha == 1.0f) {
            activity.getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp)
    }
}