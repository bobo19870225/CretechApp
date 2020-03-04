package cn.com.cretech.base

import cn.com.cretech.util.NetWorkSign

class BaseBean<T>(
    var list: MutableList<T>
){
    fun setListValues() : NetWorkSign{
            if (list.size > 0){
                return NetWorkSign.NET_CONNECT_SUCCEED
            }else{
                return NetWorkSign.NET_CONNECT_DATA_EMPTY
            }
            return NetWorkSign.NET_SERVER_PARAM_ERROR

    }
}