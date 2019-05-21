package com.example.ownrepositarypatternsample.ui.person.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.example.ownrepositarypatternsample.databinding.ActivityPersonDetailBinding
import com.kotlinlibrary.utils.ktx.fromApi
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.ktx.toApi
import com.kotlinlibrary.utils.ktx.visible
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDetailActivity : BaseActivity<ActivityPersonDetailBinding, PersonDetailViewModel>() {
    override val mViewModel: PersonDetailViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.activity_person_detail

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportPostponeEnterTransition()

        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getPersonObservable()) { updatePersonDetail(it)}
        mViewModel.postPersonId(getPersonFromIntent().id)
    }

    private fun initializeUI() {
        mBinding.incTool.toolbarHome.setOnClickListener { onBackPressed() }
        mBinding.incTool.toolbarTitle.text = getPersonFromIntent().name
        getPersonFromIntent().profilePath?.let {
            Glide.with(this).load(Api.getPosterPath(it))
                    .apply(RequestOptions().circleCrop())
                    .listener(object: RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            supportStartPostponedEnterTransition()
                            initObserve()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            supportStartPostponedEnterTransition()
                            initObserve()
                            return false
                        }
                    })
                    .into(mBinding.personDetailProfile)
        }
        mBinding.personDetailName.text = getPersonFromIntent().name
    }

    private fun updatePersonDetail(resource: ScreenState<PersonDetail>) {
        when(resource) {
            is ScreenState.SuccessState.Api -> {
                resource.data?.let {
                    mBinding.personDetailBiography.text = it.bioGraphy
                    mBinding.detailPersonTags.tags = it.alsoKnownAs

                    if(it.alsoKnownAs.isNotEmpty()) {
                        mBinding.detailPersonTags.visible()
                    }
                }
            }
            is ScreenState.ErrorState.Api -> {
                toast(resource.message)
            }
        }
    }

    private fun getPersonFromIntent(): People {
        return intent.getParcelableExtra("person") as People
    }

    companion object {
        private const val INTENT_REQUEST_CODE = 1000

        fun startActivity(fragment: Fragment, activity: FragmentActivity, people: People, view: View) {
            fromApi(Build.VERSION_CODES.LOLLIPOP, true) {
                val intent = Intent(activity, PersonDetailActivity::class.java)
                ViewCompat.getTransitionName(view)?.let {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, it)
                    intent.putExtra("people", people)
                    activity.startActivityFromFragment(fragment, intent, INTENT_REQUEST_CODE, options.toBundle())
                }
            }
            toApi(Build.VERSION_CODES.LOLLIPOP) {
                activity.startActivityForResult<PersonDetailActivity>(INTENT_REQUEST_CODE, "people" to people)
            }
        }
    }
}
