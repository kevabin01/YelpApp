package com.codequark.yelp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codequark.yelp.databinding.FragmentDetailBinding
import com.codequark.yelp.ui.adapters.base.DetailAdapter
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory

class LocationFragment: Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    private lateinit var adapter: DetailAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DetailAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.localBusiness.observe(viewLifecycleOwner) { model ->
            if(model == null) {
                return@observe
            }

            adapter.setContent(viewModel.getInfoLocation(model))
        }

        binding.layoutEmpty.containerEmpty.visibility = View.GONE
    }
}