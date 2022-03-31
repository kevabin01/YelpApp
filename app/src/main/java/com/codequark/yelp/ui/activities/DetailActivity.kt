package com.codequark.yelp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codequark.yelp.R
import com.codequark.yelp.databinding.ActivityDetailBinding
import com.codequark.yelp.managers.ImageManager
import com.codequark.yelp.ui.adapters.viewPager.DetailPagerAdapter
import com.codequark.yelp.ui.dialogs.LoadingDialog
import com.codequark.yelp.utils.Constants
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    private lateinit var loadingBuilder: LoadingDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingBuilder = LoadingDialog.Builder(this)

        val viewPagerAdapter = DetailPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        val layoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                Constants.PAGE_DETAIL -> {
                    tab.text = "Detalle"
                }

                Constants.PAGE_MAP -> {
                    tab.text = "Mapa"
                }

                Constants.PAGE_LOCATION -> {
                    tab.text = "Ubicación"
                }

                Constants.PAGE_REVIEWS -> {
                    tab.text = "Reseñas"
                }

                else -> {
                    tab.text = "Otro"
                }
            }
        }

        layoutMediator.attach()

        val tab1 = binding.tabLayout.getTabAt(Constants.PAGE_DETAIL)
        val tab2 = binding.tabLayout.getTabAt(Constants.PAGE_MAP)
        val tab3 = binding.tabLayout.getTabAt(Constants.PAGE_LOCATION)
        val tab4 = binding.tabLayout.getTabAt(Constants.PAGE_REVIEWS)

        tab1?.icon?.setTint(ContextCompat.getColor(this, R.color.textColor))
        tab2?.icon?.setTint(ContextCompat.getColor(this, R.color.textColor))
        tab3?.icon?.setTint(ContextCompat.getColor(this, R.color.textColor))
        tab4?.icon?.setTint(ContextCompat.getColor(this, R.color.textColor))

        viewModel.localBusiness.observe(this) { model ->
            if(model == null) {
                return@observe
            }

            ImageManager.instance.setImage(model.imageUrl, binding.layoutDetail.ivImage)
            binding.layoutToolbar.tvTitle.text = model.name
        }

        viewModel.getUpdating().observe(this) { updating ->
            if(updating) {
                loadingBuilder.create()
            } else {
                loadingBuilder.cancel()
            }
        }

        viewModel.getException().observe(this) { ex ->
            ex?.let {
                Toast.makeText(this@DetailActivity, R.string.textError, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getConnection().observe(this) { connection ->
            if(connection) {
                Toast.makeText(this@DetailActivity, R.string.textCheckInternet, Toast.LENGTH_SHORT).show()
            }
        }
    }
}