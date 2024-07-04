package com.soccersim.presentation.ui.standings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soccersim.domain.models.TeamStandingInfo
import com.soccersim.presentation.R
import com.soccersim.presentation.databinding.ItemTableRowBinding

class StandingsAdapter :
    ListAdapter<TeamStandingInfo, StandingsAdapter.StandingViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingViewHolder {
        val binding =
            ItemTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StandingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StandingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StandingViewHolder(private val binding: ItemTableRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(info: TeamStandingInfo) {
            binding.position.text = info.position.toString()
            binding.teamName.text = info.team.name
            binding.gamesPlayed.text = info.gamesPlayed.toString()
            binding.gamesWon.text = info.gamesWon.toString()
            binding.gamesDrawn.text = info.gamesDrawn.toString()
            binding.gamesLost.text = info.gamesLost.toString()
            binding.goalsFor.text = info.goalsFor.toString()
            binding.goalsAgainst.text = info.goalsAgainst.toString()
            binding.goalDifference.text = info.goalDifference.toString()
            binding.points.text = info.points.toString()

            val backGroundColorRes = if (info.position <= 2) {
                R.color.colorSecondary
            } else {
                R.color.colorSurface
            }

            binding.root.backgroundTintList = ContextCompat.getColorStateList(
                binding.root.context,
                backGroundColorRes
            )
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TeamStandingInfo>() {
            override fun areItemsTheSame(
                oldItem: TeamStandingInfo,
                newItem: TeamStandingInfo
            ): Boolean {
                return oldItem.team == newItem.team
            }

            override fun areContentsTheSame(
                oldItem: TeamStandingInfo,
                newItem: TeamStandingInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}