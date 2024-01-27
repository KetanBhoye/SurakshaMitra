package com.example.surakshamitra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.telephony.CellSignalStrength
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
class AgencyInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agency_info)


        //Initialization


    }





}
class CustomEditTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val editText: EditText
    private val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_edit_text_view, this, true)

        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.textView)
    }

    fun setHintText(hintText: String) {
        editText.hint = hintText
        textView.text = hintText
    }



}