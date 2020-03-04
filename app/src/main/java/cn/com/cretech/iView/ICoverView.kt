package cn.com.cretech.iView

import cn.com.cretech.bean.SchoolMessageBean

interface ICoverView {

    fun setH5Image(dataBean : SchoolMessageBean.DataBean , company_img : String , company_name : String,company_num : String)

}