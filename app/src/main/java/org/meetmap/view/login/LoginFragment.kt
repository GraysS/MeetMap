package org.meetmap.view.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.meetmap.R
import org.meetmap.util.showSnackbar
import org.meetmap.view.login.buildLogic.LoginInjector
import org.meetmap.view.points.PointsActivity


class LoginFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    // Loading
    private lateinit var llPbs: LinearLayout

    // choice/registr/auth layout/forms
    private lateinit var llChoiceForms: LinearLayout
    private lateinit var llRegistrationForms: LinearLayout
    private lateinit var llAuthorizationForms: LinearLayout

    // Registration
    private lateinit var ibRegistrationArrow: ImageButton
    private lateinit var etRegistrationEmail: EditText
    private lateinit var etRegistrationPassword: EditText
    private lateinit var btnClickRegistration: Button

    // Auth
    private lateinit var ibAuthorizationArrow: ImageButton
    private lateinit var etAuthorizationEmail: EditText
    private lateinit var etAuthorizationPassword: EditText
    private lateinit var btnClickAuthorization: Button

    // Choice registration/auth
    private lateinit var btnRegistration: Button
    private lateinit var btnAuthorization: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llPbs = view.findViewById(R.id.ll_pbs)

        llChoiceForms = view.findViewById(R.id.ll_choice_forms)
        llRegistrationForms = view.findViewById(R.id.ll_registration_forms)
        llAuthorizationForms = view.findViewById(R.id.ll_authorization_forms)

        btnRegistration = view.findViewById(R.id.btn_registration)
        btnAuthorization = view.findViewById(R.id.btn_authorization)

        ibRegistrationArrow = view.findViewById(R.id.ib_registration_arrow)
        etRegistrationEmail = view.findViewById(R.id.et_registration_email)
        etRegistrationPassword = view.findViewById(R.id.et_registration_password)
        btnClickRegistration = view.findViewById(R.id.btn_click_registration)

        ibAuthorizationArrow = view.findViewById(R.id.ib_authorization_arrow)
        etAuthorizationEmail = view.findViewById(R.id.et_authorization_email)
        etAuthorizationPassword = view.findViewById(R.id.et_authorization_password)
        btnClickAuthorization = view.findViewById(R.id.btn_click_authorization)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            LoginInjector(requireActivity().application).provideUserViewModelFactory()
        ).get(UserViewModel::class.java)

        setClickListeners()
        observeViewModel()

        viewModel.handleEvent(LoginEvent.OnStart)
    }

    private fun observeViewModel() {
        viewModel.stAnimation.observe(
            viewLifecycleOwner,
            {
                llPbs.visibility = View.VISIBLE
            }
        )
        viewModel.edAnimation.observe(
            viewLifecycleOwner,
            {
                llPbs.visibility = View.INVISIBLE
            }
        )
        viewModel.signIn.observe(
            viewLifecycleOwner,
            {
                startPointActivity()
            }
        )
        viewModel.errorTx.observe(
            viewLifecycleOwner,
            {
                showErrorState(it)
            }
        )
    }

    private fun setClickListeners() {
        btnRegistration.setOnClickListener {
            llChoiceForms.visibility = View.GONE
            llRegistrationForms.visibility = View.VISIBLE
        }

        btnAuthorization.setOnClickListener {
            llChoiceForms.visibility = View.GONE
            llAuthorizationForms.visibility = View.VISIBLE
        }

        ibRegistrationArrow.setOnClickListener {
            llChoiceForms.visibility = View.VISIBLE
            llRegistrationForms.visibility = View.GONE
        }

        ibAuthorizationArrow.setOnClickListener {
            llChoiceForms.visibility = View.VISIBLE
            llAuthorizationForms.visibility = View.GONE
        }

        btnClickRegistration.setOnClickListener {
            viewModel.handleEvent(LoginEvent.OnRegisterButtonClick(etRegistrationEmail.text.toString(),
                                                                etRegistrationPassword.text.toString())
                                 )
        }

        btnClickAuthorization.setOnClickListener {
            viewModel.handleEvent(LoginEvent.OnAuthButtonClick(etAuthorizationEmail.text.toString(),
                                                              etAuthorizationPassword.text.toString())
                                )
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startPointActivity() {
        val intent = Intent(activity,PointsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showErrorState(errorMessage: String) = view?.showSnackbar(errorMessage,Snackbar.LENGTH_SHORT)


}