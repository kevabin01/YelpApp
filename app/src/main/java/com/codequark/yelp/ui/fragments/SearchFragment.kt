package com.codequark.yelp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codequark.yelp.databinding.FragmentSearchBinding
import com.codequark.yelp.interfaces.ItemListener
import com.codequark.yelp.managers.NetworkManager
import com.codequark.yelp.models.business.Business
import com.codequark.yelp.ui.activities.DetailActivity
import com.codequark.yelp.ui.adapters.base.SearchAdapter
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory

class SearchFragment: Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    private lateinit var adapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SearchAdapter(object: ItemListener<Business> {
            override fun onItemSelected(item: Business) {
                if(NetworkManager.isNetworkConnected()) {
                    viewModel.setModel(item)
                    startActivity(Intent(requireContext(), DetailActivity::class.java))
                } else {
                    viewModel.setConnection(true)
                }
            }

            override fun onDataSet(isEmpty: Boolean, itemCount: Int) {
                LogUtils.print("onDataSet: Size: $itemCount")

                val query = viewModel.getQuery()

                if(query.isNotEmpty() && isEmpty) {
                    binding.layoutStart.containerStart.visibility = View.GONE
                    binding.layoutEmpty.containerEmpty.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else if(isEmpty) {
                    binding.layoutStart.containerStart.visibility = View.VISIBLE
                    binding.layoutEmpty.containerEmpty.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.layoutStart.containerStart.visibility = View.GONE
                    binding.layoutEmpty.containerEmpty.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.businesses.observe(viewLifecycleOwner) {
            if(it == null) {
                return@observe
            }

            LogUtils.print("Search: Size: ${it.size}")
            adapter.setContent(it)
        }
    }
}