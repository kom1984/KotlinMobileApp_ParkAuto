package com.example.mykotlinmobileapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinmobileapp.R
import com.example.mykotlinmobileapp.data.LoginBody
import com.example.mykotlinmobileapp.data.ValidateEmailBody
import com.example.mykotlinmobileapp.databinding.ActivityLoginBinding
import com.example.mykotlinmobileapp.databinding.ActivityRegisterBinding
import com.example.mykotlinmobileapp.repository.AuthRepository
import com.example.mykotlinmobileapp.utils.APIService
import com.example.mykotlinmobileapp.view.RegisterActivity
import com.example.mykotlinmobileapp.view_model.LoginActivityViewModel
import com.example.mykotlinmobileapp.view_model.LoginActivityViewModelFactory
import com.example.mykotlinmobileapp.view_model.RegisterActivityViewModel
import com.example.mykotlinmobileapp.view_model.RegisterActivityViewModelFactory

class LoginActivity : AppCompatActivity() , View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener{
    public lateinit var mBinding: ActivityLoginBinding  //lateinit modifier keeps the property from being initialized at the time of its class objet construction.
    private lateinit var mViewModel:LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this)) //Creation du binding

        setContentView(mBinding.root)
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.btnRegister.setOnClickListener(this)
        mBinding.textEmail.onFocusChangeListener = this
        mBinding.textPassword.onFocusChangeListener = this
        mBinding.textPassword.setOnKeyListener(this)

        mViewModel = ViewModelProvider(this,
            LoginActivityViewModelFactory(AuthRepository(APIService.getService()),application)
        ).get(LoginActivityViewModel::class.java)

        setupObservers()
    }
    private fun setupObservers() {
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }

        mViewModel.getErrorMessage().observe(this){

            //FistName, LastName, email, password
            val formErrorKeys = arrayOf("email","password")
            val message = StringBuilder()

            it.map {
                    entry ->
                if(formErrorKeys.contains(entry.key)) {
                    when(entry.key){
                        "email" -> {
                            mBinding.textEmailTil.apply {
                                isErrorEnabled = true
                                error = entry.value

                            }
                        }
                        "password" ->{
                            mBinding.textPasswordTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }

                        }

                    }
                } else{
                    message.append(entry.value).append("\n")
                }
                if(message.isEmpty()){
                    android.app.AlertDialog.Builder(this)
                        .setIcon(R.drawable.info_24)
                        .setTitle("INFORMATION")
                        .setMessage(message)
                        .setPositiveButton("OK"){
                                dialog, _ -> dialog!!.dismiss()
                        }
                        .show()

                }
            }
        }
        mViewModel.getUser().observe(this){
            if(it != null){
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun validateEmail():Boolean{
        var errorMessage : String? = null //Utilser var pour une variable dont la valeur peut changer
        val value :String = mBinding.textEmail.text.toString() //Utilser val pour une variable dont la valeur change jamais

        if(value.isEmpty()){
            errorMessage = "Email Address is required"

        }else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMessage ="Email Address is invalid"
        }
        //display messages in UI
        if(errorMessage != null){

            mBinding.textEmailTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null //return true bcuz error is initialized as true

    }

    private fun validatePassword():Boolean{
        var errorMessage : String? = null //Utilser var pour une variable dont la valeur peut changer
        val value :String = mBinding.textPassword.text.toString() //Utilser val pour une variable dont la valeur change jamais

        if(value.isEmpty()){
            errorMessage = "Password is required"
        } else if(value.length < 8){
            errorMessage ="Password must be 8 characters long !"
        }
        //display messages in UI
        if(errorMessage != null){

            mBinding.textPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null //return true bcuz error is initialized as true
    }

    override fun onClick(view: View?) {
        if(view!=null){
            when(view.id){
                R.id.btnLogin-> {
                    submitForm()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnRegister -> {
                    startActivity(Intent(this,RegisterActivity::class.java))
                }
            }

        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){

                R.id.textEmail ->{

                    if(hasFocus){
                        if(mBinding.textEmailTil.isErrorEnabled){
                            mBinding.textEmailTil.isErrorEnabled = false
                        }

                    }else{
                        validateEmail()
                    }

                }
                R.id.textPassword ->{

                    if(hasFocus){
                        if(mBinding.textPasswordTil.isErrorEnabled){
                            mBinding.textPasswordTil.isErrorEnabled = false
                        }

                    }else{
                        validatePassword()
                    }

                }

            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP){
            //TODO Login
            submitForm()
        }
        return false
    }

    private fun submitForm() {
        //if(validate()){

        mViewModel.loginUser(LoginBody(mBinding.textEmail.text!!.toString(),mBinding.textPassword.text!!.toString()))
        // }

    }
}