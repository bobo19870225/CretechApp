package cn.com.cretech.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class LivePagerStripAdapter : FragmentPagerAdapter {
    var topTitle : MutableList<String>
    var fragmentList : MutableList<Fragment>
    constructor(fm : FragmentManager , topTitle : MutableList<String>,fragmentList : MutableList<Fragment>) :super(fm){
        this.topTitle = topTitle
        this.fragmentList = fragmentList

    }

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return topTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return topTitle.get(position)
    }
}