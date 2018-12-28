package com.example.ownrepositarypatternsample.ui.person.detail

import android.content.Intent
import android.graphics.drawable.Drawable
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
import com.example.ownrepositarypatternsample.PersonDetailBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.InjectActivity
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.example.ownrepositarypatternsample.utils.extension.checkIsMaterialVersion
import com.example.ownrepositarypatternsample.utils.extension.observeLiveData
import com.example.ownrepositarypatternsample.utils.extension.visible
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class PersonDetailActivity : InjectActivity<PersonDetailBinding, PersonDetailViewModel>() {

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
        getPersonFromIntent().profile_path?.let {
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

    private fun updatePersonDetail(resource: Resource<PersonDetail>) {
        when(resource.status) {
            Status.SUCCESS -> {
                resource.data?.let {
                    mBinding.personDetailBiography.text = it.biography
                    mBinding.detailPersonTags.tags = it.also_known_as

                    if(it.also_known_as.isNotEmpty()) {
                        mBinding.detailPersonTags.visible()
                    }
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> { }
        }
    }

    private fun getPersonFromIntent(): Person {
        return intent.getParcelableExtra("person") as Person
    }

    companion object {
        const val intent_requestCode = 1000

        fun startActivity(fragment: Fragment, activity: FragmentActivity, person: Person, view: View) {
            if (activity.checkIsMaterialVersion()) {
                val intent = Intent(activity, PersonDetailActivity::class.java)
                ViewCompat.getTransitionName(view)?.let {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, it)
                    intent.putExtra("person", person)
                    activity.startActivityFromFragment(fragment, intent, intent_requestCode, options.toBundle())
                }
            } else {
                activity.startActivityForResult<PersonDetailActivity>(intent_requestCode, "person" to person)
            }
        }
    }
}
