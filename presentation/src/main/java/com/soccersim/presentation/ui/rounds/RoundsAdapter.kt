package com.soccersim.presentation.ui.rounds

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round
import com.soccersim.presentation.R
import com.soccersim.presentation.databinding.ItemRoundBinding

class RoundsAdapter : ListAdapter<Round, RoundsAdapter.RoundViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundViewHolder {
        val binding = ItemRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class RoundViewHolder(private val binding: ItemRoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(round: Round, position: Int) {
            val context = binding.root.context
            val scorePlaceholder = context.getString(R.string.txt_match_score_placeholder)

            binding.roundTitle.text =
                context.getString(R.string.txt_round_with_number, position + 1)

            binding.firstHomeTeam.text = round.firstMatch.teamA.name
            binding.firstAwayTeam.text = round.firstMatch.teamB.name
            binding.firstScore.text = getMatchScoreText(context, round.firstMatch, scorePlaceholder)

            binding.secondHomeTeam.text = round.secondMatch.teamA.name
            binding.secondAwayTeam.text = round.secondMatch.teamB.name
            binding.secondScore.text =
                getMatchScoreText(context, round.secondMatch, scorePlaceholder)
        }

        private fun getMatchScoreText(context: Context, match: Match, placeholder: String): String {
            return if (match.isComplete) {
                context.getString(R.string.txt_match_score, match.goalsTeamA, match.goalsTeamB)
            } else {
                placeholder
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Round>() {
            override fun areItemsTheSame(oldItem: Round, newItem: Round): Boolean {
                return oldItem.firstMatch.id == newItem.firstMatch.id &&
                        oldItem.secondMatch.id == newItem.secondMatch.id
            }

            override fun areContentsTheSame(oldItem: Round, newItem: Round): Boolean {
                return oldItem == newItem
            }
        }
    }
}