package com.soccersim.presentation.ui.standings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.soccersim.domain.models.TeamStandingInfo
import com.soccersim.presentation.R
import com.soccersim.presentation.databinding.FragmentStandingsBinding
import com.soccersim.presentation.ui.common.BaseBindingFragment
import com.soccersim.presentation.ui.util.ItemMarginDecorator
import com.soccersim.presentation.ui.util.Offsets
import org.koin.androidx.viewmodel.ext.android.viewModel

class StandingsFragment : BaseBindingFragment() {

    override val binding by lazy {
        FragmentStandingsBinding.inflate(layoutInflater, null, false)
    }

    private val standingsViewModel by viewModel<StandingsViewModel>()
    private val standingsAdapter = StandingsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.adapter = standingsAdapter
            binding.recyclerView.addItemDecoration(
                ItemMarginDecorator(
                    Offsets(
                        topDp = 8,
                        bottomDp = 0,
                        leftDp = 16,
                        rightDp = 16
                    )
                )
            )
        }
    }

    private fun observeData() {
        standingsViewModel.fetchTeams()

        standingsViewModel.standings.observe(viewLifecycleOwner, ::updateStandings)

        standingsViewModel.isLoading.observe(viewLifecycleOwner, ::enablePreloader)
    }

    private fun updateStandings(standings: List<TeamStandingInfo>) {
        binding.noStandingsOverlay.isVisible = standings.isEmpty()
        standingsAdapter.submitList(standings)
    }

    private fun enablePreloader(isLoading: Boolean) {
        binding.loadingOverlay.isVisible = isLoading
    }
}