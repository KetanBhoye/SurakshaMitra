package com.example.surakshamitra

import android.content.Intent
import com.google.firebase.auth.FirebaseAuthUserCollisionException

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.surakshamitra.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.surakshamitra.data.UserRegistrationData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RegisterUsers : AppCompatActivity() {
    private lateinit var usernameInp: EditText
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

    // for authentication
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_users)

        // Initialization of components
        usernameInp = findViewById(R.id.username_edit_text)
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

        // for exit
        registrationfromExitBtn.setOnClickListener {
            finish()
        }

        // for dropdown list
        val agencyTypes = arrayOf(
            "National Disaster Response Force (NDRF)",
            "State Police Departments",
            "Fire Services",
            "Emergency Medical Services (EMS)",
            "Indian Coast Guard",
            "National Security Guard (NSG)",
            "State Disaster Management Authorities (SDMA)",
            "Central Reserve Police Force (CRPF)",
            "Indian Red Cross Society",
            "Civil Defence",
            "State Emergency Response Centers (SERC)",
            "Railway Protection Force (RPF)",
            "National Institute of Disaster Management (NIDM)",
            "Disaster Relief Organizations (NGOs)",
            "State Health Departments"
            // Add more agencies as needed
        )

        // Add your agency types here
        agencyTypeInp = findViewById(R.id.agency_type)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, agencyTypes)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        agencyTypeInp.adapter = adapter

        // Set up the OnItemSelectedListener to handle the selected agency type
        agencyTypeInp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAgencyType = agencyTypes[position]
                val agencyTypeSpinner: Spinner = findViewById(R.id.agency_type)
                agencyTypeSpinner.prompt = "Selected Agency Type: $selectedAgencyType"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected (optional)
            }
        }

        // Create user in firebase and push values.
        agencyRegisterBtn.setOnClickListener {
             userRegistrationData = UserRegistrationData(
                username = usernameInp.text.toString(),
                agencyName = agencyNameInp.text.toString(),
                agencyType = selectedAgencyType,
                address = agencyAddressInp.text.toString(),
                phoneNumber = agencyPhoneNoInp.text.toString(),
                emailAddress = agencyEmail.text.toString(),
                totalMembers = agencyTotalMembersInp.text.toString(),
                password = agencypassword.text.toString(),
            )
            val email = agencyEmail.text.toString()  // Corrected line
            val paswd = agencycnfpassword.text.toString()

            signup(email, paswd)

        }
    }

    private fun createUser(userRegistrationData: UserRegistrationData) {
        if (agencypassword.text.toString() == agencycnfpassword.text.toString()) {
            val database = FirebaseDatabase.getInstance().reference
            val usersReference = database.child("Agencies")

            usersReference.child(userRegistrationData.username).setValue(userRegistrationData.toMap())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Data saved successfully
                        Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_SHORT).show()
                        // You may want to navigate to another activity or perform other actions here
                    } else {
                        // Failed to save data
                        Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }



                }
        } else {
            agencypassword.text.clear()
            agencycnfpassword.text.clear()
            Toast.makeText(applicationContext, "Check the password", Toast.LENGTH_LONG).show()
        }
    }

    private fun signup(email: String, password: String) {
        mAuth = Firebase.auth
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
                    createUser(userRegistrationData)
                    intent = Intent(applicationContext, LoginScreen::class.java)
                    startActivity(intent)

                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        // Email already exists, show a toast
                        Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show()
                    } else {
                        // Other registration failure
                        Toast.makeText(this, "User Creation failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


}