package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentBankDetailsBinding
import com.transport.mall.model.BankDetailsModel
import com.transport.mall.model.BankNamesModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.addnewdhaba.AddDhabaActivity
import com.transport.mall.ui.customdialogs.DialogAddDhabaSuccess
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import com.transport.mall.utils.xloadImages

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class BankDetailsFragment :
    BaseFragment<FragmentBankDetailsBinding, BankDetailsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_bank_details
    override var viewModel: BankDetailsVM
        get() = setUpVM(activity as AppCompatActivity, BankDetailsVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentBankDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null
    var bankList: ArrayList<BankNamesModel> = ArrayList()
    var banksAdapter: ArrayAdapter<BankNamesModel>? = null

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        mListener = activity as AddDhabaListener
        binding.isUpdate = mListener?.isUpdate()
        binding.viewOnly = mListener?.viewOnly()
        binding.dhabaModelMain = mListener?.getDhabaModelMain()
        binding.currentDate = GlobalUtils.getCurrentDate()
        binding.userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()

        //SETTING EXISTING DATA ON SCREEN
        showDataIfHas()

        // getexecutive, manager roles and show(read only)
/*
        viewModel.getUserByRole(GenericCallBack {
            it.data?.let {
                binding.executiveModel = it
            }
        })
*/
    }

    private fun showDataIfHas() {
        mListener?.getDhabaModelMain()?.bankDetailsModel?.let {
            viewModel.bankModel = it
            it.panPhoto.let {
                if (it.isNotEmpty()) {
                    xloadImages(binding.ivPanPhoto, it, R.drawable.ic_image_placeholder)
                    binding.ivPanPhoto.visibility = View.VISIBLE
                }
            }
        }
        mListener?.getDhabaModelMain()?.dhabaModel?.let {
            viewModel.dhabaModel = it
            setBlockingData(it)
        }
    }

    private fun setBlockingData(it: DhabaModel) {
        viewModel.dhabaModel.blockDay.let {
            when (it) {
                15 -> binding.rb15Days.isChecked = true
                30 -> binding.rb30Days.isChecked = true
                0 -> binding.rbDelisted.isChecked = true
            }
        }
        viewModel.dhabaModel.active?.let {
            when (it) {
                "true" -> binding.rbActive.isChecked = true
                "false" -> binding.rbInactive.isChecked = true
            }
        }
        viewModel.dhabaModel.blockMonth.let {
            viewModel.blockingMonths.set(it.toString())
        }
    }

    override fun initListeners() {
        setupBankNameDropdown()

        binding.rgBlockingFor.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.dhabaModel.blockDay =
                (activity?.findViewById<RadioButton>(i))?.getTag().toString().toInt()
        }
        binding.rgPropertyStatus.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.dhabaModel.status =
                activity?.findViewById<RadioButton>(i)?.getTag().toString()
        }

        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        binding.btnNext.setOnClickListener {
            viewModel.dhabaModel.isDraft = false.toString()
            saveDetails(false)
        }
        binding.btnSaveDraft.setOnClickListener {
            viewModel.dhabaModel.isDraft = true.toString()
            /*if (mListener?.getDhabaModelMain()?.bankDetailsModel != null) {
                mListener?.getDhabaModelMain()?.draftedAtScreen =
                    DhabaModelMain.DraftScreen.BankDetailsFragment.toString()
                mListener?.saveAsDraft()
                activity?.finish()
            } else {*/
            saveDetails(true)
//            }
        }

        binding.llPanPhoto.setOnClickListener {
            launchImagePicker()
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
        //ADDING DEFAULT PLACEHOLDER
        bankList.add(0, BankNamesModel(0, "", getString(R.string.select_bank), "", "", ""))

        banksAdapter =
            ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, bankList)
        binding.spnrBanks.setAdapter(banksAdapter)
        binding.spnrBanks.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0) {
                    viewModel.bankModel.bankName = bankList.get(p2).name!!
                    //GET LIST OF CITIES UNDER SELECTED STATE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        viewModel.bankModel.bankName.let {
            if (it.isNotEmpty()) {
                var index = 0
                for (i in bankList) {
                    if (i.name.equals(it)) {
                        binding.spnrBanks.setSelection(index)
                        break
                    }
                    index += 1
                }
            }
        }
    }

    private fun saveDetails(isDraft: Boolean) {
//        if (isDraft || (!isDraft && isHavingPreviousData())) {
        if (isHavingPreviousData()) {
            if (isDraft) {
                if (viewModel.bankModel.bankName.trim().isNotEmpty()) {
                    proceed(isDraft)
                } else {
                    showToastInCenter(getString(R.string.enter_bank_name))
                }
            } else {
                val ownerMissingParams = (mListener?.getDhabaModelMain()?.ownerModel?.getMissingParameters(getmContext())?.trim()!!)
                val dhabaMissingParams = (mListener?.getDhabaModelMain()?.dhabaModel?.getMissingParameters(getmContext())?.trim()!!)
                if (ownerMissingParams.isNotEmpty() || dhabaMissingParams.isNotEmpty()) {
                    GlobalUtils.showInfoDialog(getmContext(), getString(R.string.details_missing_validation),
                        ownerMissingParams + "\n" + dhabaMissingParams,
                        GenericCallBack {
                            if (ownerMissingParams.isNotEmpty()) {
                                mListener?.showOwnerScreen()
                            } else if (dhabaMissingParams.isNotEmpty()) {
                                mListener?.showDhabaScreen()
                            } else {
                                mListener?.showOwnerScreen()
                            }
                        })
                } else {
                    viewModel.bankModel.hasEverything(
                        getmContext(),
                        GenericCallBackTwoParams() { allOk, message ->
                            if (allOk) {
                                proceed(isDraft)
                            } else {
                                showToastInCenter(message)
                            }
                        })
                }
            }
        }
    }

    private fun proceed(isDraft: Boolean) {
        if (viewModel.bankModel._id.isNotEmpty()) {
            viewModel.updateBankDetail(GenericCallBack {
                handleData(it, isDraft)
            })
        } else {
            viewModel.addBankDetail(GenericCallBack {
                handleData(it, isDraft)
            })
        }
    }

    private fun handleData(it: ApiResponseModel<BankDetailsModel>, isDraft: Boolean) {
        if (it.data != null) {
            mListener?.getDhabaModelMain()?.bankDetailsModel = it.data
            viewModel.bankModel = it.data!!

            viewModel.updateDhabaStatus(
                isDraft,
                viewModel.dhabaModel,
                if (isDraft) DhabaModel.STATUS_PENDING else DhabaModel.STATUS_INPROGRESS,
                viewModel.progressObserver,
                GenericCallBack {
                    if (it.data != null) {
                        mListener?.getDhabaModelMain()?.dhabaModel = it.data
                        if (isDraft) {
                            mListener?.getDhabaModelMain()?.draftedAtScreen =
                                DhabaModelMain.DraftScreen.BankDetailsFragment.toString()
                            mListener?.saveAsDraft()
                            activity?.finish()
                        } else {
//                            if (mListener?.isUpdate()!!) {
//                                showToastInCenter(getString(R.string.updated_successfully))
//                            } else {
                            showSuccessDialog(mListener?.getDhabaModelMain()?.dhabaModel?._id!!)
//                            }
                        }
                    } else {
                        showToastInCenter(it.message)
                    }
                })
        } else {
            showToastInCenter(it.message)
        }
    }

    fun showSuccessDialog(dhabaId: String) {
        DialogAddDhabaSuccess(
            activity as Context,
            dhabaId,
            GenericCallBack {
                when (it) {
                    DialogAddDhabaSuccess.SELECTED_ACTION.GO_HOME -> {
                        goToHomeScreen()
                    }
                    DialogAddDhabaSuccess.SELECTED_ACTION.VIEW_DHABA -> {
                        viewModel.progressObserver.value = true
                        viewModel.getDhabaById(dhabaId, GenericCallBack {
                            viewModel.progressObserver.value = false
                            if (it.data != null) {
                                activity?.finish()
                                AddDhabaActivity.startForUpdate(activity as Context, it.data!!)
                            } else {
                                showToastInCenter(it.message)
                            }
                        })
                    }
                }
            }).show()
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

    private fun isHavingPreviousData(): Boolean {
        if (mListener?.getDhabaModelMain()?.ownerModel == null) {
            showToastInCenter(getString(R.string.enter_owner_details))
            return false
        }/* else if (mListener?.getDhabaModelMain()?.dhabaModel == null) {
            showToastInCenter(getString(R.string.enter_dhaba_details))
            return false
        }*/
        return true
    }

    fun youAreInFocus() {
        mListener?.getDhabaModelMain()?.ownerModel?.let {
            viewModel.bankModel.panNumber = it.panNumber
        }
        mListener?.getDhabaModelMain()?.ownerModel?.let { viewModel.bankModel.user_id = it._id }
        mListener?.getDhabaModelMain()?.dhabaModel?.let { viewModel.dhabaModel = it }
    }
}