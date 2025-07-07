package com.luisptapia.pickupandroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.luisptapia.pickupandroid.R
import com.luisptapia.pickupandroid.application.RTPickupApp
import com.luisptapia.pickupandroid.data.PickupRepository
import com.luisptapia.pickupandroid.data.remote.RetrofitHelper
import com.luisptapia.pickupandroid.data.remote.model.ErrorDto
import com.luisptapia.pickupandroid.data.remote.model.UserLoginDto
import com.luisptapia.pickupandroid.databinding.FragmentLoginBinding
import com.luisptapia.pickupandroid.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.luisptapia.pickupandroid.extension.parseError
import com.luisptapia.pickupandroid.utils.DialogUtils
import com.luisptapia.pickupandroid.utils.NetworkConnectionLiveData
import kotlin.math.log


class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding!!

    private lateinit var repository: PickupRepository

    private var email =  ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as RTPickupApp

        repository = (requireActivity().application as RTPickupApp).repository

        binding.btLogin.setOnClickListener{

            Log.d(Constants.APP_NAME_LOGS,"------ Inicia Login ------")

            if(!validateFields()){
                return@setOnClickListener
            }

            binding.pbLoading.visibility = View.VISIBLE


            lifecycleScope.launch {
                try {

                    val user = repository.login( credentials =
                        UserLoginDto(
                            email,
                            password
                        )
                    )

                    app.sessionManager.saveToken(user.token,user.name)

                    app.isLoggedInLiveData.postValue(true)

                    Log.d(Constants.APP_NAME_LOGS,user.toString())

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fcFragmentContainer,
                            OrderListFragment()
                        )
                        .commit()

                }catch (e:HttpException){

                    val errorDto = e.response()?.errorBody().parseError( app.retrofit, ErrorDto::class.java)

                    Log.e(Constants.APP_NAME_LOGS, "HTTP ${e.code()} - ${errorDto}")

                    var messageError= ""

                    if( errorDto != null ){
                        messageError = errorDto.error
                    }else{
                        messageError = getString(R.string.loggin_error)
                    }

                    DialogUtils.showDialog(
                        requireContext(),
                        "Error",
                        messageError,
                        "Aceptar"
                    )

                }catch (e:Exception){

                    Log.d(Constants.APP_NAME_LOGS,e.message.toString())

                    Toast.makeText(requireContext(), getString(R.string.loggin_error), Toast.LENGTH_LONG).show()
                }finally {
                    binding.pbLoading.visibility = View.INVISIBLE
                }

            }

            /*
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fcFragmentContainer,
                    OrderListFragment.newInstance()
                )
                .addToBackStack(null)
                .commit()

             */
        }

    }

    private fun validateFields(): Boolean{
        email = binding.user.text.toString().trim()  //Elimina los espacios en blanco
        password = binding.password.text.toString().trim()

        //Verifica que el campo de correo no esté vacío
        if(email.isEmpty()){
            binding.user.error = getString(R.string.user_require)
            binding.user.requestFocus()
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.user.error = getString(R.string.email_format_error)
            binding.user.requestFocus()
            return false
        }

        //Verifica que el campo de la contraseña no esté vacía y tenga al menos 6 caracteres
        if(password.isEmpty()){
            binding.password.error = getString(R.string.password_require)
            binding.password.requestFocus()
            return false
        }

        return true
    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {

            }
    }
}