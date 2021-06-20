package com.moonwinston.motivationaltodolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.data.CalendarDate
import com.moonwinston.motivationaltodolist.data.Task
import com.moonwinston.motivationaltodolist.databinding.ItemMonthlyCalendarBinding
import com.moonwinston.motivationaltodolist.databinding.ItemRewardsBinding
import java.text.SimpleDateFormat

class RewardAdapter : ListAdapter<Task, RewardAdapter.ViewHolder>(RewardAdapter.diffUtil) {

    //    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
//        val binding = ItemRewardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TaskHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
//        holder.onBindViewHolder(tasks[position])
//        holder.binding.executePendingBindings()
//    }
//
//    override fun getItemCount(): Int {
//        return tasks.size
//    }
//
//    inner class TaskHolder(val binding: ItemRewardsBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun onBindViewHolder(task: Task) {
//            binding.task = task
//        }
//    }
//}
    inner class ViewHolder(private val binding: ItemRewardsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            //TODO fix format
            binding.textAchieveddate.text = task.taskDate.toString()
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
        val diffUtil = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}