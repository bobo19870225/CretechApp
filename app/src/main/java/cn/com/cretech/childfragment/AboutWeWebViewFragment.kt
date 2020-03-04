package cn.com.cretech.childfragment

import cn.com.cretech.R
import cn.com.cretech.base.MVPBaseFragment
import cn.com.cretech.databinding.WebviewAboutBinding
import cn.com.cretech.iView.IAboutWeWebView
import cn.com.cretech.presenter.AboutWeWebViewPresenter
import cn.com.cretech.util.AboutHandlers
import cn.com.cretech.widget.AttributesUtil

class AboutWeWebViewFragment : MVPBaseFragment<AboutWeWebViewPresenter,WebviewAboutBinding >() , IAboutWeWebView {
    override fun setAboutWebView(url: String) {
        AboutHandlers.getInstance().post(Runnable {
            kotlin.run {
                AttributesUtil.appendWebViewContent(binDing.webView,binDing.webView.settings,url)
            }
        })
    }

    override fun setLayoutRes(): Int {
        return R.layout.webview_about
    }

    override fun initView() {
        super.initView()
        presenter = AboutWeWebViewPresenter(this , childFragmentManager)
    }

    override fun initData() {
        super.initData()
        presenter.setData(arguments!!.getInt("location",0))
    }
}