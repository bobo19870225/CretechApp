package cn.com.cretech.childfragment

import android.annotation.SuppressLint
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentLiveChildBinding
import cn.com.cretech.iView.ILiveView
import cn.com.cretech.presenter.ChildLivePresenter
import com.baoyz.widget.PullRefreshLayout



class ChildLiveFragment : MVPBaseFragment<ChildLivePresenter, FragmentLiveChildBinding>() ,ILiveView{
    override fun setRecyclerView(): RecyclerView {
        return binDing.lvData
    }

    override fun setSwipeRefreshLayout(): PullRefreshLayout {
        return binDing.swipeRefreshLayout
    }
    var time = ""
    var isToday = false

    override fun setSkip() {
        isToday = arguments!!.getBoolean("is_today_live",false)
        time = arguments!!.getString("flag","")

    }

    override fun setLayoutRes(): Int {
        return R.layout.fragment_live_child
    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        super.initView()
        presenter = ChildLivePresenter(this,context!! , childFragmentManager)
        presenter.setIsToday(isToday)
        presenter.setFlag(time)
        initRecycler(binDing.lvData)
    }

    override fun setListener() {
        binDing.swipeRefreshLayout.setOnRefreshListener(object : PullRefreshLayout(context),
            PullRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                presenter.setData()
            }
        })

    }

    override fun initData() {
        super.initData()
        presenter.setData()
    }
}