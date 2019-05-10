package com.example.ownrepositarypatternsample.ui.person

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ownrepositarypatternsample.PersonListBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseFragment
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailActivity
import com.example.ownrepositarypatternsample.utils.extension.currentScope
import com.example.ownrepositarypatternsample.utils.extension.observeLiveData
import com.example.ownrepositarypatternsample.utils.extension.startActivitys
import com.example.ownrepositarypatternsample.utils.extension.toast
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator

class PersonListFragment: BaseFragment<PersonListBinding, MainViewModel>(), PeopleViewHolder.Delegate {
    override val mViewModel: MainViewModel by currentScope<MainActivity>().inject()

    private val adapter = PeopleAdapter(this)
    private lateinit var paginator: RecyclerViewPaginator

    override fun getLayoutId(): Int = R.layout.main_fragment_star

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getPeopleObservable()) { updatePeople(it) }
        mViewModel.postPeoplePage(1)
    }

    private fun initializeUI() {
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        paginator = RecyclerViewPaginator(
                recyclerView = mBinding.recyclerView,
                isLoading = { mViewModel.getPeopleValues()?.status == Status.LOADING },
                loadMore = { loadMore(it) },
                onLast =  { mViewModel.getPeopleValues()?.onLastPage!! })
    }

    private fun updatePeople(resource: Resource<List<Person>>) {
        when(resource.status) {
            Status.SUCCESS -> adapter.addPeople(resource)
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> { }
        }
    }

    private fun loadMore(page: Int) {
        mViewModel.postPeoplePage(page)
    }

    override fun onItemClick(person: Person, view: View) {
        activity?.let {
            PersonDetailActivity.startActivity(this, it, person, view)
        } ?: startActivitys<PersonDetailActivity>("person" to person)
    }
}
