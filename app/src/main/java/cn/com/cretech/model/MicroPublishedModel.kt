package cn.com.cretech.model

import cn.com.cretech.bean.ChildMicroPublishedBean
import cn.com.cretech.iModel.IMicroPublishedModel

class MicroPublishedModel : IMicroPublishedModel {
    override fun setClear() {
        list.clear()
    }

    override fun setSchoolDynamic(list: MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic>) {
        this.list.addAll(list)
    }

    override fun getSchoolDynamic(): MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic> {
        return list
    }

    var list : MutableList<ChildMicroPublishedBean.DataBean.SchoolDynamic> = mutableListOf()
    var default_id = 0
    var total = 15
    var page_number = 1
    override fun setCount(count : Int) {
        this.total = count
    }

    override fun getCount(): Int {
        return total
    }

    override fun setPageNumber(page_number: Int) {
        this.page_number = page_number
    }

    override fun getPageNumber(): Int {
        return page_number
    }

    override fun setId(default_id : Int) {
            this.default_id = default_id
    }

    override fun getId(): Int {
        return default_id
    }
}