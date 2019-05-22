package com.example.ownrepositarypatternsample.ui.person

import androidx.recyclerview.widget.GridLayoutManager
import com.example.ownrepositarypatternsample.BR
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseFragment
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailActivity
import com.kotlinlibrary.loadmore.item.ErrorItem
import com.kotlinlibrary.loadmore.item.LoadingItem
import com.kotlinlibrary.loadmore.paginate.Direction
import com.kotlinlibrary.loadmore.paginate.NoPaginate
import com.kotlinlibrary.recycleradapter.setUpBinding
import com.kotlinlibrary.recycleradapter.simple.SingleBindingAdapter
import com.example.ownrepositarypatternsample.databinding.ItemPersonBinding
import com.example.ownrepositarypatternsample.databinding.MainFragmentStarBinding
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.navigate.launchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonListFragment: BaseFragment<MainFragmentStarBinding, MainViewModel>(R.layout.main_fragment_star) {
    override val mViewModel: MainViewModel by viewModel()

    private var noPaginate: NoPaginate? = null
    private var adapter: SingleBindingAdapter<People>? = null

    override fun initObserve() {
        observeLiveData(mViewModel.getPeopleListObservable()) { updatePeople(it) }

        initializeUI()
    }

    private fun initializeUI() {
        adapter = mBinding.recyclerView.setUpBinding<People> {
            withLayoutManager(GridLayoutManager(context, 2))
            withLayoutResId(R.layout.item_person)
            onBind<ItemPersonBinding>(BR.data) { _, _ ->
            }
            onClick(R.id.item_person_profile) { view, _, item ->
                activity?.let { act ->
                    PersonDetailActivity.startActivity(this@PersonListFragment, act, item, view)
                } ?: mContext.launchActivity<PersonDetailActivity>(params = *arrayOf("person" to item))
            }
            withItems(mutableListOf())
        }
        setupLoadMore()
    }

    private fun setupLoadMore() {
        noPaginate = NoPaginate {
            loadingTriggerThreshold = 0
            recyclerView = mBinding.recyclerView
            loadingItem = LoadingItem.DEFAULT
            errorItem = ErrorItem.DEFAULT
            direction = Direction.DOWN
            onLoadMore = {
                mViewModel.postPeoplePage()
            }
        }
    }

    override fun onDestroy() {
        noPaginate?.unbind()
        super.onDestroy()
    }

    private fun updatePeople(resource: ScreenState<List<People>>) {
        when(resource) {
            is ScreenState.LoadingState.Show -> {
                if(resource.isInitial) {
                    showAlertView(true)
                } else {
                    noPaginate?.showError(false)
                    noPaginate?.showLoading(true)
                }
            }
            is ScreenState.LoadingState.Hide -> {
                if(resource.isInitial) {
                    showAlertView(false)
                } else {
                    noPaginate?.showLoading(false)
                }
            }
            is ScreenState.SuccessState.Api -> {
                noPaginate?.setNoMoreItems(resource.onLastPage)
                resource.data?.let {
                    val list = adapter?.getItemLists()?.toMutableList() ?: mutableListOf()
                    list.addAll(it.toMutableList())
                    adapter?.reSet(list.distinct().toMutableList())
                }
            }
            is ScreenState.ErrorState.Api -> {
                noPaginate?.showError(true)
                mViewModel.message.postValue(resource.message)
            }
        }
    }
}
