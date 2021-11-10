package com.fabfm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabfm.MainActivity
import com.fabfm.R
import com.fabfm.databinding.FragmentHomeBinding
import com.fabfm.ui.home.model.BrowseState
import io.reactivex.disposables.CompositeDisposable
import model.RadioTimeTransformer
import service.RadioTimeServiceImpl
import service.getRadioTimeApi

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var browseAdapter: BrowseAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val radioTimeService = RadioTimeServiceImpl(getRadioTimeApi(), RadioTimeTransformer())
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(radioTimeService))
            .get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpAdapter()
        return root
    }

    private fun setUpAdapter() {
        browseAdapter = BrowseAdapter(emptyList())
        binding.elementList.apply {
            adapter = browseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(
            homeViewModel.state()
                .subscribe { state ->
                    when (state) {
                        is BrowseState.Success -> renderSuccess(state)
                        BrowseState.Empty -> renderEmptyState()
                        BrowseState.Error -> showError()
                        BrowseState.Loading -> renderLoadingState()
                    }
                }
        )

        homeViewModel.loadData()
    }

    private fun renderSuccess(state: BrowseState.Success) {
        hideLoadingSpinner()
        (activity as MainActivity).setActionBarTitle(state.title.orEmpty())
        browseAdapter.updateData(state.elements)
    }

    private fun renderLoadingState() {
        showLoadingSpinner()
    }

    private fun showError() {
        hideLoadingSpinner()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.try_again) { _, _ ->
                showLoadingSpinner()
                homeViewModel.loadData()
            }
            .show()
        renderEmptyState()
    }

    private fun renderEmptyState() {
        hideLoadingSpinner()
        browseAdapter.updateData(emptyList())
    }

    private fun showLoadingSpinner() {
        binding.loading.isVisible = true
    }

    private fun hideLoadingSpinner() {
        binding.loading.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}