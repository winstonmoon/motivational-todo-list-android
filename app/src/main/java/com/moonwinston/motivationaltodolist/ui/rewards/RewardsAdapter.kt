package com.moonwinston.motivationaltodolist.ui.rewards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.databinding.ItemRewardsBinding
import com.moonwinston.motivationaltodolist.utils.dateToLocalDate

class RewardsAdapter: ListAdapter<AchievementRateEntity, RewardsAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRewardsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(achievementRateEntity: AchievementRateEntity) {
            val achievedDate = achievementRateEntity.date.dateToLocalDate()
            val achievedDateText = "${achievedDate.year}\n${achievedDate.monthValue}/${achievedDate.dayOfMonth}"
            binding.achievedDateTextView.text = achievedDateText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRewardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AchievementRateEntity>() {
            override fun areItemsTheSame(oldItem: AchievementRateEntity, newItem: AchievementRateEntity): Boolean {
                return oldItem.rate == newItem.rate
            }

            override fun areContentsTheSame(oldItem: AchievementRateEntity, newItem: AchievementRateEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}