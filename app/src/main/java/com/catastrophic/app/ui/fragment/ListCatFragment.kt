package com.catastrophic.app.ui.fragment

import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catastrophic.app.R
import com.catastrophic.app.databinding.FragmentListCatBinding
import com.catastrophic.app.ui.adapter.CatsAdapter
import com.catastrophic.app.ui.navigator.ListCatNavigator
import com.catastrophic.app.ui.base.BaseFragment
import com.catastrophic.app.ui.viewmodel.ListCatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListCatFragment : BaseFragment<FragmentListCatBinding, ListCatViewModel>(true),
    ListCatNavigator {

    private val userViewModel: ListCatViewModel by viewModels()
    private val mainNavController by lazy { requireActivity().findNavController(R.id.nav_host_main) }


    private lateinit var binding: FragmentListCatBinding
    private lateinit var usersAdapter: CatsAdapter

    override fun setLayout(): Int = R.layout.fragment_list_cat

    override fun getViewModels(): ListCatViewModel = userViewModel


    override fun onInitialization() {
        super.onInitialization()
        binding = getViewDataBinding()
        binding.lifecycleOwner = this
        userViewModel.navigator = this
    }

    override fun onReadyAction() {
        usersAdapter = CatsAdapter(CatsAdapter.UserComparator) {

        }

        binding.rvCat.apply {
            visibility = View.VISIBLE
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = usersAdapter

        }

        binding.refreshCats.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                usersAdapter.refresh()
                binding.refreshCats.isRefreshing = false
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onObserveAction() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.catsFlow.collectLatest { pagingData ->
                usersAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            usersAdapter.loadStateFlow.collectLatest { loadStates ->

                when {
                    loadStates.refresh is LoadState.Loading -> {

                        binding.tvError.visibility = View.GONE

                        binding.skeletonCat.showSkeleton()
                    }
                    loadStates.refresh is LoadState.Error -> {
                        if (usersAdapter.itemCount == 0) {
                            binding.tvError.visibility = View.VISIBLE
                        }
                        binding.skeletonCat.showOriginal()
                        Toast.makeText(
                            requireContext(),
                            (loadStates.refresh as LoadState.Error).error.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    loadStates.append is LoadState.Error -> {
                        binding.tvError.visibility = View.GONE
                        binding.skeletonCat.showOriginal()
                        Toast.makeText(
                            requireContext(),
                            (loadStates.append as LoadState.Error).error.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        binding.skeletonCat.showOriginal()
                        binding.tvError.visibility = View.GONE
                    }
                }
            }
        }
    }
}