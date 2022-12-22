package com.moonwinston.motivationaltodolist.ui.rewards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.AchievementRateEntity
import com.moonwinston.motivationaltodolist.databinding.ItemRewardsBinding
import java.text.SimpleDateFormat
import java.util.*

class RewardsAdapter: ListAdapter<AchievementRateEntity, RewardsAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRewardsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(achievementRateEntity: AchievementRateEntity) {
            //TODO fix month
            val cal = Calendar.getInstance().apply {
                this.time = achievementRateEntity.date
            }
            val formatter = Formatter()

            val year = cal.get(Calendar.YEAR)
            val month = formatter.format("%tm", cal)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            binding.achievedDateTextView.text = "$year\n$month/$day"
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
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: AchievementRateEntity, newItem: AchievementRateEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}