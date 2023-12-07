package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.databinding.FragmentAuthorizationBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1

    private var isSignUpShown = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = getString(R.string.have_account)
        val text1 = getString(R.string.sign_in)
        binding.haveAccTextView.text = Html.fromHtml("$text <b>${text1.toLowerCase().capitalize()}</b>" , Html.FROM_HTML_MODE_LEGACY)

        binding.haveAccTextView.setOnClickListener {
            if (isSignUpShown)
                toSignIn()
            else toSignUp()
            isSignUpShown = !isSignUpShown
        }
        binding.signupButton.setOnClickListener {
            if (isSignUpShown) {
                viewModel.createUser(
                    UserEntity(
                        username = binding.usernameTextInput.text.toString(),
                        email = binding.emailTextInput.text.toString(),
                        password = binding.passwordTextInput.text.toString()
                    )
                )
                recreateActivity()
            } else {
                viewModel.authorizeUser(binding.usernameTextInput.text.toString(), binding.passwordTextInput.text.toString())


                viewModel.authorizeUserLiveData.observe(viewLifecycleOwner) { isAuthorized ->
                    Log.d(">>> isAuthorized", isAuthorized.toString())
                    if (isAuthorized) {
                        viewModel.getUserByUsername(binding.usernameTextInput.text.toString())
                        viewModel.userData.observe(viewLifecycleOwner) {user ->
                            if (user != null) {
                                viewModel.setUserId(user.id)
                                recreateActivity()
                            }
                        }
                    }
                }
            }



        }


    }

    private fun toSignUp() {
        binding.apply {
            signupTextView.text = getString(R.string.sign_up)
            signupButton.text = getString(R.string.sign_up)
            val text = getString(R.string.have_account)
            val text1 = getString(R.string.sign_in)
            haveAccTextView.text = Html.fromHtml("$text <b>${text1.toLowerCase().capitalize()}</b>" , Html.FROM_HTML_MODE_LEGACY)
            textBox2.visibility = View.VISIBLE
        }
    }

    private fun toSignIn() {
        binding.apply {
            signupTextView.text = getString(R.string.sign_in)
            signupButton.text = getString(R.string.sign_in)
            val text = getString(R.string.dont_have_acc)
            val text1 = getString(R.string.sign_up)
            haveAccTextView.text = Html.fromHtml("$text <b>${text1.toLowerCase().capitalize()}</b>" , Html.FROM_HTML_MODE_LEGACY)


            textBox2.visibility = View.GONE
        }
    }

    private fun recreateActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}