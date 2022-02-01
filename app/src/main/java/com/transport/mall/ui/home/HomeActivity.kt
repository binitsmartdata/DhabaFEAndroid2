package com.transport.mall.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.ActivityHomeBinding
import com.transport.mall.model.SideMenu
import com.transport.mall.model.TermsConditionsModel
import com.transport.mall.model.Toolbar
import com.transport.mall.model.UserModel
import com.transport.mall.repository.commonprocesses.CityStateHighwayBanksFetcher
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.addnewdhaba.AddDhabaActivity
import com.transport.mall.ui.authentication.pre_login.splash.SplashActivity
import com.transport.mall.ui.home.helpline.EditProfileFragment
import com.transport.mall.ui.home.helpline.HelplineFragment
import com.transport.mall.ui.home.notifications.NotificationsFragment
import com.transport.mall.ui.home.profile.owner.OwnerEditProfileFragment
import com.transport.mall.ui.home.settings.NotificationSettings
import com.transport.mall.ui.home.settings.SettingsFragment
import com.transport.mall.ui.home.sidemenu.SideMenuAdapter
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.WebViewActivity
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class HomeActivity : BaseActivity<ActivityHomeBinding, BaseVM>(),
    CommonActivityListener {
    override val binding: ActivityHomeBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_home
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(application))
        set(value) {}
    override val context: Context
        get() = this

    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private val toolbar = Toolbar()
    var list = ArrayList<SideMenu>()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    val observer = MutableLiveData<Boolean>()

    override fun bindData() {
        setUpToolbar()
        if (SharedPrefsHelper.getInstance(this).getUserData()
                .isOwner() || SharedPrefsHelper.getInstance(this).getUserData().isManager()
        ) {
            setUpSideMenuOwner()
        } else/* if (SharedPrefsHelper.getInstance(this).getUserData().isExecutive())*/ {
            setUpSideMenuExecutive()
        }
        setUserData()

        CityStateHighwayBanksFetcher.getAllData(
            this,
            object : CityStateHighwayBanksFetcher.CallBack {
                override fun onAllSucceed() {
                    Log.e("CITY STATE BANKS HWAYS", "FETCHED SUCCESSFULLY")
                }

                override fun completedWithSomeErrors(failedThings: String) {
                    Log.e("CITY STATE BANKS HWAYS", "FAILED FOR $failedThings")
                }
            })
    }

    private fun setUserData() {
        binding.userModel = SharedPrefsHelper.getInstance(this).getUserData()
        binding.user.profileView.setOnClickListener {
            openProfileFragment()
        }
        //LISTENER TO LISTEN WHEN TO EXECUTE SAVE BUTTON
        RxBus.listen(UserModel::class.java).subscribe {
            binding.userModel = SharedPrefsHelper.getInstance(this).getUserData()
        }
    }

    fun openProfileFragment() {
        if (SharedPrefsHelper.getInstance(this).getUserData().isExecutive()) {
            openFragmentReplaceNoAnim(
                binding.dashboardContainer.id,
                EditProfileFragment(),
                "editProfile",
                true
            )
            toolbar.title = getString(R.string.edit_profile)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        } else {
            openFragmentReplaceNoAnim(
                binding.dashboardContainer.id,
                OwnerEditProfileFragment(),
                "editProfile",
                true
            )
            toolbar.title = getString(R.string.edit_profile)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
    }

    /**
     * Function - Set Up Toolbar ...
     */
    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        setupDrawerToggle()
        binding.toolbarModel = toolbar

        binding.toolbar.getNavigationIcon()
            ?.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
    }

    /**
     *  SetUp Side Menu items ...
     */
    private fun setUpSideMenuExecutive() {
        val menuArray = resources.getStringArray(R.array.menu_array_fiels_ex)
        val menuArrayIcons = resources.obtainTypedArray(R.array.menu_icons_fiels_ex)
        val menuArrayIconsHilighted =
            resources.obtainTypedArray(R.array.menu_icons_hilighted_fiels_ex)
        for (i in menuArray.indices) {
            list.add(
                SideMenu(
                    menuArray[i],
                    menuArrayIcons.getResourceId(i, 0),
                    menuArrayIconsHilighted.getResourceId(i, 0),
                    i == 0
                )
            )
        }
        binding.sideMenuRecyclerV.adapter = SideMenuAdapter(this, list, GenericCallBack {
            // ON ITEM CLICKED
            displayViewExecutive(it)
        })
        displayViewExecutive(0)
    }

    private fun setUpSideMenuOwner() {
        val menuArray = resources.getStringArray(R.array.menu_array_owner)
        val menuArrayIcons = resources.obtainTypedArray(R.array.menu_icons_owner)
        val menuArrayIconsHilighted =
            resources.obtainTypedArray(R.array.menu_icons_hilighted_owner)
        for (i in menuArray.indices) {
            list.add(
                SideMenu(
                    menuArray[i],
                    menuArrayIcons.getResourceId(i, 0),
                    menuArrayIconsHilighted.getResourceId(i, 0),
                    i == 0
                )
            )
        }
        binding.sideMenuRecyclerV.adapter = SideMenuAdapter(this, list, GenericCallBack {
            // ON ITEM CLICKED
            displayViewOwner(it)
        })
        displayViewOwner(0)
        ((findViewById<ImageView>(R.id.ivClose))).setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
    }

    private fun refreshSideMenu(it: Int?) {
        list.forEachIndexed { index, element ->
            element.isSelected = it == index
        }
        binding.sideMenuRecyclerV.adapter?.notifyDataSetChanged()
    }

    override fun initListeners() {
        observer.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.tvAboutUs.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("from", "about_us")
            startActivity(intent)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            /*getTermsAndConditions("privacy_policy", observer, GenericCallBack {
                handleData(it)
            })*/
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("from", "privacy_policy")
            startActivity(intent)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
        binding.tvTermsConditions.setOnClickListener {
            /* getTermsAndConditions("terms_and_condition", observer, GenericCallBack {
                 handleData(it)
             })*/
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("from", "terms_conditions")
            startActivity(intent)
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
    }

    private fun handleData(it: TermsConditionsModel) {
        when (SharedPrefsHelper.getInstance(this@HomeActivity).getSelectedLanguage()) {
            "en" -> {
                CommonActivity.showTermsAndConditionsMessage(this, it.title_en!!, it.content_en!!)
            }
            "hi" -> {
                CommonActivity.showTermsAndConditionsMessage(this, it.title_hi!!, it.content_hi!!)
            }
            "pa" -> {
                CommonActivity.showTermsAndConditionsMessage(this, it.title_pu!!, it.content_pu!!)
            }
        }
    }

    /**
     *  SetUp Drawer Toggle ...
     */
    private fun setupDrawerToggle() {
        mDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_name
        )
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle?.syncState()
    }

    /**
     *  Show Fragment on Menu List Click ...
     */
    private fun displayViewOwner(position: Int) {
        refreshSideMenu(position) // SHOW SELECTED FRAGMENT TITLE SELECTED
        var fragment: Fragment? = null

        /*
        * 0 my_dhabas
        * 1 notifications
        * 2 discount_offers
        * 3 my_offers
        * 4 customers
        * 5 shops
        * 6 helpline
        * 7 settings
        * */
        when (position) {
            0 -> fragment = HomeFragment()
//            1 -> fragment = NotificationsFragment()
//            2 -> fragment = DiscountsOffersFragment()
//            3 -> fragment = OrdersFragment()
//            4 -> fragment = CustomersFragment()
//            5 -> fragment = ShopsFragment()
            1 -> fragment = HelplineFragment()
            2 -> fragment = SettingsFragment()
        }

        fragment?.let {
            openFragmentReplaceNoAnim(binding.dashboardContainer.id, it, "", true)

            toolbar.title = list[position].title
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
    }

    private fun displayViewExecutive(position: Int) {
        refreshSideMenu(position) // SHOW SELECTED FRAGMENT TITLE SELECTED
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = HomeFragment()
            1 -> {
                binding.drawerLayout.closeDrawer(binding.leftDrawer)
                AddDhabaActivity.start(this)
                return
            }
//            2 -> fragment = NotificationsFragment()
            2 -> fragment = SettingsFragment()
        }

        fragment?.let {
            openFragmentReplaceNoAnim(binding.dashboardContainer.id, it, "", true)

            toolbar.title = list[position].title
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
    }

    override fun openNotificationFragment() {
        if (SharedPrefsHelper.getInstance(this).getUserData()
                .isOwner() || SharedPrefsHelper.getInstance(this).getUserData().isManager()
        )
            displayViewOwner(1)
        else
            displayViewExecutive(2)
    }

    override fun openNotificationSettings() {
        openFragmentReplace(
            binding.dashboardContainer.id,
            NotificationSettings.getInstance(),
            NotificationSettings.TAG,
            true
        )
    }

    fun displayView(position: Int) {
        if (SharedPrefsHelper.getInstance(this).getUserData()
                .isOwner() || SharedPrefsHelper.getInstance(this).getUserData().isManager()
        ) {
            displayViewOwner(position)
        } else /*if (SharedPrefsHelper.getInstance(this).getUserData().isExecutive())*/ {
            displayViewExecutive(position)
        }
    }

    override fun openProfileScreen() {
        openProfileFragment()
    }

    override fun startOver() {
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
        finish()
    }

    override fun openAddDhabaActivity() {
        AddDhabaActivity.start(this)
    }

    override fun onResume() {
        super.onResume()
        refreshSideMenu()
    }

    fun getTermsAndConditions(
        slug: String,
        progressObserver: MutableLiveData<Boolean>,
        callBack: GenericCallBack<TermsConditionsModel>
    ) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                viewModel.executeApi(viewModel.getApiService()?.getTermsAndConditions(slug))
                    .collect {
                        when (it.status) {
                            ApiResult.Status.LOADING -> {
                                progressObserver.value = true
                            }
                            ApiResult.Status.ERROR -> {
                                progressObserver.value = false
                            }
                            ApiResult.Status.SUCCESS -> {
                                progressObserver.value = false
                                it.data?.data?.data?.let {
                                    callBack.onResponse(it)
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(viewModel.getCorrectErrorMessage(e))
            }
        }
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.backStackEntryCount
        if (fragments == 1) {
            GlobalUtils.showCustomConfirmationDialogYesNo(
                this,
                getString(R.string.are_you_sure_to_exit),
                {
                    if (it) {
                        finish()
                    }
                })
        } else if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                refreshSideMenu()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }, 500)
    }

    private fun refreshSideMenu() {
        val myFragment: Fragment? =
            supportFragmentManager.findFragmentById(binding.dashboardContainer.id)
        if (myFragment != null && myFragment.isVisible()) {
            if (myFragment is HomeFragment) {
                if (isExecutive()) {
                    refreshSideMenu(0) // SHOW SELECTED FRAGMENT TITLE SELECTED
                } else {
                    refreshSideMenu(0) // SHOW SELECTED FRAGMENT TITLE SELECTED
                }
            } else if (myFragment is NotificationsFragment) {
                refreshSideMenu(2) // SHOW SELECTED FRAGMENT TITLE SELECTED
            } else if (myFragment is HelplineFragment) {
                if (isExecutive()) {
                    refreshSideMenu(0) // SHOW SELECTED FRAGMENT TITLE SELECTED
                } else {
                    refreshSideMenu(1) // SHOW SELECTED FRAGMENT TITLE SELECTED
                }
            } else if (myFragment is SettingsFragment) {
                if (isExecutive()) {
                    refreshSideMenu(2) // SHOW SELECTED FRAGMENT TITLE SELECTED
                } else {
                    refreshSideMenu(2) // SHOW SELECTED FRAGMENT TITLE SELECTED
                }
            }
        }
    }

    fun isExecutive(): Boolean {
        return SharedPrefsHelper.getInstance(this).getUserData().isExecutive()
    }

}