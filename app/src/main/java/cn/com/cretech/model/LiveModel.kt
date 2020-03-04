package cn.com.cretech.model

import cn.com.cretech.iModel.ILiveModel

class LiveModel : ILiveModel {

    var isToday = false
    var time =""

    override fun setIsToday(isToday : Boolean) {
        this.isToday = isToday
    }

    override fun getIsToday(): Boolean {
        return isToday
    }

    override fun setFlag(flag: String) {
        this.time = flag
    }

    override fun getFlag(): String {
        return time
    }



}