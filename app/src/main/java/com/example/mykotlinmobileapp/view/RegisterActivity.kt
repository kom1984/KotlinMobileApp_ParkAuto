package com.example.mykotlinmobileapp.view

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
import com.example.mykotlinmobileapp.activity.HomeActivity
import com.example.mykotlinmobileapp.activity.LoginActivity
import com.example.mykotlinmobileapp.data.RegisterBody
import com.example.mykotlinmobileapp.data.ValidateEmailBody
import com.example.mykotlinmobileapp.databinding.ActivityRegisterBinding

import com.example.mykotlinmobileapp.repository.AuthRepository
import com.example.mykotlinmobileapp.utils.APIService
import com.example.mykotlinmobileapp.view_model.RegisterActivityViewModel
import com.example.mykotlinmobileapp.view_model.RegisterActivityViewModelFactory
import java.lang.StringBuilder

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,View.OnKeyListener  {
    public lateinit var mBinding: ActivityRegisterBinding  //lateinit modifier keeps the property from being initialized at the time of its class objet construction.
    private lateinit var mViewModel : RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this)) //Creation du binding
        setContentView(mBinding.root)
        mBinding.textFirstname.onFocusChangeListener = this
        mBinding.textLastname.onFocusChangeListener = this
        mBinding.textEmail.onFocusChangeListener = this
        mBinding.textPassword.onFocusChangeListener = this
        mBinding.textConfirmPassword.onFocusChangeListener = this
        mBinding.btnRegister.setOnClickListener(this)

        mViewModel = ViewModelProvider(this,RegisterActivityViewModelFactory(AuthRepository(APIService.getService()),application)).get(RegisterActivityViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() {
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }

        mViewModel.getErrorMessage().observe(this){

            //FistName, LastName, email, password
            val formErrorKeys = arrayOf("fistName","lastName","email","password")
            val message = StringBuilder()

            it.map {
                    entry ->
                if(formErrorKeys.contains(entry.key)) {
                    when(entry.key){
                        "fistName" ->{
                            mBinding.textFirstnameTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                        "lastName" -> {
                            mBinding.textLastnameTil.apply {
                                isErrorEnabled = true
                                error = entry.value

                            }
                        }
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


    private fun validateFirstname():Boolean{
        var errorMessage : String? = null //Utilser var pour une variable dont la valeur peut changer
        val value :String = mBinding.textFirstname.text.toString() //Utilser val pour une variable dont la valeur change jamais

        if(value.isEmpty()){
            errorMessage = "Firstname is required"
        }
        //display messages in UI
        if(errorMessage != null){

            mBinding.textFirstnameTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null //return true bcuz error is initialized as true

    }

    private fun validateLastname():Boolean{
        var errorMessage : String? = null //Utilser var pour une variable dont la valeur peut changer
        val value :String = mBinding.textLastname.text.toString() //Utilser val pour une variable dont la valeur change jamais

        if(value.isEmpty()){
            errorMessage = "Lastname is required"
        }
        //display messages in UI
        if(errorMessage != null){

            mBinding.textLastnameTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null //return true bcuz error is initialized as true

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
    private fun validateConfirmPassword():Boolean{
        var errorMessage : String? = null //Utilser var pour une variable dont la valeur peut changer
        val value :String = mBinding.textConfirmPassword.text.toString() //Utilser val pour une variable dont la valeur change jamais

        if(value.isEmpty()){
            errorMessage = "Confirm Password is required"
        } else if(value.length < 8){
            errorMessage ="Confirm Password must be 8 characters long !"
        }
        //display messages in UI
        if(errorMessage != null){

            mBinding.textConfirmPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null //return true bcuz error is initialized as true
    }
    private fun validatePasswordAndConfirmPassword():Boolean{
        var errorMessage : String? = null //Utilser var pour une variable dont la valeur peut changer
        val password :String = mBinding.textPassword.text.toString()
        val confirmPassword :String = mBinding.textConfirmPassword.text.toString() //Utilser val pour une variable dont la valeur change jamais

        if(password != confirmPassword){
            errorMessage = "Password does not match with confirm password"
        }
        //display messages in UI
        if(errorMessage != null){

            mBinding.textConfirmPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null //return true bcuz error is initialized as true
    }

    override fun onClick(view: View?) {

        if(view!= null && view.id == R.id.btnRegister)
            onSubmit()
        val intent = Intent(this, LoginActivity::class.java )
        startActivity(intent)
    }

    private fun onSubmit() {
        //if(validate()){
            mViewModel.registerUser(RegisterBody(mBinding.textFirstname.text!!.toString(),mBinding.textLastname.text!!.toString(),mBinding.textEmail.text!!.toString(),mBinding.textPassword.text!!.toString() ))
       // }
    }


    private fun validate() : Boolean{
        var isValid = true
        if(!validateFirstname()) isValid  = false
        if(!validateLastname()) isValid  = false
        if(!validatePassword()) isValid  = false
        if(!validateConfirmPassword()) isValid  = false
        if(isValid && !validatePasswordAndConfirmPassword()) isValid  = false
        return isValid
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {

        if(view != null){
            when(view.id){
                R.id.textFirstname ->{
                    if(hasFocus){
                        if(mBinding.textFirstnameTil.isErrorEnabled){
                            mBinding.textFirstnameTil.isErrorEnabled = false
                        }

                    }else{
                        validateFirstname()
                    }
                }
                R.id.textLastname ->{

                    if(hasFocus){
                        if(mBinding.textLastnameTil.isErrorEnabled){
                            mBinding.textLastnameTil.isErrorEnabled = false
                        }

                    }else{
                        validateLastname()
                    }

                }
                R.id.textEmail ->{

                    if(hasFocus){
                        if(mBinding.textEmailTil.isErrorEnabled){
                            mBinding.textEmailTil.isErrorEnabled = false
                        }

                    }else{
                        if(validateEmail()){
                            //Faire validation si email unique
                            mViewModel.validateEmailAddress(ValidateEmailBody(mBinding.textEmail.text!!.toString()))
                        }
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
                R.id.textConfirmPassword ->{
                    if(hasFocus){
                        if(mBinding.textConfirmPasswordTil.isErrorEnabled){
                            mBinding.textConfirmPasswordTil.isErrorEnabled = false
                        }

                    }else{
                        validateConfirmPassword()
                    }

                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {

        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP){
            //TODO Registration
            onSubmit()
        }
        return false
    }
}

