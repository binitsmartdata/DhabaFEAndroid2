package com.transport.mall.ui.home.profile.owner.bankdetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentProfileBankDetailsBinding
import com.transport.mall.model.BankNamesModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.customdialogs.DialogDropdownOptions
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OwnerBankDetailsFragment :
    BaseFragment<FragmentProfileBankDetailsBinding, OwnerBankDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_profile_bank_details
    override var viewModel: OwnerBankDetailsVM
        get() = setUpVM(activity as AppCompatActivity, OwnerBankDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentProfileBankDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var bankList: ArrayList<BankNamesModel> = ArrayList()
    lateinit var banksAdapter: ArrayAdapter<BankNamesModel>

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.currentDate = GlobalUtils.getCurrentDate()
        binding.userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()

        //SETTING EXISTING DATA ON SCREEN
        showDataIfHas()
    }

    private fun showDataIfHas() {
        viewModel.bankModel.user_id = SharedPrefsHelper.getInstance(getmContext()).getUserData()._id
    }

    override fun initListeners() {
        setupBankNameDropdown()

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.btnNext.setOnClickListener {
            saveDetails(false)
        }

        binding.llPanPhoto.setOnClickListener {
            launchImagePicker()
        }
        setRxBusListener()
    }

    private fun setRxBusListener() {
        //LISTENER TO LISTEN WHEN TO EXECUTE SAVE BUTTON
        RxBus.listen(DhabaModelMain.ActiveScreen::class.java).subscribe {
            if (it == DhabaModelMain.ActiveScreen.BankDetailsFragment) {
                try {
                    binding.btnNext.performClick()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupBankNameDropdown() {
        AppDatabase.getInstance(getmContext())?.bankDao()
            ?.getAll()?.observe(this, Observer {
                bankList = it as ArrayList<BankNamesModel>
                if (bankList.isNotEmpty()) {
                    setBankNamesAdapter(bankList)
                }
            })
    }

    private fun setBankNamesAdapter(bankList: java.util.ArrayList<BankNamesModel>) {
        banksAdapter =
            ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, bankList)
        binding.edBank.setOnClickListener {
            DialogDropdownOptions(getmContext(), getString(R.string.select_bank), banksAdapter, {
                viewModel.bankModel.bankName = bankList[it].name!!
            }).show()
        }
    }

    private fun saveDetails(isDraft: Boolean) {

    }

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop(16f, 9f)
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            binding.ivPanPhoto.setImageURI(uri)
            binding.ivPanPhoto.visibility = View.VISIBLE
            viewModel.bankModel.panPhoto = getRealPathFromURI(uri)
        }
    }
}