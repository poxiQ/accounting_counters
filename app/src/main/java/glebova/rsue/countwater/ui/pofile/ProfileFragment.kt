package glebova.rsue.countwater.ui.pofile

import android.os.Bundle
import android.view.View
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun initializeBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
