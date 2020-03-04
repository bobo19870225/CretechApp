package cn.com.cretech.model

import cn.com.cretech.iModel.INewDynamicMicroModel
import cn.com.cretech.iModel.INewMicroModel

class NewMicroModel : INewDynamicMicroModel {



    var company_id: String = ""
    var page_number: Int = 1
    var count : Int = 15

    override fun setPageNumber(page_number: Int) {
        this.page_number = page_number
    }

    override fun getPageNumber(): Int {
        return page_number
    }

    override fun setCompany(company_id: String) {
        this.company_id = company_id
    }

    override fun getCompany(): String {
        return company_id
    }
    override fun setListCount(count: Int) {
        this.count = count
    }

    override fun getListCount(): Int {
        return count
    }
    override fun setAddListCount(count: Int) {
        this.count+=count
    }
}