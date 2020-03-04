package cn.com.cretech.model

import cn.com.cretech.base.BaseModel

class KeepListModel : BaseModel {
    override fun setCount(count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    var page_number = 1
    override fun setPageNumber(page_number: Int) {
        this.page_number = page_number
    }

    override fun getPageNumber(): Int {
        return page_number
    }
}