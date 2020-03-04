package cn.com.cretech.adapter

import android.content.Context
import android.os.Build
import android.util.TypedValue
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.base.BaseListAdapter
import cn.com.cretech.bean.CodeMessageBean
import cn.com.cretech.bean.KeepListBean
import cn.com.cretech.bean.UserDataBean
import cn.com.cretech.databinding.ItemKeepListBinding
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.LoadImage
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.ListenerHandlers
import retrofit2.Response
import java.text.SimpleDateFormat

class KeepListAdapter(mContext: Context,var map : MutableMap<String,String> ,val dataList : MutableList<KeepListBean.DataBean> ) : BaseListAdapter<KeepListBean.DataBean,ItemKeepListBinding>(mContext,dataList) {
    override fun setRes(): Int {
        return R.layout.item_keep_list
    }

    override fun initData(position: Int) {

        bindDing!!.model = dataList.get(position)
        bindDing!!.listener = ListenerHandlers(context)
        bindDing!!.adapter = this
        if (dataList.get(position).company_image != "") {
            LoadImage.loadImageView(bindDing!!.ivLiveImage,BaseLink().BASE_H5_IMAGE_URL + dataList.get(position).logo)
        } else {
            bindDing!!.ivLiveImage.setImageResource(R.drawable.icon_default)
        }

        if (dataList.get(position).ctime != 0) {
            val sf = SimpleDateFormat("yyyy-MM-dd")
            val format = sf.format(dataList.get(position).ctime)
            bindDing!!.tvTime.setText(format)
        }

        if (dataList.get(position).defaults === 1) {
           bindDing!!.tvDefaultKeep.setBackgroundResource(0)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                bindDing!!.tvDefaultKeep.background = (context.resources.getDrawable(R.drawable.blue12_shape))
            }
            bindDing!!.tvDefaultKeep.setPadding(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16f,
                    context.getResources().getDisplayMetrics()
                ).toInt(),
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    4f,
                    context.getResources().getDisplayMetrics()
                ).toInt(),
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16f,
                    context.getResources().getDisplayMetrics()
                ).toInt(),
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    4f,
                    context.getResources().getDisplayMetrics()
                ).toInt()
            )
        }
    }

    fun setNotify(addListBean : MutableList<KeepListBean.DataBean>){

        dataList.addAll(addListBean)
        notifyDataSetChanged()
    }

    /**
     * 取消关注
     */
    fun setCancelKeep(dataBean : KeepListBean.DataBean){

        BaseApplication.netService.getCancelKeep(map,dataBean.company_id,BaseApplication().getUid())
            .enqueue(object : NetWorkResult<CodeMessageBean>(){
                override fun onSucceed(response: Response<CodeMessageBean>) {
                    super.onSucceed(response)
                    Toast(context,response.body()!!.msg)
                    if (response.body()!!.code == 1){
                        dataList.remove(dataBean)
                        notifyDataSetChanged()
                    }
                }
            })
    }
    /**
     * 设为默认关注
     */
    fun setDefaultKeepSchool(dataBean : KeepListBean.DataBean){
        if (dataBean.defaults != 1){
            BaseApplication.netService.getDefaultKeepSchool(map,BaseApplication().getUid(),dataBean.company_id)
                .enqueue(object : NetWorkResult<CodeMessageBean>(){
                    override fun onSucceed(response: Response<CodeMessageBean>) {
                        super.onSucceed(response)
                        Toast(context,response.body()!!.msg)
                        if (response.body()!!.code == 1){
                            dataList.forEach { it.defaults = 0 }
                            for (i in 0..(dataList.size -1 )){
                                if (dataList.get(i) == dataBean){
                                    dataList.get(i).defaults = 1
                                    BaseApplication.company_id = dataBean.company_id
                                }
                            }
                            dataList.sortByDescending { it.defaults }
                            defaultListener.setClick(dataList)
                        }
                    }
                })
        }
    }

    lateinit var defaultListener : OnDefaultListener

    fun setListener(defaultListener : OnDefaultListener){
        this.defaultListener = defaultListener
    }

    interface OnDefaultListener{
        fun setClick(dataList: MutableList<KeepListBean.DataBean>)
    }
}