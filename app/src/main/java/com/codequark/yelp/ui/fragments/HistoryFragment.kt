package com.codequark.yelp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codequark.yelp.databinding.FragmentHistoryBinding
import com.codequark.yelp.interfaces.ItemListener
import com.codequark.yelp.managers.NetworkManager
import com.codequark.yelp.room.models.LocalBusiness
import com.codequark.yelp.ui.activities.DetailActivity
import com.codequark.yelp.ui.adapters.pagingAdater.HistoryAdapter
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryFragment: Fragment() {
    private lateinit var binding: FragmentHistoryBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = HistoryAdapter(object: ItemListener<LocalBusiness> {
            override fun onItemSelected(@NonNull item: LocalBusiness) {
                if(NetworkManager.isNetworkConnected()) {
                    viewModel.setModel(item)
                    startActivity(Intent(requireContext(), DetailActivity::class.java))
                } else {
                    viewModel.setConnection(true)
                }
            }

            override fun onDataSet(isEmpty: Boolean, itemCount: Int) {
                if(isEmpty) {
                    binding.layoutStart.containerStart.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.layoutStart.containerStart.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.localBusinesses.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}