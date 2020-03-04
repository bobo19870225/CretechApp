package cn.com.cretech.bean

data class SchoolNavigationBean(

     var code: Int,
     var msg: String,
     var token: String,
     var data: MutableList<DataBean>
) {

    data class DataBean(
         var company_id: String,
         var logo: String,
         var company_name: String,
         var grade: Int ,
         var follow: Int

    )

}