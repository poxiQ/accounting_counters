package glebova.rsue.countwater.ui.master

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import glebova.rsue.countwater.BlankFragment
import glebova.rsue.countwater.R

class MasterFragment : Fragment() {

    companion object {
        fun newInstance() = MasterFragment()
    }

    private lateinit var viewModel: MasterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.master_fragment, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}