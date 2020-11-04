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
import com.kotlinlibrary.utils.arguments.bindArgument
import com.kotlinlibrary.utils.ktx.fromApi
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.ktx.toApi
import com.kotlinlibrary.utils.ktx.visible
import com.kotlinlibrary.utils.navigate.launchActivity

class PersonDetailActivity : BaseActivity<ActivityPersonDetailBinding, PersonDetailViewModel>(R.layout.activity_person_detail) {

    private val people: People by bindArgument("person")

    override fun initObserve() {
        observeLiveData(mViewModel.getPersonObservable()) { updatePersonDetail(it) }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportPostponeEnterTransition()
        initializeUI()
    }

    private fun initializeUI() {
        mBinding.incTool.toolbarHome.setOnClickListener { onBackPressed() }
        mBinding.incTool.toolbarTitle.text = people.name
        people.profilePath?.let {
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
        mBinding.personDetailName.text = people.name
        mViewModel.postPersonId(people.id)
    }

    private fun updatePersonDetail(resource: ScreenState<PersonDetail>) {
        when(resource) {
            is ScreenState.LoadingState.Show -> {
                showAlertView(true)
            }
            is ScreenState.LoadingState.Hide -> {
                showAlertView(false)
            }
            is ScreenState.SuccessState.Api -> {
                resource.data?.let {
                    mBinding.personDetailBiography.text = it.bioGraphy
                    mBinding.detailPersonTags.tags = it.alsoKnownAs

                    if(!it.alsoKnownAs.isNullOrEmpty()) {
                        mBinding.detailPersonTags.visible()
                    }
                }
            }
            is ScreenState.ErrorState.Api -> {
                mViewModel.message.postValue(resource.message)
            }
            else -> {}
        }
    }

    companion object {
        private const val INTENT_REQUEST_CODE = 1000

        fun startActivity(fragment: Fragment, activity: FragmentActivity, people: People, view: View) {
            fromApi(Build.VERSION_CODES.LOLLIPOP, true) {
                val intent = Intent(activity, PersonDetailActivity::class.java)
                ViewCompat.getTransitionName(view)?.let {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, it)
                    intent.putExtra("person", people)
                    activity.startActivityFromFragment(fragment, intent, INTENT_REQUEST_CODE, options.toBundle())
                }
            }
            toApi(Build.VERSION_CODES.LOLLIPOP) {
                activity.launchActivity<PersonDetailActivity>(
                    params = arrayOf("person" to people), resultCode = INTENT_REQUEST_CODE
                )
            }
        }
    }
}
