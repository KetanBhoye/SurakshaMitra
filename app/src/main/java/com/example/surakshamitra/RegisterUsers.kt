package com.example.surakshamitra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar

class RegisterUsers : AppCompatActivity() {
    private lateinit var usernameInp: EditText
    private  lateinit var agencyNameInp: EditText
    private lateinit var  agencyTypeInp: Spinner
    private lateinit var  agencyAddressInp: EditText
    private lateinit var  agencyPhoneNoInp: EditText
    private lateinit var  agencyTotalMembersInp: EditText
    private lateinit var agencypassword: EditText
    private lateinit var agencycnfpassword: EditText
    private lateinit var agencyRegisterBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_users)
        val actionBar = supportActionBar
        actionBar?.hide()


        //Initialization of components

        usernameInp = findViewById(R.id.username_edit_text)
        agencyNameInp = findViewById(R.id.agency_name_edit_text)
        agencyTypeInp = findViewById(R.id.agency_type)
        agencyAddressInp =findViewById(R.id.address_edit_text)
        agencyPhoneNoInp = findViewById(R.id.phone_number_edit_text)
        agencyTotalMembersInp = findViewById(R.id.total_members_edit_text)
        agencypassword = findViewById(R.id.password_edit_text)
        agencycnfpassword = findViewById(R.id.confirm_password_edit_text)
        agencyRegisterBtn = findViewById(R.id.register_button)



        //for dropdownlist

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
                val selectedAgencyType = agencyTypes[position]
                val agencyTypeSpinner: Spinner = findViewById(R.id.agency_type)
                agencyTypeSpinner.prompt = "Selected Agency Type: $selectedAgencyType"

                Toast.makeText(applicationContext, "Selected Agency Type: $selectedAgencyType", Toast.LENGTH_LONG).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected (optional)
            }
        }






    }
}

data class UserRegistraionData(
    val username: String,
    val agencyName: String,
    val agencyType: String,
    val address: String,
    val phoneNumber: String,
    val totalMembers: Int,
    val password: String,
    val confirmPassword: String
)
