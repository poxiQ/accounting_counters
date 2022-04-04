package glebova.rsue.countwater.ui.statistics

import androidx.fragment.app.viewModels
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.StatisticsFragmentBinding

class StatisticsFragment : BaseFragment<StatisticsFragmentBinding>() {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun initializeBinding() = StatisticsFragmentBinding.inflate(layoutInflater)
}