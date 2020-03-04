package cn.com.cretech.iModel

import cn.com.cretech.base.BaseModel
import cn.com.cretech.bean.ChildMicroPublishedBean

interface IMicroPublishedModel : BaseModel{

    fun setId(default_id : Int)
    fun getId() : Int
    fun setSchoolDynamic(list : MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic>)
    fun getSchoolDynamic() : MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic>
    fun setClear()

}