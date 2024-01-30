import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.surakshamitra.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PanicFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val markersMap: MutableMap<String, Marker?> = mutableMapOf()
    private lateinit var contactNearbyAgencies: Button

    private var Msg: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_map_nearby_agencies, container, false)
        val locateMeBtn: ImageView = view.findViewById(R.id.locateMeBtn)
        contactNearbyAgencies = view.findViewById(R.id.connect_button)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locateMeBtn.setOnClickListener {
            // Zoom to user's current location
            zoomToCurrentLocation()
        }

        contactNearbyAgencies.setOnClickListener {
            // Get the current user's location and send the message
            getCurrentLocationAndSendMessage()
        }

        // Load agency data and add markers to the map
        loadAgencyData()

        // Listen for live updates from the Firebase Realtime Database
        listenForUpdates()

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocationAndSendMessage() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val lastLocation: Location? = locationResult.lastLocation
                lastLocation?.let {
                    // Message to be sent
                    val mapsLink = "https://www.google.com/maps?q=${it.latitude},${it.longitude}"
                    val message = "Panic Alert!\nHelp needed at: $mapsLink\nPlease reach us ASAP to the given coordinates."

                    // Send the message to all agencies
                    sendMessageToAgencies(message)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun sendMessageToAgencies(message: String) {
        // Get reference to the "Agencies" node in Firebase Realtime Database
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Agencies")

        // Attach a ValueEventListener to retrieve data from the "Agencies" node
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Loop through each child node under "Agencies"
                for (agencySnapshot in dataSnapshot.children) {
                    val phoneNumber = agencySnapshot.child("phoneNumber").value.toString()

                    // Send the SMS with the current user's location
                    sendSMS(phoneNumber, message)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve data from Firebase",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        val sentIntent = Intent("SMS_SENT")
        val deliveredIntent = Intent("SMS_DELIVERED")

        val sentPI = PendingIntent.getBroadcast(requireContext(), 0, sentIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val deliveredPI = PendingIntent.getBroadcast(requireContext(), 0, deliveredIntent,
            PendingIntent.FLAG_IMMUTABLE)

        // Check for message length and split it into parts if necessary
        val parts = smsManager.divideMessage(message)
        val messageCount = parts.size

        // Send each part of the message
        for (i in 0 until messageCount) {
            // Send the SMS using the default SMS manager
            smsManager.sendTextMessage(phoneNumber, null, parts[i], sentPI, deliveredPI)
        }
    }

    private fun zoomToCurrentLocation() {
        // Request location updates
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                Msg = ""
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    private fun loadAgencyData() {
        val database = FirebaseDatabase.getInstance()
        val agenciesRef = database.getReference("Agencies")

        agenciesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (agencySnapshot in snapshot.children) {
                    updateMarker(agencySnapshot)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun listenForUpdates() {
        val database = FirebaseDatabase.getInstance()
        val agenciesRef = database.getReference("Agencies")

        agenciesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (agencySnapshot in snapshot.children) {
                    updateMarker(agencySnapshot)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateMarker(agencySnapshot: DataSnapshot) {
        val latitude = agencySnapshot.child("latitude").getValue(String::class.java)
        val longitude = agencySnapshot.child("longitude").getValue(String::class.java)
        val agencyName = agencySnapshot.child("agencyName").getValue(String::class.java)
        val agencyId = agencySnapshot.key

        if (latitude != null && longitude != null && agencyId != null) {
            val agencyLatLng = LatLng(latitude.toDouble(), longitude.toDouble())
            if (markersMap.containsKey(agencyId)) {
                // If marker already exists, update its position
                markersMap[agencyId]?.position = agencyLatLng
            } else {
                // If marker doesn't exist, create a new one
                val marker = mMap.addMarker(MarkerOptions().position(agencyLatLng).title(agencyName))
                markersMap[agencyId] = marker
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
