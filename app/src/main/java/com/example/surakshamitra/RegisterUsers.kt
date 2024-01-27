package com.example.surakshamitra

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surakshamitra.data.UserRegistrationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

class RegisterUsers : AppCompatActivity() {

    private lateinit var usernameInp: String
    private lateinit var agencyNameInp: EditText
    private lateinit var agencyTypeInp: Spinner
    private lateinit var agencyAddressInp: EditText
    private lateinit var agencyPhoneNoInp: EditText
    private lateinit var agencyTotalMembersInp: EditText
    private lateinit var agencypassword: EditText
    private lateinit var agencycnfpassword: EditText
    private lateinit var agencyRegisterBtn: Button
    private lateinit var registrationfromExitBtn: Button
    private lateinit var selectedAgencyType: String
    private lateinit var agencyEmail: EditText
    private lateinit var userRegistrationData: UserRegistrationData
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_users)

        initializeViews()

        setupAgencyTypeSpinner()

        registrationfromExitBtn.setOnClickListener {
            finish()
        }

        agencyRegisterBtn.setOnClickListener {
            createUserAndSignUp()
        }
    }

    private fun initializeViews() {
        agencyNameInp = findViewById(R.id.agency_name_edit_text)
        agencyTypeInp = findViewById(R.id.agency_type)
        agencyAddressInp = findViewById(R.id.address_edit_text)
        agencyPhoneNoInp = findViewById(R.id.phone_number_edit_text)
        agencyTotalMembersInp = findViewById(R.id.total_members_edit_text)
        agencypassword = findViewById(R.id.password_edit_text)
        agencycnfpassword = findViewById(R.id.confirm_password_edit_text)
        agencyRegisterBtn = findViewById(R.id.register_button)
        registrationfromExitBtn = findViewById(R.id.exitbutton)
        agencyEmail = findViewById(R.id.email_id)
    }

    private fun setupAgencyTypeSpinner() {
        val agencyTypes = arrayOf(
            "National Disaster Response Force (NDRF)",
            "State Police Departments",
            "Fire Services",
            "Indian Search and Rescue",
            "Emergency Medical Services (EMS)",
            "Indian Coast Guard"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, agencyTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        agencyTypeInp.adapter = adapter

        agencyTypeInp.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAgencyType = agencyTypes[position]
                val agencyTypeSpinner: Spinner = findViewById(R.id.agency_type)
                agencyTypeSpinner.prompt = "Selected Agency Type: $selectedAgencyType"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected (optional)
            }
        }
    }

    private fun createUserAndSignUp() {
        val index = agencyEmail.text.indexOf('@')
        usernameInp = agencyEmail.text.substring(0, index)
        userRegistrationData = UserRegistrationData(
            username = usernameInp,
            agencyName = agencyNameInp.text.toString(),
            agencyType = selectedAgencyType,
            address = agencyAddressInp.text.toString(),
            phoneNumber = agencyPhoneNoInp.text.toString(),
            emailAddress = agencyEmail.text.toString(),
            totalMembers = agencyTotalMembersInp.text.toString(),
            password = agencypassword.text.toString()
        )

        val email = agencyEmail.text.toString()
        val password = agencycnfpassword.text.toString()

        signUpUser(email, password)
    }

    private fun signUpUser(email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
                    saveUserDataToDatabase()
                    navigateToLoginScreen()
                } else {
                    handleRegistrationError(task.exception)
                }
            }
    }

    private fun saveUserDataToDatabase() {
        if (agencypassword.text.toString() == agencycnfpassword.text.toString()) {
            val database = FirebaseDatabase.getInstance().reference
            val usersReference = database.child("Agencies")

            usersReference.child(userRegistrationData.username).setValue(userRegistrationData.toMap())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            clearPasswordFields()
            Toast.makeText(applicationContext, "Check the password", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToLoginScreen() {
        intent = Intent(applicationContext, LoginScreen::class.java)
        startActivity(intent)
    }

    private fun handleRegistrationError(exception: Exception?) {
        when {
            exception is FirebaseAuthUserCollisionException -> {
                Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "User Creation failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearPasswordFields() {
        agencypassword.text.clear()
        agencycnfpassword.text.clear()
    }
}
