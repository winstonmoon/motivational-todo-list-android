package com.moonwinston.motivationaltodolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.databinding.FragmentMonthlyBinding
import com.moonwinston.motivationaltodolist.adapters.MonthlyScreenSlidePagerAdapter
import com.moonwinston.motivationaltodolist.viewmodels.MonthlyViewModel
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MonthlyFragment : Fragment(){

    private lateinit var binding: FragmentMonthlyBinding
    private lateinit var monthlyViewModel: MonthlyViewModel
    private val sharedViewModel: SharedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        monthlyViewModel =
            ViewModelProvider(this).get(MonthlyViewModel::class.java)
        binding = FragmentMonthlyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewpagerCalendar.adapter = MonthlyScreenSlidePagerAdapter(this)
        binding.viewpagerCalendar.setCurrentItem(MonthlyScreenSlidePagerAdapter.START_POSITION, false)

        binding.buttonSettings.setOnClickListener {
            it.findNavController().navigate(R.id.action_monthly_to_settings)
        }

        binding.buttonAdd.setOnClickListener {
            it.findNavController().navigate(R.id.action_monthly_to_add)
        }
    }
}