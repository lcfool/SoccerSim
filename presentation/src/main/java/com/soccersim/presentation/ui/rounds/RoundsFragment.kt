package com.soccersim.presentation.ui.rounds

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.soccersim.domain.models.Round
import com.soccersim.presentation.databinding.FragmentRoundsBinding
import com.soccersim.presentation.ui.common.BaseBindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RoundsFragment : BaseBindingFragment() {

    override val binding by lazy {
        FragmentRoundsBinding.inflate(layoutInflater, null, false)
    }

    private val roundsViewModel by viewModel<RoundsViewModel>()

    private val roundsAdapter = RoundsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setOnClickListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.adapter = roundsAdapter
        }
    }

    private fun observeData() {
        roundsViewModel.fetchMatches()
        roundsViewModel.rounds.observe(viewLifecycleOwner, ::updateRoundsList)
        roundsViewModel.isLoading.observe(viewLifecycleOwner, ::enablePreloader)
    }

    private fun setOnClickListeners() {
        binding.fabSimulate.setOnClickListener {
            roundsViewModel.simulateMatches()
        }
    }

    private fun updateRoundsList(list: List<Round>) {
        roundsAdapter.submitList(list)
    }

    private fun enablePreloader(isLoading: Boolean) {
        binding.loadingOverlay.isVisible = isLoading
        binding.fabSimulate.isVisible = !isLoading
    }
}