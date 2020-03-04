package cn.com.cretech.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import cn.com.cretech.R
import cn.com.cretech.base.BaseApplication
import cn.com.cretech.bean.SchoolMessageBean
import cn.com.cretech.bean.UpdateVersionBean
import cn.com.cretech.databinding.DialogVersionUpdateBinding
import cn.com.cretech.fragment.*
import cn.com.cretech.setting.BaseLink
import cn.com.cretech.setting.NetWorkResult
import cn.com.cretech.util.Toast
import cn.com.cretech.widget.CustomPopupDialog
import cn.com.cretech.widget.DownLoadAppUtils
import com.google.android.material.bottomnavigation.*
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import retrofit2.Response
import kotlin.system.exitProcess

class MainActivity : FragmentActivity() {

    companion object {
        var isExit = true
        private lateinit var mHandler: Handler
    }


    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.msgFragment -> {
                switchFragment(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.liveFragment -> {
                switchFragment(1)
                return@OnNavigationItemSelectedListener true

            }
            R.id.dynamicFragment -> {
                switchFragment(2)
                return@OnNavigationItemSelectedListener true
            }
            R.id.styleFragment -> {
                switchFragment(3)
                return@OnNavigationItemSelectedListener true
            }
            R.id.myFragment -> {
                switchFragment(4)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun switchFragment(position: Int) {
        this.position = position
        isFragmentShow(fragmentList.get(position))
    }

    var fragmentList = arrayListOf<Fragment>()
    var currentFragment: Fragment? = null
    lateinit var ft: FragmentTransaction
    var position = 0
    lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //请求安装未知应用来源的权限
        ActivityCompat.requestPermissions(this, BaseLink.permission, BaseLink.CONFIRM_PERMISSION)
        navView = findViewById(R.id.nav_view)
        BottomNavigationViewHelper.disableShiftMode(navView)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        fragmentList.add(LiveFragment())
        fragmentList.add(MicroFragment())
        fragmentList.add(SchoolPublishedFragment())
        fragmentList.add(MicroPublishedFragment())
        fragmentList.add(MyFragment())
        isFragmentShow(fragmentList.get(position))
        mHandler = MyHandler()
        UpdateVersion()

    }

    override fun onResume() {
        super.onResume()
    }

    var versionBindingUtil: DialogVersionUpdateBinding? = null
    var downLoadUrl: String = ""
    lateinit var versionCustom: CustomPopupDialog<DialogVersionUpdateBinding>
    fun UpdateVersion() {
        BaseApplication.netService.getUpdateVersion(BaseApplication.map)
            .enqueue(object : NetWorkResult<UpdateVersionBean>() {

                override fun onSucceed(response: Response<UpdateVersionBean>) {
                    super.onSucceed(response)
                    when (response.body()!!.dataBean.is_update) {
                        1 -> {
                            downLoadUrl = response.body()!!.dataBean.download_address
                            getVersionDialog()
                        }
                    }

                }
            })

    }

    fun getVersionDialog() {
        if (versionBindingUtil == null) {
            versionBindingUtil = DataBindingUtil.inflate<DialogVersionUpdateBinding>(
                LayoutInflater.from(this),
                R.layout.dialog_version_update,
                null,
                false
            )
            versionCustom = CustomPopupDialog<DialogVersionUpdateBinding>(this, versionBindingUtil, false)
        }
        versionBindingUtil!!.tvOk.setOnClickListener {

            if (downLoadUrl != "") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0
                    //来判断应用是否有权限安装apk
                    val installAllowed = this.packageManager.canRequestPackageInstalls()
                    //有权限
                    if (installAllowed) {
                        DownLoadAppUtils.showDownloadProgressDialog(this, downLoadUrl)             //打开安装apk文件操作
                        versionCustom.dismiss()
                    } else {
                        //无权限 申请权限
                        //后面跟上包名，可以直接跳转到对应APP的未知来源权限设置界面。使用startActivityForResult 是为了在关闭设置界面之后，获取用户的操作结果，然后根据结果做其他处理
                        val intent = Intent(
                            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse(
                                "package:$packageName"
                            )
                        )
                        startActivityForResult(intent, BaseLink.PERMISSIONINSTAL)
                    }
                } else {
                    DownLoadAppUtils.showDownloadProgressDialog(this, downLoadUrl)
                    versionCustom.dismiss()
                }

            }

        }
        versionCustom.show()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var values = intent!!.getSerializableExtra("dataBean")
        if (values != null){
            position = 1
            navView.menu.findItem(R.id.liveFragment).setChecked(true)
            isFragmentShow(fragmentList.get(position))
            (fragmentList[1] as MicroFragment).onNewIntent(values as SchoolMessageBean.DataBean)
        }
    }

    private fun isFragmentShow(pitchFragment: Fragment) {
        ft = supportFragmentManager.beginTransaction()
        if (currentFragment != null) {
            if (currentFragment!!.isAdded) {
                ft.hide(currentFragment!!)
            }
        }

        currentFragment = pitchFragment
        if (pitchFragment.isAdded) {

            ft.show(pitchFragment).commit()

        } else {

            ft.add(R.id.nav_fragment, pitchFragment).commit()
        }

    }


    class BottomNavigationViewHelper {
        companion object {
            @SuppressLint("RestrictedApi")
            open fun disableShiftMode(view: BottomNavigationView) {
                //获取子View BottomNavigationMenuView的对象
                val menuView = view.getChildAt(0) as BottomNavigationMenuView
                try {
                    //设置私有成员变量mShiftingMode可以修改
                    val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
                    shiftingMode.setBoolean(menuView, false)
                    shiftingMode.isAccessible = true
                    for (i in 0 until menuView.childCount) {
                        val item = menuView.getChildAt(i) as BottomNavigationItemView
                        //去除shift效果
                        item.setShifting(false)
                        item.setChecked(item.itemData.isChecked)
                    }
                } catch (e: NoSuchFieldException) {
                } catch (e: IllegalAccessException) {
                }

            }
        }
    }

    class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = true
            mHandler.removeMessages(0)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    fun exit() {
        if (isExit) {
            isExit = false
            Toast(this@MainActivity, resources.getString(R.string.two_out_system))
            mHandler.sendEmptyMessageDelayed(0, BaseLink.BACK_TIME)
        } else {
            finish()
            exitProcess(0)
        }
    }
}
