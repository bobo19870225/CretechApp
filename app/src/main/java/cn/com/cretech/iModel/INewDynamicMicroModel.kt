package cn.com.cretech.iModel

interface INewDynamicMicroModel {

    fun setPageNumber(page_number : Int)
    fun getPageNumber(): Int

    fun setCompany(company_id : String)
    fun getCompany(): String

    fun setListCount(count : Int)
    fun getListCount(): Int
    fun setAddListCount(count : Int)


}