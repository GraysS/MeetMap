package org.meetmap.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.meetmap.R
import org.meetmap.util.showSnackbar
import org.meetmap.view.settings.buildSettings.SettingsInjector


class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var etEmail: EditText
    private lateinit var etNickname: EditText
    private lateinit var btnFamiliarAdd: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.et_email)
        etNickname = view.findViewById(R.id.et_nickname)
        btnFamiliarAdd = view.findViewById(R.id.btn_familiar_add)
        btnLogout = view.findViewById(R.id.btn_logout)
    }


    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            SettingsInjector(requireActivity().application).provideSettingsViewModelFactory()
        ).get(SettingsViewModel::class.java)

        setClickListeners()
        observeViewModel()
    }

    private fun setClickListeners() {
        btnFamiliarAdd.setOnClickListener {
            viewModel.handleEvent(SettingsEvent.OnClickButtonAddFamiliar(etEmail.text.toString(),
                                                                        etNickname.text.toString())
                                )
        }

        btnLogout.setOnClickListener {
            viewModel.handleEvent(SettingsEvent.OnClickButtonLogout)
        }
    }

    private fun observeViewModel() {
        viewModel.successTx.observe(
            viewLifecycleOwner,{
                showSuccessState(it)
        })

        viewModel.errorTx.observe(
            viewLifecycleOwner, {
                showErrorState(it)
            }
        )

        viewModel.signOut.observe(viewLifecycleOwner, {
            requireActivity().finish()
        })
    }

    private fun showErrorState(errorMessage: String) = view?.showSnackbar(errorMessage, Snackbar.LENGTH_SHORT)
    private fun showSuccessState(successMessage: Int) = view?.showSnackbar(successMessage, Snackbar.LENGTH_SHORT)

}