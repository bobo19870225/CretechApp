package cn.com.cretech.activity

import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseActivity
import cn.com.cretech.bean.SchoolMessageBean
import cn.com.cretech.databinding.ActivityCoverBinding
import cn.com.cretech.iView.ICoverView
import cn.com.cretech.presenter.CoverPresenter
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.util.FromTime
import cn.com.cretech.util.LoadImage
import java.util.logging.Handler

class CoverActivity : MVPBaseActivity<CoverPresenter,ActivityCoverBinding>() , ICoverView {
    override fun setH5Image(dataBean : SchoolMessageBean.DataBean  , company_img : String, company_name : String, company_num: String) {

        if (company_img == ""){
            binDing.tvSchoolName.text = company_name
        }else{
            LoadImage.loadImageView(binDing.ivH5Image,BaseLink().BASE_H5_IMAGE_URL + company_img)
        }
        binDing.tvLookNum.text = company_num
        FromTime(this).FromCoverskipMain(dataBean,2000)

    }

    override fun setLayoutRes(): Int {
        return R.layout.activity_cover
    }

    override fun isTitleBarBack(): Boolean {
        return false
    }
    lateinit var company_id : String
    var column_id = 0
    override fun initView() {
        super.initView()
         company_id = intent.getStringExtra("company_id")
        column_id = intent.getIntExtra("column_id",0)
        presenter = CoverPresenter(this ,column_id , supportFragmentManager)

    }

    override fun initData() {
        super.initData()
        presenter.SkipCover(company_id)
    }
}