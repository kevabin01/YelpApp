package com.codequark.yelp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codequark.yelp.R
import com.codequark.yelp.databinding.FragmentRegisterBinding
import com.codequark.yelp.utils.Constants.LoginStateDef
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    @NonNull
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoginState().observe(viewLifecycleOwner) { integer ->
            when (integer) {
                LoginStateDef.STATE_DEFAULT, LoginStateDef.STATE_LOGIN_ERROR_PASSWORD, LoginStateDef.STATE_LOGIN_ERROR_NOT_EXISTS, LoginStateDef.STATE_LOGIN_ERROR_MANY_REQUESTS -> {}

                LoginStateDef.STATE_LOGIN_SUCCESS -> {
                    viewModel.setUpdating(false)

                    viewModel.setDestination(R.id.navigationSearch)
                }

                LoginStateDef.STATE_LOGIN_ERROR -> {
                    viewModel.setUpdating(false)
                    showToast(R.string.textError)
                }

                LoginStateDef.STATE_LOGIN_ERROR_NETWORK -> {
                    viewModel.setUpdating(false)
                    showToast(R.string.textCheckInternet)
                }

                LoginStateDef.STATE_LOGIN_ERROR_EMAIL -> {
                    viewModel.setUpdating(false)
                    showToast(R.string.textUsuarioInvalido)
                }

                LoginStateDef.STATE_LOGIN_ERROR_EMAIL_EMPTY -> {
                    binding.edtEmail.error = text(R.string.textUsuarioVacio)
                    binding.edtEmail.requestFocus()
                }

                LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_EMPTY -> {
                    binding.edtPassword.error = text(R.string.textPasswordVacio)
                    binding.edtPassword.requestFocus()
                }

                LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_LENGTH -> {
                    binding.edtPassword.error = text(R.string.textErrorCampoLongitud)
                    binding.edtPassword.requestFocus()
                }

                LoginStateDef.STATE_LOGIN_ERROR_EXISTS -> {
                    viewModel.setUpdating(false)
                    showToast(R.string.textUsuarioExiste)
                }
            }
        }
    }

    private fun showToast(@StringRes resource: Int) {
        Toast.makeText(requireContext(), resource, Toast.LENGTH_SHORT).show()
    }

    private fun text(@StringRes key: Int): String {
        return resources.getString(key)
    }
}