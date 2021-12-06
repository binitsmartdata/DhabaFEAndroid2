package com.transport.mall.ui.termsandconditions

import android.os.Build
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.text.HtmlCompat
import com.transport.mall.R
import com.transport.mall.databinding.FragmentTermsAndConditionsBinding
import com.transport.mall.model.UserModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class TermsAndConditionsFragment(val html: String) : BaseFragment<FragmentTermsAndConditionsBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_terms_and_conditions
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentTermsAndConditionsBinding
        get() = setUpBinding()
        set(value) {}

    var userModel: UserModel? = UserModel()
    var prefix = "91"

    override fun bindData() {
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        try {
            if (Build.VERSION.SDK_INT >= 24) {
                binding.tvText.text = Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else { // FOR OLD DEVICES
                binding.tvText.text = Html.fromHtml(html)
            }
        } catch (e: Exception) {
            binding.tvText.text = html
        }
    }

    override fun initListeners() {


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_close, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionClose -> {
                activity?.finish()
                return true
            }
        }
        return false
    }

}