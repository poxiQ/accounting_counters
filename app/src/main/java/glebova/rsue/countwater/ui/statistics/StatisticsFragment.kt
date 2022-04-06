package glebova.rsue.countwater.ui.statistics

import androidx.fragment.app.viewModels
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentStatisticsBinding

class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun initializeBinding() = FragmentStatisticsBinding.inflate(layoutInflater)
}