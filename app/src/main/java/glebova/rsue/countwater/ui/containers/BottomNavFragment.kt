package glebova.rsue.countwater.ui.containers

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import glebova.rsue.countwater.R
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentNavContainerBinding

class BottomNavFragment : BaseFragment<FragmentNavContainerBinding>() {

    override fun initializeBinding() = FragmentNavContainerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)
    }
}