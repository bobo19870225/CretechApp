package cn.com.cretech.model

import cn.com.cretech.iModel.IMicroDataModel

class MicroDataModel : IMicroDataModel {

    lateinit var company_id : String

    override fun setCompany(company_id: String) {
        this.company_id = company_id
    }

    override fun getCompany(): String {
        return company_id
    }
}