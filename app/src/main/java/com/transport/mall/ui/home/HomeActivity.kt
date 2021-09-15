package com.transport.mall.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.ActivityHomeBinding
import com.transport.mall.model.SideMenu
import com.transport.mall.model.Toolbar
import com.transport.mall.ui.addnewdhaba.AddDhabaActivity
import com.transport.mall.ui.authentication.pre_login.splash.SplashActivity
import com.transport.mall.ui.home.helpline.EditProfileFragment
import com.transport.mall.ui.home.notifications.NotificationsFragment
import com.transport.mall.ui.home.settings.SettingsFragment
import com.transport.mall.ui.home.sidemenu.SideMenuAdapter
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


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
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun bindData() {
        setUpToolbar()
        setUpSideMenu()
        setUserData()
    }

    private fun setUserData() {
        binding.userModel = SharedPrefsHelper.getInstance(this).getUserData()
        binding.user.ivEdit.setOnClickListener {
            openProfileFragment()
        }
    }

    public fun openProfileFragment() {
        openFragmentReplaceNoAnim(
            binding.dashboardContainer.id,
            EditProfileFragment(),
            "editProfile",
            false
        )
        toolbar.title = getString(R.string.edit_profile)
        binding.drawerLayout.closeDrawer(binding.leftDrawer)
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
    }

    /**
     *  SetUp Side Menu items ...
     */
    private fun setUpSideMenu() {
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
            displayView(it)
        })
        displayView(0)
    }

    private fun refreshSideMenu(it: Int?) {
        list.forEachIndexed { index, element ->
            element.isSelected = it == index
        }
        binding.sideMenuRecyclerV.adapter?.notifyDataSetChanged()
    }

    override fun initListeners() {

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
    private fun displayView(position: Int) {
        refreshSideMenu(position) // SHOW SELECTED FRAGMENT TITLE SELECTED
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = HomeFragment()
            1 -> {
                binding.drawerLayout.closeDrawer(binding.leftDrawer)
                AddDhabaActivity.start(this)
                return
            }
            2 -> fragment = NotificationsFragment()
//            3 -> fragment = HelplineFragment()
            3 -> fragment = SettingsFragment()
        }

        fragment?.let {
            openFragmentReplaceNoAnim(binding.dashboardContainer.id, it, "", false)

            toolbar.title = list[position].title
            binding.drawerLayout.closeDrawer(binding.leftDrawer)
        }
    }

    override fun openNotificationFragment() {
        displayView(2)
    }

    override fun openProfileScreen() {
        openProfileFragment()
    }

    override fun startOver() {
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        finish()
    }

    override fun openAddDhabaActivity() {
        AddDhabaActivity.start(this)
    }

    override fun onResume() {
        super.onResume()
        val myFragment: Fragment? =
            supportFragmentManager.findFragmentById(binding.dashboardContainer.id)
        if (myFragment != null && myFragment.isVisible()) {
            if (myFragment is HomeFragment) {
                refreshSideMenu(0) // SHOW SELECTED FRAGMENT TITLE SELECTED
            } else if (myFragment is NotificationsFragment) {
                refreshSideMenu(2) // SHOW SELECTED FRAGMENT TITLE SELECTED
            } else if (myFragment is EditProfileFragment) {
                refreshSideMenu(3) // SHOW SELECTED FRAGMENT TITLE SELECTED
            } else if (myFragment is SettingsFragment) {
                refreshSideMenu(4) // SHOW SELECTED FRAGMENT TITLE SELECTED
            }
        }
    }
}