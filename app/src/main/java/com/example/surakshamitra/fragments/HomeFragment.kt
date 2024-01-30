import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surakshamitra.MyAdapter
import com.example.surakshamitra.MyDataModel
import com.example.surakshamitra.R

class HomeFragment : Fragment() {

    private val PREFS_NAME = "MyPrefsFile"
    private val dataList = getDataList() // Implement this method to provide your data
    private lateinit var adapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dashboard_fragment, container, false)

        // Set the click listener for the button in the fragment
        view.findViewById<View>(R.id.buttonShowMenu).setOnClickListener {
            showPopupMenu(view)
        }

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapter = MyAdapter(requireContext(), dataList)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        // Restore the state of the switches when the fragment is created
        restoreSwitchStates(view)

        return view
    }

    // Function to display a toast message
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Function to be called when the button is clicked
    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view.findViewById<View>(R.id.buttonShowMenu))
        popup.menuInflater.inflate(R.menu.menu_main, popup.menu)

        // Show the popup menu
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.active_st, R.id.services -> {
                    val switchKey = item.itemId.toString()
                    val switchState = !item.isChecked
                    item.isChecked = switchState
                    showToast("${item.title} switched to $switchState")
                    saveSwitchState(switchKey, switchState)
                    true
                }
                R.id.action_logout -> {
                    showToast("Logout clicked")
                    // Add your logout logic here
                    true
                }
                else -> false
            }
        }

        // Restore the state of the switches when the menu is shown
        restoreSwitchStates(view, popup)

        popup.show()
    }

    // Save the state of the switch to SharedPreferences
    private fun saveSwitchState(switchKey: String, isChecked: Boolean) {
        val sharedPref =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(switchKey, isChecked)
            apply()
        }
    }

    // Restore the state of the switches from SharedPreferences
    private fun restoreSwitchStates(view: View, popup: PopupMenu? = null) {
        val sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Replace R.id.active_st, R.id.services with the actual IDs of your switches in the layout
        val switch1 = view.findViewById<Switch>(R.id.active_st)
        val switch2 = view.findViewById<Switch>(R.id.services)
        // Add more switches as needed

        // Check if switches are found before attempting to set their state
        switch1?.let {
            it.isChecked = sharedPref.getBoolean("switch1", false)
            popup?.menu?.findItem(R.id.active_st)?.isChecked = it.isChecked
        }

        switch2?.let {
            it.isChecked = sharedPref.getBoolean("switch2", false)
            popup?.menu?.findItem(R.id.services)?.isChecked = it.isChecked
        }
    }


    private fun getDataList(): List<MyDataModel> {
        // Populate your data list with image resources and text
        // Example:
        return listOf(
            MyDataModel(R.drawable.img_4, "Text 1"),
            MyDataModel(R.drawable.img_4, "Text 2"),
            MyDataModel(R.drawable.img_4, "Text 3"),
            MyDataModel(R.drawable.img_4, "Text 1"),
            MyDataModel(R.drawable.img_4, "Text 2"),
            MyDataModel(R.drawable.img_4, "Text 3"),
            MyDataModel(R.drawable.img_4, "Text 1"),
            MyDataModel(R.drawable.img_4, "Text 2"),
            MyDataModel(R.drawable.img_4, "Text 3"),
            MyDataModel(R.drawable.img_4, "Text 1"),
            MyDataModel(R.drawable.img_4, "Text 2"),
            MyDataModel(R.drawable.img_4, "Text 3"),
            MyDataModel(R.drawable.img, "Text 1"),
            MyDataModel(R.drawable.img, "Text 2"),
            MyDataModel(R.drawable.img, "Text 3"),
            MyDataModel(R.drawable.img, "Text 1"),
            MyDataModel(R.drawable.img, "Text 2"),
            MyDataModel(R.drawable.img, "Text 3"),
            MyDataModel(R.drawable.img, "Text 1"),
            MyDataModel(R.drawable.img, "Text 2"),
            MyDataModel(R.drawable.img, "Text 3"),
            // Add more items as needed
        )
    }

}
