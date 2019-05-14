package com.example.ownrepositarypatternsample.ui.person

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ownrepositarypatternsample.BR
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseFragment
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailActivity
import com.example.ownrepositarypatternsample.utils.extension.currentScope
import com.kotlinlibrary.loadmore.item.ErrorItem
import com.kotlinlibrary.loadmore.item.LoadingItem
import com.kotlinlibrary.loadmore.paginate.Direction
import com.kotlinlibrary.loadmore.paginate.NoPaginate
import com.kotlinlibrary.recycleradapter.setUpBinding
import com.kotlinlibrary.recycleradapter.simple.SingleBindingAdapter
import timber.log.Timber
import com.example.ownrepositarypatternsample.databinding.ItemPersonBinding
import com.example.ownrepositarypatternsample.databinding.MainFragmentStarBinding
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.navigate.launchActivity
import org.jetbrains.anko.toast

class PersonListFragment: BaseFragment<MainFragmentStarBinding, MainViewModel>() {
    override val mViewModel: MainViewModel by currentScope<MainActivity>().inject()
    private var adapter: SingleBindingAdapter<People>? = null
    private lateinit var noPaginate: NoPaginate

    override fun getLayoutId(): Int = R.layout.main_fragment_star

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getPeopleListObservable()) { updatePeople(it) }
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
                } ?: launchActivity<PersonDetailActivity>(params = *arrayOf("person" to item))
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
        noPaginate.unbind()
        super.onDestroy()
    }

    private fun updatePeople(resource: Resource<List<People>>) {
        when(resource.status) {
            Status.SUCCESS ->  {
                Timber.e("Load People List ${resource.data}")
                noPaginate.showLoading(false)
                noPaginate.setNoMoreItems(mViewModel.getPeopleListValues()?.onLastPage!!)
                resource.data?.let {
                    adapter?.addAll(it.toMutableList())
                }
            }
            Status.ERROR -> {
                Timber.e("Error People List")
                mContext.toast(resource.message.toString())
                noPaginate.showLoading(false)
                noPaginate.showError(true)
                noPaginate.setNoMoreItems(mViewModel.getPeopleListValues()?.onLastPage!!)
            }
            Status.LOADING -> {
                Timber.e("Loading People List")
                noPaginate.showError(false)
                noPaginate.showLoading(true)
            }
        }
    }
}
