package cn.com.cretech.childfragment

import android.os.Handler
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.FragmentWallFameBinding
import cn.com.cretech.iView.IWallFameView
import cn.com.cretech.presenter.WallFamePresenter
import cn.com.cretech.widget.ScrollLinearLayoutManager

class WallFameFragment : MVPBaseFragment<WallFamePresenter,FragmentWallFameBinding>(),
    IWallFameView {
    override fun setGallery(): RecyclerView {
        return binDing.glWallFame
    }

    override fun setLayoutRes(): Int {
        return R.layout.fragment_wall_fame
    }

    override fun initView() {
        super.initView()
        var layoutManager = ScrollLinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binDing.glWallFame.layoutManager = layoutManager
        binDing.glWallFame.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        presenter = WallFamePresenter(this, context!!,layoutManager,childFragmentManager)


        /*AutoUtils.setRatioValue(context as FragmentActivity)
        AutoUtils.auto(context as FragmentActivity)*/
    }

    override fun initData() {
        super.initData()
        presenter.setData()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
}