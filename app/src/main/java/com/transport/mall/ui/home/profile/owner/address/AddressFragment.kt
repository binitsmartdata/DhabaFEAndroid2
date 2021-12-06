package com.transport.mall.ui.home.profile.owner.address

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentAddressBinding
import com.transport.mall.model.CityModel
import com.transport.mall.model.HighwayModel
import com.transport.mall.model.StateModel
import com.transport.mall.model.UserModel
import com.transport.mall.ui.customdialogs.DialogDropdownOptions
import com.transport.mall.ui.customdialogs.DialogHighwaySelection
import com.transport.mall.ui.customdialogs.DialogProfileUpdate
import com.transport.mall.ui.home.profile.owner.OwnerProfileVM
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class AddressFragment : BaseFragment<FragmentAddressBinding, OwnerProfileVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_address
    override var viewModel: OwnerProfileVM
        get() = setUpVM(this, OwnerProfileVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddressBinding
        get() = setUpBinding()
        set(value) {}

    var userData: UserModel? = null
    var StateList = ArrayList<StateModel>()
    lateinit var statesAdapter: ArrayAdapter<StateModel>

    override fun bindData() {
        binding.lifecycleOwner = this
        setUserData()
    }

    private fun setUserData() {
        userData = SharedPrefsHelper.getInstance(activity as Context).getUserData()
        userData?.let {
            viewModel.userModel = it
//            binding.userModel = viewModel.userModel
            binding.viewModel = viewModel

            it.state?.let {
                AppDatabase.getInstance(getmContext())?.statesDao()?.getByName(it)?.observe(this, {
                    if (it.isNotEmpty()) {
                        //GET LIST OF CITIES UNDER SELECTED STATE
                        AppDatabase.getInstance(getmContext())?.cityDao()
                            ?.getAllByState(it.get(0).stateCode!!)
                            ?.observe(viewLifecycleOwner, Observer {
                                it?.let { setCitiesAdapter(it as ArrayList<CityModel>) }
                            })
                    }
                })

            }
        }
    }

    override fun initListeners() {
        propertyStatusListener()
        setupCitiesAndStateView()

        binding.edHighway.setOnClickListener {
            AppDatabase.getInstance(getmContext())?.highwayDao()?.getAll()
                ?.observe(this, Observer {
                    DialogHighwaySelection(
                        getmContext(),
                        true,
                        it as ArrayList<HighwayModel>,
                        GenericCallBack {
                            viewModel.userModel.highway = it.highwayNumber!!
                        }).show()
                })
        }

        viewModel.progressObserverUpdate.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.updating_profile))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.progressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.deleting_profile_photo))
            } else {
                hideProgressDialog()
            }
        })

        binding.btnUpdateProfile.setOnClickListener {
//            viewModel.userModel = binding.userModel as UserModel

            viewModel.updateAddressData(GenericCallBack {
                if (it.data != null) {
                    viewModel.userModel = it.data!!

                    SharedPrefsHelper.getInstance(getmContext()).setUserData(it.data!!)
//                    showToastInCenter(getString(R.string.profile_updated))
                    DialogProfileUpdate(getmContext()).show()

                    //NOTIFY THAT USER MODEL IS UPDATED
                    RxBus.publish(it.data!!)
                } else {
                    showToastInCenter(it.message)
                }
            })
        }
    }

    private fun setupCitiesAndStateView() {
        //SET LOCALLY SAVED STATES
        AppDatabase.getInstance(getmContext())?.statesDao()
            ?.getAll()?.observe(this, Observer {
                StateList = it as ArrayList<StateModel>
                if (StateList.isNotEmpty()) {
                    setStatesAdapter(StateList)
                }
            })
    }

    private fun setStatesAdapter(stateList: ArrayList<StateModel>) {
        statesAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, stateList
        )
        binding.edState.setOnClickListener {
            DialogDropdownOptions(getmContext(), getString(R.string.select_state), statesAdapter, {
                viewModel.userModel.state = stateList[it].name_en!!
                viewModel.userModel.city = arrayOf("")

                //GET LIST OF CITIES UNDER SELECTED STATE
                AppDatabase.getInstance(getmContext())?.cityDao()
                    ?.getAllByState(stateList.get(it).stateCode!!)
                    ?.observe(viewLifecycleOwner, Observer {
                        it?.let { setCitiesAdapter(it as ArrayList<CityModel>) }
                    })
            }).show()
        }
    }

    private fun setCitiesAdapter(cityList: ArrayList<CityModel>) {
        //SET CITIES ADAPTER ON SPINNER
        var citiesAdapter = ArrayAdapter<CityModel>(
            activity as Context,
            android.R.layout.simple_list_item_1, cityList
        )
        binding.edCity.setOnClickListener {
            DialogDropdownOptions(getmContext(), getString(R.string.select_city), citiesAdapter, {
                Log.e("selected city", cityList[it].name_en!!)
                viewModel.userModel.city = arrayOf(cityList[it].name_en!!)
            }).show()
        }
    }

    private fun propertyStatusListener() {
        // SET ITEM SELECTED LISTENER ON PROPERTY STATUS SPINNER
        /*
        val menuArray = resources.getStringArray(R.array.property_status)
        var designationAdapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_list_item_1, menuArray
        )

        binding.edPropertyStatus.setOnClickListener {
            DialogDropdownOptions(getmContext(), getString(R.string.property_status), designationAdapter, {
                viewModel.userModel.propertyStatus = menuArray[it]
            }).show()
        }
*/
    }
}