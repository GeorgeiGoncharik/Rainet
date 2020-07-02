package xyz.goshanchik.rainet.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.goshanchik.rainet.R
import xyz.goshanchik.rainet.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.nextButton.setOnClickListener {
            viewModel.isPasswordValid(binding.passwordEditText.text)
            viewModel.navigateToTrackerFragment()
        }

        viewModel.isPasswordValid.observe(viewLifecycleOwner, Observer {
            if(it)
                binding.passwordTextInput.error = null
            else
                binding.passwordTextInput.error = getString(R.string.password_short_error)
        })

        viewModel.navigateToTrackerFragment.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPlantTrackerFragment())
                viewModel.onNavigateToTrackerFragment()
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}