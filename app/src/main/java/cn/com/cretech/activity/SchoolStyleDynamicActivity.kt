package cn.com.cretech.activity

import android.content.Intent
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.databinding.ActivitySchoolStyleDynamicBinding
import cn.com.cretech.iView.IChildDynamicMicroView
import cn.com.cretech.presenter.SchoolStylePresenter
import cn.com.cretech.util.DensityUtils
import cn.com.cretech.util.EncodeImgZxing
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.ShareUtil
import com.tmall.ultraviewpager.UltraViewPager
import com.umeng.socialize.UMShareAPI

class SchoolStyleDynamicActivity : MVPBaseActivity<SchoolStylePresenter,ActivitySchoolStyleDynamicBinding>(),
    IChildDynamicMicroView {
    override fun setUltraViewPager(): UltraViewPager {
        return binDing.ultraViewpager
    }

    override fun setLinearLayout(): LinearLayout {
        return this.findViewById(R.id.ll_net_work)
    }

    override fun setNetWorkError(): TextView {
        return this.findViewById(R.id.tv_net_work)
    }

    override fun setNetWorkClick(): TextView {
        return this.findViewById(R.id.tv_click)
    }


    override fun setLayoutRes(): Int {
        return R.layout.activity_school_style_dynamic
    }
    lateinit var schoolDynamic : ChildMicroPublishedBean.DataBean.SchoolDynamic

    override fun initView() {
        super.initView()
        presenter = SchoolStylePresenter(this,this , supportFragmentManager)
        tool_bar.inflateMenu(R.menu.h5_menu)
    }
    override fun setValues(): String {
        schoolDynamic = intent.getSerializableExtra("schoolDynamic") as ChildMicroPublishedBean.DataBean.SchoolDynamic
        return schoolDynamic.column_names
    }

    override fun initData() {
        super.initData()
        presenter.setData(schoolDynamic)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun setListener() {
        super.setListener()
        tool_bar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if (schoolDynamic == null){
                    Toast(this@SchoolStyleDynamicActivity,this@SchoolStyleDynamicActivity.resources.getString(R.string.no_school))
                    return false
                }
                if (schoolDynamic.column_img != "") {
                    ShareUtil(this@SchoolStyleDynamicActivity, schoolDynamic.column_img, null, schoolDynamic.column_names)
                } else {
                    if (schoolDynamic.qrc != "") {
                        var qrImage = EncodeImgZxing.createQRImage(
                            schoolDynamic.qrc,
                            DensityUtils().dip2px(238f),
                            DensityUtils().dip2px(325f),
                            null,
                            "")
                        ShareUtil(this@SchoolStyleDynamicActivity, "", qrImage, schoolDynamic.column_names)
                    }
                }
                return true
            }
        })
    }
}