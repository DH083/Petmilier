package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.maps.data.AnimalHospital
import com.example.myapplication.databinding.ActivityMapsBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// 동물 API 928bec75e0a24715bb1d3919474a7a2a 경기
// API2 54486f72466262613636415555474a 서울

@Suppress("UNREACHABLE_CODE")
class MapActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    //객체 선언
    //var mapFragment: SupportMapFragment? = null
    // var map: GoogleMap? = null
    //lateinit var btnLocation: Button
    // var btnKor2Loc: Button? = null
    // var editText: EditText? = null


    lateinit var providerClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    var googleMap: GoogleMap? = null

// 온크레이트 ---~~~!!!!!!!!!!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if (it.all { permission -> permission.value == true }) {
                apiClient.connect()
            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment)!!.getMapAsync(
            this
        )

        providerClient = LocationServices.getFusedLocationProviderClient(this)
        apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            )
        } else {
            apiClient.connect()
        }

        //현재 위치 이동 버튼 ----------------------------------------------------------------

        binding.button1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                providerClient.lastLocation.addOnSuccessListener(
                    this@MapActivity,
                    object : OnSuccessListener<Location> {
                        override fun onSuccess(p0: Location?) {
                            p0?.let {
                                val latitude = p0.latitude
                                val longitude = p0.longitude
                                Log.d("KD", "$latitude, $longitude")
                                moveMap(latitude, longitude)
                            }
                        }
                    }
                )
                googleMap?.clear()
                apiClient.disconnect()
            }
        }



    }


    private fun createDrawableFromView(context: Context, view: View): Bitmap {
        val displayMetrics = DisplayMetrics()
        (context as Activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        view.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap: Bitmap = Bitmap.createBitmap(
            view.getMeasuredWidth(),
            view.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


//지도 움직이기

    private fun moveMap(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val position: CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()

        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))


        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        markerOptions.position(latLng)
        markerOptions.title("현재 위치")

        googleMap?.addMarker(markerOptions)

        //서울임의로 표시하기-------------------------------------------------------------

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                providerClient.lastLocation.addOnSuccessListener(
                    this@MapActivity,
                    object : OnSuccessListener<Location> {
                        override fun onSuccess(p0: Location?) {
                            p0?.let {
                                val latitude = 37.6160203
                                val longitude = 127.020105
                                moveMap(latitude, longitude)
                            }
                        }
                    }
                )
                googleMap?.clear()
                apiClient.disconnect()

                val markerOptions_r = MarkerOptions()

                markerOptions_r.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                markerOptions_r.position(LatLng(37.6160203,127.020105))
                markerOptions_r.title("현재 위치")
                moveMap(latitude, longitude)

                googleMap?.clear()
                googleMap?.addMarker(markerOptions_r)
            }
        }

        //---------------------------------------------------------------------------------

        val marker1 = MarkerOptions()
        marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker1.position(LatLng(37.4945312,126.908080))
        marker1.title("동물병원예지").snippet("02-2135-7228")
        googleMap?.addMarker(marker1)

        val marker2 = MarkerOptions()
        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker2.position(LatLng(37.6129704,127.076870))
        marker2.title("치료해 주오").snippet(" 02-949-7577")
        googleMap?.addMarker(marker2)

        val marker3 = MarkerOptions()
        marker3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker3.position(LatLng(37.5835118,126.949281))
        marker3.title("빛가온동물병원").snippet("02-736-5834")
        googleMap?.addMarker(marker3)

        val marker4 = MarkerOptions()
        marker4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker4.position(LatLng(37.5576926,126.922488))
        marker4.title("워너비 동물병원").snippet(" 02-322-1275")
        googleMap?.addMarker(marker4)

        val marker5 = MarkerOptions()
        marker5.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker5.position(LatLng(37.4729971,127.049846))
        marker5.title("위드펫동물병원").snippet("02-529-7577")
        googleMap?.addMarker(marker5)

        val marker6 = MarkerOptions()
        marker6.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker6.position(LatLng(37.4932087,126.856404))
        marker6.title("모두동물병원").snippet("  02-2060-7975")
        googleMap?.addMarker(marker6)

        val marker7 = MarkerOptions()
        marker7.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker7.position(LatLng(37.5032456,127.113413))
        marker7.title("한양동물병원").snippet("02-420-9993")
        googleMap?.addMarker(marker7)

        val marker8 = MarkerOptions()
        marker8.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker8.position(LatLng(37.4536152,126.906322))
        marker8.title("허브동물병원").snippet(" 02-867-7776")
        googleMap?.addMarker(marker8)

        val marker9 = MarkerOptions()
        marker9.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker9.position(LatLng(37.5356907,127.138862))
        marker9.title("펫트리동물의료센터").snippet("02-6949-5775")
        googleMap?.addMarker(marker9)

        val marker10 = MarkerOptions()
        marker10.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker10.position(LatLng(37.4924174,127.039999))
        marker10.title("예은동물병원").snippet("02-529-5575")
        googleMap?.addMarker(marker10)

        val marker11 = MarkerOptions()
        marker11.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker11.position(LatLng(37.5605107,127.019084))
        marker11.title("신당 좋아요 동물병원").snippet("02-2233-6675")
        googleMap?.addMarker(marker11)

        val marker12 = MarkerOptions()
        marker12.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker12.position(LatLng(37.6584899,127.032582))
        marker12.title("아이조은동물병원").snippet("02-900-7555")
        googleMap?.addMarker(marker12)

        val marker13 = MarkerOptions()
        marker13.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker13.position(LatLng(37.5496104,126.863368))
        marker13.title("로운동물의료센터").snippet("02-6953-7576")
        googleMap?.addMarker(marker13)

        val marker14 = MarkerOptions()
        marker14.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker14.position(LatLng(37.5194261,127.049005))
        marker14.title("VIP동물의료센터 청담점").snippet("02-511-7522")
        googleMap?.addMarker(marker14)

        val marker15 = MarkerOptions()
        marker15.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker15.position(LatLng(37.5092388,127.111850))
        marker15.title("송파원동물병원").snippet("02-419-7571")
        googleMap?.addMarker(marker15)

        val marker16 = MarkerOptions()
        marker16.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker16.position(LatLng(37.5223215,126.891145))
        marker16.title("24시 수 동물메디컬센터").snippet("02-2676-7582")
        googleMap?.addMarker(marker16)

        val marker17 = MarkerOptions()
        marker17.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker17.position(LatLng(37.5546282,127.154605))
        marker17.title("아이안동물병원").snippet("02-441-2202")
        googleMap?.addMarker(marker17)

        val marker18 = MarkerOptions()
        marker18.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker18.position(LatLng(37.5799626,127.021975))
        marker18.title("포포동물병원").snippet("02-922-7555")
        googleMap?.addMarker(marker18)

        val marker19 = MarkerOptions()
        marker19.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker19.position(LatLng(37.4854578,126.985191))
        marker19.title("심바동물병원").snippet("02-523-0275")
        googleMap?.addMarker(marker19)

        val marker20 = MarkerOptions()
        marker20.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker20.position(LatLng(37.5570788,126.954279))
        marker20.title("소망동물병원").snippet("02-719-7052")
        googleMap?.addMarker(marker20)

        val marker21 = MarkerOptions()
        marker21.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker21.position(LatLng(37.5320734,126.905024))
        marker21.title("이든동물의료센터").snippet("02-2635-2475")
        googleMap?.addMarker(marker21)

        val marker22 = MarkerOptions()
        marker22.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker22.position(LatLng(37.5207116,127.029957))
        marker22.title("24시 스마트동물병원(신사본원)").snippet("02-549-0275")
        googleMap?.addMarker(marker22)

        val marker23 = MarkerOptions()
        marker23.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker23.position(LatLng(37.6216758,127.073821))
        marker23.title("메인동물메디컬센터").snippet("02-549-0275")
        googleMap?.addMarker(marker23)

        val marker24 = MarkerOptions()
        marker24.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker24.position(LatLng(37.6216758,127.073821))
        marker24.title("경인동물의료센터").snippet("02-571-8275")
        googleMap?.addMarker(marker24)

        val marker25 = MarkerOptions()
        marker25.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker25.position(LatLng(37.5997657,126.918106))
        marker25.title("24시스마트동물메디컬센터").snippet("02-387-7582")
        googleMap?.addMarker(marker25)

        val marker26 = MarkerOptions()
        marker26.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker26.position(LatLng(37.5430157,126.937858))
        marker26.title("닥터 호오 동물병원").snippet("02-715-0075")
        googleMap?.addMarker(marker26)

        val marker27 = MarkerOptions()
        marker27.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker27.position(LatLng(37.5983760,127.034335))
        marker27.title("미소동물병원").snippet("02-929-1212")
        googleMap?.addMarker(marker27)

        val marker28 = MarkerOptions()
        marker28.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker28.position(LatLng(37.5429705,126.842175))
        marker28.title("24시연동물의료센터").snippet("")
        googleMap?.addMarker(marker28)

        val marker29 = MarkerOptions()
        marker29.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker29.position(LatLng(37.5805219,127.017198))
        marker29.title("폴라동물병원").snippet("")
        googleMap?.addMarker(marker29)

        val marker30 = MarkerOptions()
        marker30.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker30.position(LatLng(37.5382977,126.967620))
        marker30.title("좋아서하는 동물병원").snippet("02-703-0075")
        googleMap?.addMarker(marker30)

        val marker31 = MarkerOptions()
        marker31.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker31.position(LatLng(37.6161846,127.092878))
        marker31.title("쥬라기동물병원").snippet("02-3421-7588")
        googleMap?.addMarker(marker31)

        val marker32 = MarkerOptions()
        marker32.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker32.position(LatLng(37.5223249,126.860466))
        marker32.title("우리들동물메디컬센터").snippet("02-2642-7588")
        googleMap?.addMarker(marker32)

        val marker33 = MarkerOptions()
        marker33.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker33.position(LatLng(37.5446866,126.946027))
        marker33.title("굿모닝 동물병원").snippet("02-711-3375")
        googleMap?.addMarker(marker33)

        val marker34 = MarkerOptions()
        marker34.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker34.position(LatLng(37.5425241,127.073699))
        marker34.title("건국대학교 부속 동물병원").snippet("02-450-0472")
        googleMap?.addMarker(marker34)

        val marker35 = MarkerOptions()
        marker35.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker35.position(LatLng(37.5447041,127.087664))
        marker35.title("연두동물병원").snippet("02-455-0975")
        googleMap?.addMarker(marker35)

        val marker36 = MarkerOptions()
        marker36.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker36.position(LatLng(37.5571359,127.040089))
        marker36.title("바우라움동물병원").snippet("02-6454-6182")
        googleMap?.addMarker(marker36)

        val marker37 = MarkerOptions()
        marker37.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker37.position(LatLng(37.5665735,127.024066))
        marker37.title("베이지동물병원").snippet("02-2295-9888")
        googleMap?.addMarker(marker37)

        val marker38 = MarkerOptions()
        marker38.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker38.position(LatLng(37.6153821,127.066634))
        marker38.title("우솔종합동물병원").snippet("02-973-7588")
        googleMap?.addMarker(marker38)

        val marker39 = MarkerOptions()
        marker39.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker39.position(LatLng(37.5569489,126.852161))
        marker39.title("강서YD동물의료센터").snippet("02-518-7500")
        googleMap?.addMarker(marker39)

        val marker40 = MarkerOptions()
        marker40.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker40.position(LatLng(37.5356712,127.081884))
        marker40.title("우주동물병원").snippet("02-2039-9930")
        googleMap?.addMarker(marker40)

        val marker41 = MarkerOptions()
        marker41.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker41.position(LatLng(37.5328883,127.146664))
        marker41.title("힐동물병원").snippet("")
        googleMap?.addMarker(marker41)

        val marker42 = MarkerOptions()
        marker42.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker42.position(LatLng(37.4811705,126.909555))
        marker42.title("러브펫종합동물병원").snippet("02-837-8875")
        googleMap?.addMarker(marker42)

        val marker43 = MarkerOptions()
        marker43.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker43.position(LatLng(37.5778655,126.888878))
        marker43.title("아프지말게 동물병원").snippet("02-307-3355")
        googleMap?.addMarker(marker43)

        val marker44 = MarkerOptions()
        marker44.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker44.position(LatLng(37.5085853,127.133961))
        marker44.title("엘 동물병원").snippet("02-2088-2878")
        googleMap?.addMarker(marker44)

        val marker45 = MarkerOptions()
        marker45.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker45.position(LatLng(37.4747188,127.104802))
        marker45.title("힐스타동물병원").snippet("02-445-5022")
        googleMap?.addMarker(marker45)

        val marker46 = MarkerOptions()
        marker46.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker46.position(LatLng(37.5115534,127.078996))
        marker46.title("24시잠실ON동물의료센터").snippet("02-418-0724")
        googleMap?.addMarker(marker46)

        val marker47 = MarkerOptions()
        marker47.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker47.position(LatLng(37.5164841,127.059467))
        marker47.title("중앙동물메디컬센터").snippet("02-512-3331")
        googleMap?.addMarker(marker47)

        val marker48 = MarkerOptions()
        marker48.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker48.position(LatLng(37.5455889,126.948753))
        marker48.title("힐링 24시 동물병원").snippet("02-713-8275")
        googleMap?.addMarker(marker48)

        val marker49 = MarkerOptions()
        marker49.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker49.position(LatLng(37.5759789,126.894722))
        marker49.title("상암동물병원").snippet("02-375-7222")
        googleMap?.addMarker(marker49)

        val marker50 = MarkerOptions()
        marker50.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker50.position(LatLng(37.5579935,126.928067))
        marker50.title("마리동물병원").snippet("02-323-7582")
        googleMap?.addMarker(marker50)

        val marker51 = MarkerOptions()
        marker51.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker51.position(LatLng(37.5586606,127.033672))
        marker51.title("24시센트럴동물메디컬센터").snippet("02-3395-7975")
        googleMap?.addMarker(marker51)

        val marker52 = MarkerOptions()
        marker52.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker52.position(LatLng(37.5480762,126.918367))
        marker52.title("홍익동물종합병원").snippet("02-325-2026")
        googleMap?.addMarker(marker52)

        val marker53 = MarkerOptions()
        marker53.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker53.position(LatLng(37.5717883,127.057076))
        marker53.title("그랜드동물병원").snippet("02-2214-6129")
        googleMap?.addMarker(marker53)

        val marker54 = MarkerOptions()
        marker54.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker54.position(LatLng(37.4561053,126.899515))
        marker54.title("금천 24시 K동물의료센터").snippet("02-808-2475")
        googleMap?.addMarker(marker54)

        val marker55 = MarkerOptions()
        marker55.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker55.position(LatLng(37.5922893,127.013408))
        marker55.title("VIP 동물의료센터").snippet("02-953-0075")
        googleMap?.addMarker(marker55)

        val marker56 = MarkerOptions()
        marker56.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker56.position(LatLng(37.4854378,127.142422))
        marker56.title("24시 스탠다드 동물의료센터").snippet("02-448-6400")
        googleMap?.addMarker(marker56)

        val marker57 = MarkerOptions()
        marker57.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker57.position(LatLng(37.5285159,127.125126))
        marker57.title("더파크동물의료센터").snippet("02-6949-2475")
        googleMap?.addMarker(marker57)

        val marker58 = MarkerOptions()
        marker58.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker58.position(LatLng(37.5658639,127.003329))
        marker58.title("헬릭스동물암센터").snippet("02-6956-7502")
        googleMap?.addMarker(marker58)

        val marker59 = MarkerOptions()
        marker59.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker59.position(LatLng(37.5497989,127.009248))
        marker59.title("남산동물병원").snippet("02-2256-8275")
        googleMap?.addMarker(marker59)

        val marker60 = MarkerOptions()
        marker60.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker60.position(LatLng(37.5536371,127.009633))
        marker60.title("미래지동물의료센터").snippet("02-2233-7574")
        googleMap?.addMarker(marker60)

        val marker61 = MarkerOptions()
        marker61.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker61.position(LatLng(37.5620436,126.996867))
        marker61.title("대한동물병원").snippet("02-2263-6134")
        googleMap?.addMarker(marker61)

        val marker62 = MarkerOptions()
        marker62.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker62.position(LatLng(37.6561756,127.027697))
        marker62.title("사랑해요동물병원").snippet("02-907-7512")
        googleMap?.addMarker(marker62)

        val marker63 = MarkerOptions()
        marker63.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker63.position(LatLng(37.4756483,126.958751))
        marker63.title("닥터펫보리 특수클리닉").snippet("")
        googleMap?.addMarker(marker63)

        val marker64 = MarkerOptions()
        marker64.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker64.position(LatLng(37.4958435,127.032209))
        marker64.title("동물치과병원메이").snippet("02-797-4408")
        googleMap?.addMarker(marker64)

        val marker65 = MarkerOptions()
        marker65.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker65.position(LatLng(37.5623590,126.999979))
        marker65.title("황동물병원").snippet("02-2278-0075")
        googleMap?.addMarker(marker65)

        val marker66 = MarkerOptions()
        marker66.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker66.position(LatLng(37.5143545,127.092980))
        marker66.title("잠실동물병원").snippet("02-6953-7587")
        googleMap?.addMarker(marker66)

        val marker67 = MarkerOptions()
        marker67.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker67.position(LatLng(37.5510416,126.934366))
        marker67.title("신촌 숲 동물병원").snippet("0507-1350-7229")
        googleMap?.addMarker(marker67)

        val marker68 = MarkerOptions()
        marker68.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker68.position(LatLng(37.5701125,127.056893))
        marker68.title("동대문 루시드 동물메디컬센터").snippet("02-6217-0202")
        googleMap?.addMarker(marker68)


        val marker69 = MarkerOptions()
        marker69.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker69.position(LatLng(37.5107741,127.027838))
        marker69.title("순수동물병원").snippet("02-515-8575")
        googleMap?.addMarker(marker69)

        val marker70 = MarkerOptions()
        marker70.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker70.position(LatLng(37.4808981,126.981247))
        marker70.title("다나동물병원").snippet("02-3471-0375")
        googleMap?.addMarker(marker70)

        val marker71 = MarkerOptions()
        marker71.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker71.position(LatLng(37.5251796,126.969085))
        marker71.title("워너비동물병원 용산점").snippet("02-797-1275")
        googleMap?.addMarker(marker71)

        val marker72 = MarkerOptions()
        marker72.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker72.position(LatLng(37.5616111,126.996833))
        marker72.title("윤신근박사동물병원").snippet("02-2274-8558")
        googleMap?.addMarker(marker72)

        val marker73 = MarkerOptions()
        marker73.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker73.position(LatLng(37.6060320,126.960873))
        marker73.title("월드펫동물병원").snippet("02-379-7575")
        googleMap?.addMarker(marker73)

        val marker74 = MarkerOptions()
        marker74.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker74.position(LatLng(37.5037446,127.087855))
        marker74.title("리베동물메디컬센터").snippet("02-6953-7502")
        googleMap?.addMarker(marker74)

        val marker75 = MarkerOptions()
        marker75.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker75.position(LatLng(37.5844381,126.969954))
        marker75.title("선동물병원").snippet("02-733-6030")
        googleMap?.addMarker(marker75)

        val marker76 = MarkerOptions()
        marker76.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker76.position(LatLng(37.5748820,127.015488))
        marker76.title("해동물병원").snippet("02-744-9098")
        googleMap?.addMarker(marker76)

        val marker77 = MarkerOptions()
        marker77.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker77.position(LatLng(37.5988689,126.960232))
        marker77.title("북악동물병원").snippet("02-391-4575")
        googleMap?.addMarker(marker77)

        val marker78 = MarkerOptions()
        marker78.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker78.position(LatLng(37.5992649,126.958963))
        marker78.title("올리브동물병원").snippet("02-391-7502")
        googleMap?.addMarker(marker78)

        val marker79 = MarkerOptions()
        marker79.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker79.position(LatLng(37.5786931,127.015624))
        marker79.title("우리동물병원").snippet("02-3676-1190")
        googleMap?.addMarker(marker79)

        val marker80 = MarkerOptions()
        marker80.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker80.position(LatLng(37.5757799,126.971044))
        marker80.title("누리봄동물병원").snippet("02-735-7530")
        googleMap?.addMarker(marker80)

        val marker81 = MarkerOptions()
        marker81.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker81.position(LatLng(37.5790235,126.971145))
        marker81.title("광화문동물병원").snippet("02-722-8275")
        googleMap?.addMarker(marker81)

        val marker82 = MarkerOptions()
        marker82.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker82.position(LatLng(37.5709030,126.962273))
        marker82.title("경희궁 바른 동물병원").snippet("")
        googleMap?.addMarker(marker82)

        val marker83 = MarkerOptions()
        marker83.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker83.position(LatLng(37.5846315,127.000171))
        marker83.title("대학로동물병원").snippet("02-3672-8441")
        googleMap?.addMarker(marker83)

        val marker84 = MarkerOptions()
        marker84.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker84.position(LatLng(37.5542947,127.011466))
        marker84.title("웰펫동물병원").snippet("02-2256-7582")
        googleMap?.addMarker(marker84)

        val marker85 = MarkerOptions()
        marker85.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker85.position(LatLng(37.5820310,126.926685))
        marker85.title("백년동물병원").snippet("02-373-2923")
        googleMap?.addMarker(marker85)

        val marker86 = MarkerOptions()
        marker86.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker86.position(LatLng(37.5660693,127.022130))
        marker86.title("바른동물병원").snippet("02-2232-7975")
        googleMap?.addMarker(marker86)

        val marker87 = MarkerOptions()
        marker87.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker87.position(LatLng(37.5550827,126.965745))
        marker87.title("백슬동물병원").snippet("02-393-7588")
        googleMap?.addMarker(marker87)

        val marker88 = MarkerOptions()
        marker88.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker88.position(LatLng(37.5520605,127.008014))
        marker88.title("애니컴 메디컬센터").snippet("02-2232-9702")
        googleMap?.addMarker(marker88)

        val marker89 = MarkerOptions()
        marker89.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker89.position(LatLng(37.5634844,127.015950))
        marker89.title("메종동물병원").snippet("02-2038-7555")
        googleMap?.addMarker(marker89)

        val marker90 = MarkerOptions()
        marker90.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker90.position(LatLng(37.5709618,127.021407))
        marker90.title("웰니스클리닉(청계천점)").snippet("02-2234-7585")
        googleMap?.addMarker(marker90)

        val marker91 = MarkerOptions()
        marker91.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker91.position(LatLng(37.5658639,127.003329))
        marker91.title("헬릭스동물심장수술센터").snippet("02-6965-7502")
        googleMap?.addMarker(marker91)

        val marker92 = MarkerOptions()
        marker92.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker92.position(LatLng(37.5592771,127.012825))
        marker92.title("바우미우동물병원").snippet("02-2237-3366")
        googleMap?.addMarker(marker92)

        val marker93 = MarkerOptions()
        marker93.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker93.position(LatLng(37.5066799,127.040843))
        marker93.title("서울동물심장병원").snippet("02-6203-7588")
        googleMap?.addMarker(marker93)

        val marker94 = MarkerOptions()
        marker94.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker94.position(LatLng(37.5066799,127.040843))
        marker94.title("서울동물영상종양센터").snippet("02-6956-7502")
        googleMap?.addMarker(marker94)

        val marker95 = MarkerOptions()
        marker95.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker95.position(LatLng(37.5642308,127.024869))
        marker95.title("24시SD동물영상의료센터").snippet("02-2039-0303")
        googleMap?.addMarker(marker95)

        val marker96 = MarkerOptions()
        marker96.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker96.position(LatLng(37.5690964,127.023240))
        marker96.title("웰튼동물의료센터").snippet("02-2253-2233")
        googleMap?.addMarker(marker96)

        val marker97 = MarkerOptions()
        marker97.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker97.position(LatLng(37.4857735,127.015426))
        marker97.title("시니어 동물병원").snippet("02-521-0525")
        googleMap?.addMarker(marker97)

        val marker98 = MarkerOptions()
        marker98.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker98.position(LatLng(37.5382977,126.967620))
        marker98.title("엉클장동물병원").snippet("02-713-0053")
        googleMap?.addMarker(marker98)

        val marker99 = MarkerOptions()
        marker99.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker99.position(LatLng(37.5310944,126.995805))
        marker99.title("청화종합동물병원").snippet("02-792-7602")
        googleMap?.addMarker(marker99)

        val marker100 = MarkerOptions()
        marker100.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker100.position(LatLng(37.5334912,127.001665))
        marker100.title("펫토이동물병원").snippet("02-795-2184")
        googleMap?.addMarker(marker100)

        val marker101 = MarkerOptions()
        marker101.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker101.position(LatLng(37.5504815,126.977086))
        marker101.title("남산동물병원").snippet("02-778-7582")
        googleMap?.addMarker(marker101)

        val marker102 = MarkerOptions()
        marker102.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker102.position(LatLng(37.5393050,126.961871))
        marker102.title("효창동물병원").snippet("02-711-4527")
        googleMap?.addMarker(marker102)

        val marker103 = MarkerOptions()
        marker103.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker103.position(LatLng(37.5449656,126.969472))
        marker103.title("원스동물병원").snippet("02-719-8275")
        googleMap?.addMarker(marker103)

        val marker104 = MarkerOptions()
        marker104.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker104.position(LatLng(37.5356536,126.998803))
        marker104.title("더벳한남동물병원").snippet("02-6956-2280")
        googleMap?.addMarker(marker104)

        val marker105 = MarkerOptions()
        marker105.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker105.position(LatLng(37.5283555,126.999188))
        marker105.title("21세기동물병원").snippet("02-749-6750")
        googleMap?.addMarker(marker105)

        val marker106 = MarkerOptions()
        marker106.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker106.position(LatLng(37.5324099,127.005252))
        marker106.title("한남동물병원").snippet("02-793-9230")
        googleMap?.addMarker(marker106)

        val marker107 = MarkerOptions()
        marker107.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker107.position(LatLng(37.5356109,126.961737))
        marker107.title("열린동물종합병원").snippet("02-3273-0075")
        googleMap?.addMarker(marker107)

        val marker108 = MarkerOptions()
        marker108.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker108.position(LatLng(37.5395841,126.961068))
        marker108.title("펫동물병원").snippet("02-3273-0075")
        googleMap?.addMarker(marker108)

        val marker109 = MarkerOptions()
        marker109.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker109.position(LatLng(37.5342644,126.951103))
        marker109.title("차오름동물병원").snippet("02-706-7975")
        googleMap?.addMarker(marker109)

        val marker110 = MarkerOptions()
        marker110.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker110.position(LatLng(37.5355454,127.006497))
        marker110.title("달래동물의료센터").snippet("02-794-1255")
        googleMap?.addMarker(marker110)

        val marker111 = MarkerOptions()
        marker111.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker111.position(LatLng(37.5264995,127.000738))
        marker111.title("보광동물병원").snippet("02-790-9808")
        googleMap?.addMarker(marker111)

        val marker112 = MarkerOptions()
        marker112.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker112.position(LatLng(37.5340118,126.950390))
        marker112.title("민트동물병원").snippet("02-707-2235")
        googleMap?.addMarker(marker112)

        val marker113 = MarkerOptions()
        marker113.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker113.position(LatLng(37.5363916,126.987217))
        marker113.title("이태원동물병원").snippet("02-797-6677")
        googleMap?.addMarker(marker113)

        val marker114 = MarkerOptions()
        marker114.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker114.position(LatLng(37.5430860,126.987815))
        marker114.title("퍼스동물병원").snippet("02-790-7508")
        googleMap?.addMarker(marker114)

        val marker115 = MarkerOptions()
        marker115.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker115.position(LatLng(37.5242610,126.970669))
        marker115.title("바다동물병원(Bada Animal Hospital)").snippet("02-792-2561")
        googleMap?.addMarker(marker115)

        val marker116 = MarkerOptions()
        marker116.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker116.position(LatLng(37.5192255,126.975286))
        marker116.title("베이동물병원").snippet("02-749-7588")
        googleMap?.addMarker(marker116)

        val marker117 = MarkerOptions()
        marker117.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker117.position(LatLng(37.5346712,127.009585))
        marker117.title("더힐동물의료센터").snippet("02-792-8275")
        googleMap?.addMarker(marker117)

        val marker118 = MarkerOptions()
        marker118.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker118.position(LatLng(37.5178750,126.980015))
        marker118.title("금강동물병원").snippet("02-798-7512")
        googleMap?.addMarker(marker118)

        val marker119 = MarkerOptions()
        marker119.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker119.position(LatLng(37.5396264,126.989536))
        marker119.title("라온동물병원").snippet("02-792-7585")
        googleMap?.addMarker(marker119)

        val marker120 = MarkerOptions()
        marker120.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker120.position(LatLng(37.5365206,126.960707))
        marker120.title("원효종합동물병원").snippet("02-719-7070")
        googleMap?.addMarker(marker120)

        val marker121 = MarkerOptions()
        marker121.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker121.position(LatLng(37.5582327,126.955818))
        marker121.title("마음을 나누는 동물병원").snippet("02-365-7582")
        googleMap?.addMarker(marker121)

        val marker122 = MarkerOptions()
        marker122.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker122.position(LatLng(37.5192255,126.975286))
        marker122.title("(주)이상윤동물병원").snippet("02-792-5455")
        googleMap?.addMarker(marker122)

        val marker123 = MarkerOptions()
        marker123.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker123.position(LatLng(37.5200261,126.969788))
        marker123.title("24시 시유동물메디컬센터").snippet("02-793-0075")
        googleMap?.addMarker(marker123)

        val marker124 = MarkerOptions()
        marker124.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker124.position(LatLng(37.4975972,127.102515))
        marker124.title("헬리오동물병원").snippet("02-2135-6575")
        googleMap?.addMarker(marker124)

        val marker125 = MarkerOptions()
        marker125.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker125.position(LatLng(37.4797160,126.904706))
        marker125.title("우리동물메디컬센터").snippet("02-853-7582")
        googleMap?.addMarker(marker125)

        val marker126 = MarkerOptions()
        marker126.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker126.position(LatLng(37.5486977,126.977924))
        marker126.title("아프리카동물병원").snippet("02-773-1119")
        googleMap?.addMarker(marker126)

        val marker127 = MarkerOptions()
        marker127.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker127.position(LatLng(37.5320136,126.999663))
        marker127.title("성심동물병원").snippet("02-794-7588")
        googleMap?.addMarker(marker127)

        val marker128 = MarkerOptions()
        marker128.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker128.position(LatLng(37.5560296,127.033943))
        marker128.title("오렌지동물병원").snippet("02-2297-7582")
        googleMap?.addMarker(marker128)

        val marker129 = MarkerOptions()
        marker129.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker129.position(LatLng(37.5533297,127.019535))
        marker129.title("한빛동물병원").snippet("02-2233-1848")
        googleMap?.addMarker(marker129)

        val marker130 = MarkerOptions()
        marker130.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker130.position(LatLng(37.5480226,127.021027))
        marker130.title("아지동물병원").snippet("02-6085-7750")
        googleMap?.addMarker(marker130)

        val marker131 = MarkerOptions()
        marker131.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker131.position(LatLng(37.5442573,127.021027))
        marker131.title("오석헌동물병원").snippet("02-6402-0301")
        googleMap?.addMarker(marker131)

        val marker132 = MarkerOptions()
        marker132.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker132.position(LatLng(37.5678077,127.025028))
        marker132.title("텐즈힐링동물병원").snippet("02-2295-0975")
        googleMap?.addMarker(marker132)

        val marker133 = MarkerOptions()
        marker133.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker133.position(LatLng(37.5478957,127.025260))
        marker133.title("펫365동물병원").snippet("02-2282-3653")
        googleMap?.addMarker(marker133)

        val marker134 = MarkerOptions()
        marker134.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker134.position(LatLng(37.5390840,127.053204))
        marker134.title("서울숲동물병원").snippet("02-466-2475")
        googleMap?.addMarker(marker134)

        val marker135 = MarkerOptions()
        marker135.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker135.position(LatLng(37.5665735,127.024066))
        marker135.title("바라봄동물병원").snippet("02-2297-9300")
        googleMap?.addMarker(marker135)

        val marker136 = MarkerOptions()
        marker136.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker136.position(LatLng(37.5659449,127.081418))
        marker136.title("다온동물병원").snippet("02-2039-7572")
        googleMap?.addMarker(marker136)

        val marker137 = MarkerOptions()
        marker137.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker137.position(LatLng(37.5392947,127.044684))
        marker137.title("달링동물병원").snippet("02-466-8215")
        googleMap?.addMarker(marker137)

        val marker138 = MarkerOptions()
        marker138.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker138.position(LatLng(37.5436177,127.049179))
        marker138.title("포레동물병원").snippet("02-468-7975")
        googleMap?.addMarker(marker138)

        val marker139 = MarkerOptions()
        marker139.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker139.position(LatLng(37.5645545,127.027857))
        marker139.title("왕십리조은동물병원").snippet("02-2297-0075")
        googleMap?.addMarker(marker139)

        val marker140 = MarkerOptions()
        marker140.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker140.position(LatLng(37.5462579,127.011227))
        marker140.title("스테이동물병원").snippet("02-2282-7501")
        googleMap?.addMarker(marker140)

        val marker141 = MarkerOptions()
        marker141.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker141.position(LatLng(37.5560128,127.029212))
        marker141.title("이윤세동물병원").snippet("02-2282-7503")
        googleMap?.addMarker(marker141)

        val marker142 = MarkerOptions()
        marker142.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker142.position(LatLng(37.5596417,127.036989))
        marker142.title("한양동물메디컬센터").snippet("02-2281-5200")
        googleMap?.addMarker(marker142)

        val marker143 = MarkerOptions()
        marker143.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker143.position(LatLng(37.5514638,127.024480))
        marker143.title("금호동물병원").snippet("02-2281-0049")
        googleMap?.addMarker(marker143)

        val marker144 = MarkerOptions()
        marker144.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker144.position(LatLng(37.5459596,127.019658))
        marker144.title("바른동물병원").snippet("02-2297-7570")
        googleMap?.addMarker(marker144)

        val marker145 = MarkerOptions()
        marker145.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker145.position(LatLng(37.5656764,127.042753))
        marker145.title("기린동물병원").snippet("02-2291-8275")
        googleMap?.addMarker(marker145)

        val marker146 = MarkerOptions()
        marker146.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker146.position(LatLng(37.5430051,127.013105))
        marker146.title("함께하는동물병원").snippet("02-2299-3283")
        googleMap?.addMarker(marker146)

        val marker147 = MarkerOptions()
        marker147.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker147.position(LatLng(37.5543540,127.032822))
        marker147.title("도담동물병원(출장진료전문 동물병원)").snippet("")
        googleMap?.addMarker(marker147)

        val marker148 = MarkerOptions()
        marker148.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker148.position(LatLng(37.5655270,127.030291))
        marker148.title("라임동물병원").snippet("02-2281-8122")
        googleMap?.addMarker(marker148)

        val marker149 = MarkerOptions()
        marker149.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker149.position(LatLng(37.5560128,127.029212))
        marker149.title("평생피부과동물병원").snippet("02-3395-0075")
        googleMap?.addMarker(marker149)

        val marker150 = MarkerOptions()
        marker150.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker150.position(LatLng(37.5485015,127.067085))
        marker150.title("수동물병원").snippet("02-3409-4640")
        googleMap?.addMarker(marker150)

        val marker151 = MarkerOptions()
        marker151.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker151.position(LatLng(37.5485015,127.067085))
        marker151.title("옥수수 동물병원").snippet("02-3409-4640")
        googleMap?.addMarker(marker151)

        val marker152 = MarkerOptions()
        marker152.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker152.position(LatLng(37.5571359,127.040089))
        marker152.title("바우라움동물병원").snippet("02-6454-6182")
        googleMap?.addMarker(marker152)

        val marker153 = MarkerOptions()
        marker153.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker153.position(LatLng(37.5468540,127.044847))
        marker153.title("오늘동물병원").snippet("02-469-7975")
        googleMap?.addMarker(marker153)

        val marker154 = MarkerOptions()
        marker154.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker154.position(LatLng(37.5390293,127.054664))
        marker154.title("제인동물병원").snippet("02-468-7582")
        googleMap?.addMarker(marker154)

        val marker155 = MarkerOptions()
        marker155.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker155.position(LatLng(37.5545344,127.087053))
        marker155.title("서울동물병원").snippet("02-2038-8253")
        googleMap?.addMarker(marker155)

        val marker156 = MarkerOptions()
        marker156.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker156.position(LatLng(37.5349307,127.070286))
        marker156.title("준동물병원").snippet("02-457-2220")
        googleMap?.addMarker(marker156)

        val marker157 = MarkerOptions()
        marker157.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker157.position(LatLng(37.5493479,127.082396))
        marker157.title("서울어린이대공원동물병원").snippet("02-450-9306")
        googleMap?.addMarker(marker157)

        val marker158 = MarkerOptions()
        marker158.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker158.position(LatLng(37.5484640,127.069710))
        marker158.title("파라곤동물병원").snippet("02-498-7588")
        googleMap?.addMarker(marker158)

        val marker159 = MarkerOptions()
        marker159.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker159.position(LatLng(37.5517480,127.090117))
        marker159.title("아차산동물병원").snippet("02-452-7580")
        googleMap?.addMarker(marker159)

        val marker160 = MarkerOptions()
        marker160.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker160.position(LatLng(37.5323619,127.085727))
        marker160.title("아이본 동물병원").snippet("02-455-5975")
        googleMap?.addMarker(marker160)

        val marker161 = MarkerOptions()
        marker161.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker161.position(LatLng(37.5431631,127.099544))
        marker161.title("나루동물병원").snippet("02-457-7582")
        googleMap?.addMarker(marker161)


        val marker162 = MarkerOptions()
        marker162.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker162.position(LatLng(37.5317806,127.079334))
        marker162.title("자양동물병원").snippet("02-456-0975")
        googleMap?.addMarker(marker162)

        val marker163 = MarkerOptions()
        marker163.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker163.position(LatLng(37.5654207,127.083772))
        marker163.title("드림펫 동물병원").snippet("02-464-7588")
        googleMap?.addMarker(marker163)

        val marker164 = MarkerOptions()
        marker164.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker164.position(LatLng(37.5368184,127.061847))
        marker164.title("노룬산동물병원").snippet("02-464-2748")
        googleMap?.addMarker(marker164)

        val marker165 = MarkerOptions()
        marker165.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker165.position(LatLng(37.5608184,127.081209))
        marker165.title("스마트동물병원군자점").snippet("02-464-7582")
        googleMap?.addMarker(marker165)

        val marker166 = MarkerOptions()
        marker166.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker166.position(LatLng(37.5392859,127.091969))
        marker166.title("프라임동물병원").snippet("02-457-0292")
        googleMap?.addMarker(marker166)

        val marker167 = MarkerOptions()
        marker167.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker167.position(LatLng(37.5417874,127.096181))
        marker167.title("동서울동물병원").snippet("02-444-7533")
        googleMap?.addMarker(marker167)

        val marker168 = MarkerOptions()
        marker168.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker168.position(LatLng(37.5472726,127.073251))
        marker168.title("피카소동물병원").snippet("02-467-3673")
        googleMap?.addMarker(marker168)

        val marker169 = MarkerOptions()
        marker169.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker169.position(LatLng(37.5595073,127.087863))
        marker169.title("용마동물병원").snippet("02-446-2349")
        googleMap?.addMarker(marker169)

        val marker170 = MarkerOptions()
        marker170.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker170.position(LatLng(37.5627373,127.081551))
        marker170.title("중곡동물병원").snippet("02-462-5595")
        googleMap?.addMarker(marker170)

        val marker171 = MarkerOptions()
        marker171.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker171.position(LatLng(37.5593139,127.080925))
        marker171.title("방주종합동물병원").snippet("02-454-9975")
        googleMap?.addMarker(marker171)

        val marker172 = MarkerOptions()
        marker172.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker172.position(LatLng(37.5452279,127.085934))
        marker172.title("광진동물의료센터").snippet("02-457-4545")
        googleMap?.addMarker(marker172)

        val marker173 = MarkerOptions()
        marker173.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker173.position(LatLng(37.5357981,127.066055))
        marker173.title("이다종합동물병원").snippet("02-463-7075")
        googleMap?.addMarker(marker173)

        val marker174 = MarkerOptions()
        marker174.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker174.position(LatLng(37.5553334,127.078136))
        marker174.title("나래동물병원").snippet("02-463-3216")
        googleMap?.addMarker(marker174)

        val marker175 = MarkerOptions()
        marker175.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker175.position(LatLng(37.5505084,127.071138))
        marker175.title("보람동물병원").snippet("02-465-7582")
        googleMap?.addMarker(marker175)

        val marker176 = MarkerOptions()
        marker176.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker176.position(LatLng(37.5505084,127.071138))
        marker176.title("보람동물병원").snippet("02-465-7582")
        googleMap?.addMarker(marker176)

        val marker177 = MarkerOptions()
        marker177.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker177.position(LatLng(37.5531730,127.088285))
        marker177.title("애플동물병원").snippet("02-455-0077")
        googleMap?.addMarker(marker177)

        val marker178 = MarkerOptions()
        marker178.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker178.position(LatLng(37.5320333,127.078757))
        marker178.title("로엘동물병원").snippet("02-457-8575")
        googleMap?.addMarker(marker178)

        val marker179 = MarkerOptions()
        marker179.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker179.position(LatLng(37.5315793,127.083916))
        marker179.title("해맑은동물병원").snippet("02-6954-7582")
        googleMap?.addMarker(marker179)

        val marker180 = MarkerOptions()
        marker180.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker180.position(LatLng(37.5357337,127.068307))
        marker180.title("로얄 도그앤캣 메디컬센터").snippet("02-468-6612")
        googleMap?.addMarker(marker180)

        val marker181 = MarkerOptions()
        marker181.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker181.position(LatLng(37.5386161,127.095849))
        marker181.title("광진24시필동물병원").snippet("02-446-8175")
        googleMap?.addMarker(marker181)

        val marker182 = MarkerOptions()
        marker182.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker182.position(LatLng(37.5378396,127.072529))
        marker182.title("쿨펫동물병원").snippet("02-2024-1253")
        googleMap?.addMarker(marker182)

        val marker183 = MarkerOptions()
        marker183.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker183.position(LatLng(37.5378396,127.072529))
        marker183.title("쿨펫동물병원").snippet("02-2024-1253")
        googleMap?.addMarker(marker183)

        val marker184 = MarkerOptions()
        marker184.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker184.position(LatLng(37.5204859,126.845368))
        marker184.title("24시월드펫동물메디컬센터").snippet("02-2698-7582")
        googleMap?.addMarker(marker184)

        val marker185 = MarkerOptions()
        marker185.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker185.position(LatLng(37.5420855,127.095186))
        marker185.title("쥬라기동물병원").snippet("02-455-7570")
        googleMap?.addMarker(marker185)

        val marker186 = MarkerOptions()
        marker186.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker186.position(LatLng(37.5650526,127.067371))
        marker186.title("아인스동물병원").snippet("02-2213-0075")
        googleMap?.addMarker(marker186)

        val marker187 = MarkerOptions()
        marker187.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker187.position(LatLng(37.5724914,127.073037))
        marker187.title("현대종합동물병원").snippet("02-2212-7596")
        googleMap?.addMarker(marker187)

        val marker188 = MarkerOptions()
        marker188.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker188.position(LatLng(37.5725951,127.065170))
        marker188.title("땅콩동물병원").snippet("02-3394-7075")
        googleMap?.addMarker(marker188)

        val marker189 = MarkerOptions()
        marker189.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker189.position(LatLng(37.5719344,127.024056))
        marker189.title("맑은 동물병원").snippet("02-2234-1379")
        googleMap?.addMarker(marker189)

        val marker190 = MarkerOptions()
        marker190.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker190.position(LatLng(37.5771662,127.036024))
        marker190.title("그린동물병원").snippet("02-957-7075")
        googleMap?.addMarker(marker190)

        val marker191 = MarkerOptions()
        marker191.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker191.position(LatLng(37.5746520,127.037607))
        marker191.title("엘동물병원").snippet("02-3295-3927")
        googleMap?.addMarker(marker191)

        val marker192 = MarkerOptions()
        marker192.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker192.position(LatLng(37.5751974,127.049448))
        marker192.title("제연동물병원").snippet("02-2242-7975")
        googleMap?.addMarker(marker192)

        val marker193 = MarkerOptions()
        marker193.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker193.position(LatLng(37.5719506,127.073535))
        marker193.title("VIP동물의료센터").snippet("02-2215-7522")
        googleMap?.addMarker(marker193)

        val marker194 = MarkerOptions()
        marker194.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker194.position(LatLng(37.5894908,127.061018))
        marker194.title("하나동물병원").snippet("02-6242-7500")
        googleMap?.addMarker(marker194)

        val marker195 = MarkerOptions()
        marker195.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker195.position(LatLng(37.5903320,127.054032))
        marker195.title("봄봄동물병원").snippet("02-969-7579")
        googleMap?.addMarker(marker195)

        val marker196 = MarkerOptions()
        marker196.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker196.position(LatLng(37.5737276,127.052311))
        marker196.title("아띠동물의료센터").snippet("02-6242-8275")
        googleMap?.addMarker(marker196)

        val marker197 = MarkerOptions()
        marker197.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker197.position(LatLng(37.5754949,127.067652))
        marker197.title("스마트동물병원").snippet("02-2242-0175")
        googleMap?.addMarker(marker197)

        val marker198 = MarkerOptions()
        marker198.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker198.position(LatLng(37.5984732,127.061874))
        marker198.title("바론동물병원").snippet("02-965-0075")
        googleMap?.addMarker(marker198)

        val marker199 = MarkerOptions()
        marker199.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker199.position(LatLng(37.5984732,127.061874))
        marker199.title("바론동물병원").snippet("02-965-0075")
        googleMap?.addMarker(marker199)

        val marker200 = MarkerOptions()
        marker200.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker200.position(LatLng(37.5724557,127.055616))
        marker200.title("더케이동물병원").snippet("02-2213-8875")
        googleMap?.addMarker(marker200)

        val marker201 = MarkerOptions()
        marker201.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker201.position(LatLng(37.5666081,127.055883))
        marker201.title("하이유 동물병원").snippet("02-2242-7510")
        googleMap?.addMarker(marker201)

        val marker202 = MarkerOptions()
        marker202.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker202.position(LatLng(37.5910916,127.066499))
        marker202.title("심재웅동물병원").snippet("02-2244-7755")
        googleMap?.addMarker(marker202)

        val marker203 = MarkerOptions()
        marker203.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker203.position(LatLng(37.5788995,127.069670))
        marker203.title("박철환동물병원").snippet("02-2244-7479")
        googleMap?.addMarker(marker203)

        val marker204 = MarkerOptions()
        marker204.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker204.position(LatLng(37.5695861,127.064511))
        marker204.title("뉴월드동물병원").snippet("02-2212-5233")
        googleMap?.addMarker(marker204)

        val marker205 = MarkerOptions()
        marker205.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker205.position(LatLng(37.5728460,127.067683))
        marker205.title("페트로동물의료원").snippet("02-2249-7600")
        googleMap?.addMarker(marker205)

        val marker206 = MarkerOptions()
        marker206.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker206.position(LatLng(37.5940238,127.058847))
        marker206.title("애니케어동물병원").snippet("02-967-0877")
        googleMap?.addMarker(marker206)

        val marker207 = MarkerOptions()
        marker207.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker207.position(LatLng(37.5845698,127.043963))
        marker207.title("푸른종합동물병원").snippet("02-963-7582")
        googleMap?.addMarker(marker207)

        val marker208 = MarkerOptions()
        marker208.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker208.position(LatLng(37.5728889,127.071374))
        marker208.title("장평25시동물종합병원").snippet("02-2215-2022")
        googleMap?.addMarker(marker208)

        val marker209 = MarkerOptions()
        marker209.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker209.position(LatLng(37.5805293,127.026776))
        marker209.title("안암동물병원").snippet("02-925-3686")
        googleMap?.addMarker(marker209)

        val marker210 = MarkerOptions()
        marker210.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker210.position(LatLng(37.5778513,127.058461))
        marker210.title("주주동물병원").snippet("02-2244-1081")
        googleMap?.addMarker(marker210)

        val marker211 = MarkerOptions()
        marker211.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker211.position(LatLng(37.5859304,127.043511))
        marker211.title("디아크동물병원").snippet("02-960-7501")
        googleMap?.addMarker(marker211)

        val marker212 = MarkerOptions()
        marker212.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker212.position(LatLng(37.5719322,127.057574))
        marker212.title("아이원 동물병원").snippet("02-2217-8930")
        googleMap?.addMarker(marker212)

        val marker213 = MarkerOptions()
        marker213.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker213.position(LatLng(37.5917099,127.072342))
        marker213.title("서울우유협동조합유우진료소").snippet("02-2094-8344")
        googleMap?.addMarker(marker213)

        val marker214 = MarkerOptions()
        marker214.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker214.position(LatLng(37.6157785,127.093738))
        marker214.title("도그플러스 동물병원").snippet("02-3421-9075")
        googleMap?.addMarker(marker214)

        val marker215 = MarkerOptions()
        marker215.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker215.position(LatLng(37.6077832,127.098168))
        marker215.title("행복이 있는 동물병원").snippet("02-433-3387")
        googleMap?.addMarker(marker215)

        val marker216 = MarkerOptions()
        marker216.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker216.position(LatLng(37.5899353,127.097386))
        marker216.title("보담동물병원").snippet("02-494-7775")
        googleMap?.addMarker(marker216)

        val marker217 = MarkerOptions()
        marker217.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker217.position(LatLng(37.5950301,127.079400))
        marker217.title("한국동물영상의학센터").snippet("02-6215-3416")
        googleMap?.addMarker(marker217)

        val marker218 = MarkerOptions()
        marker218.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker218.position(LatLng(37.5924216,127.072615))
        marker218.title("중랑차 동물병원").snippet("")
        googleMap?.addMarker(marker218)

        val marker219 = MarkerOptions()
        marker219.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker219.position(LatLng(37.5823380,127.088274))
        marker219.title("혜원동물병원").snippet("02-439-8300")
        googleMap?.addMarker(marker219)

        val marker220 = MarkerOptions()
        marker220.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker220.position(LatLng(37.5770060,127.085732))
        marker220.title("보람동물병원").snippet("02-434-5465")
        googleMap?.addMarker(marker220)

        val marker221 = MarkerOptions()
        marker221.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker221.position(LatLng(37.5887800,127.088349))
        marker221.title("엔젤동물병원").snippet("02-435-0166")
        googleMap?.addMarker(marker221)

        val marker222 = MarkerOptions()
        marker222.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker222.position(LatLng(37.6106545,127.077446))
        marker222.title("홍상희동물병원").snippet("02-974-0339")
        googleMap?.addMarker(marker222)

        val marker223 = MarkerOptions()
        marker223.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker223.position(LatLng(37.5915561,127.086937))
        marker223.title("하스펫탈동물병원").snippet("02-435-2275")
        googleMap?.addMarker(marker223)

        val marker224 = MarkerOptions()
        marker224.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker224.position(LatLng(37.6027022,127.085693))
        marker224.title("신내동물병원").snippet("02-3422-0001")
        googleMap?.addMarker(marker224)

        val marker225 = MarkerOptions()
        marker225.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker225.position(LatLng(37.6017519,127.078750))
        marker225.title("진정훈동물병원").snippet("02-2207-0073")
        googleMap?.addMarker(marker225)

        val marker226 = MarkerOptions()
        marker226.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker226.position(LatLng(37.5798967,127.087750))
        marker226.title("성지동물병원").snippet("02-493-7582")
        googleMap?.addMarker(marker226)

        val marker227 = MarkerOptions()
        marker227.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker227.position(LatLng(37.5772764,127.085687))
        marker227.title("주연동물병원").snippet("02-2207-9931")
        googleMap?.addMarker(marker227)

        val marker228 = MarkerOptions()
        marker228.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker228.position(LatLng(37.6046856,127.095899))
        marker228.title("진서동물병원").snippet("02-432-7530")
        googleMap?.addMarker(marker228)

        val marker229 = MarkerOptions()
        marker229.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker229.position(LatLng(37.5986668,127.096231))
        marker229.title("세진동물병원").snippet("02-436-7311")
        googleMap?.addMarker(marker229)

        val marker230 = MarkerOptions()
        marker230.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker230.position(LatLng(37.6125825,127.077561))
        marker230.title("태능동물병원").snippet("02-973-1953")
        googleMap?.addMarker(marker230)

        val marker231 = MarkerOptions()
        marker231.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker231.position(LatLng(37.5984380,127.100579))
        marker231.title("금란동물병원").snippet("02-492-7582")
        googleMap?.addMarker(marker231)

        val marker232 = MarkerOptions()
        marker232.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker232.position(LatLng(37.5954172,127.080000))
        marker232.title("24시 동물응급의료센터").snippet("02-2208-7533")
        googleMap?.addMarker(marker232)

        val marker233 = MarkerOptions()
        marker233.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker233.position(LatLng(37.5893010,127.090535))
        marker233.title("숲속동물병원").snippet("02-492-9303")
        googleMap?.addMarker(marker233)

        val marker234 = MarkerOptions()
        marker234.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker234.position(LatLng(37.5811962,127.084876))
        marker234.title("면목동물병원").snippet("02-437-7579")
        googleMap?.addMarker(marker234)

        val marker235 = MarkerOptions()
        marker235.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker235.position(LatLng(37.6168705,127.091270))
        marker235.title("하니동물병원").snippet("02-3422-0977")
        googleMap?.addMarker(marker235)

        val marker236 = MarkerOptions()
        marker236.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker236.position(LatLng(37.5881782,127.085880))
        marker236.title("한국동물메디컬센터(KAMC)").snippet("02-2208-6788")
        googleMap?.addMarker(marker236)

        val marker237 = MarkerOptions()
        marker237.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker237.position(LatLng(37.6050373,127.095605))
        marker237.title("아이봄 동물병원").snippet("02-433-7515")
        googleMap?.addMarker(marker237)

        val marker238 = MarkerOptions()
        marker238.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker238.position(LatLng(37.5833443,127.079444))
        marker238.title("레미 동물병원").snippet("02-470-4707")
        googleMap?.addMarker(marker238)

        val marker239 = MarkerOptions()
        marker239.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker239.position(LatLng(37.5874782,127.094099))
        marker239.title("닥터멍동물병원").snippet("02-439-7582")
        googleMap?.addMarker(marker239)

        val marker240 = MarkerOptions()
        marker240.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker240.position(LatLng(37.5954172,127.080000))
        marker240.title("로얄동물메디컬센터").snippet("02-494-7582")
        googleMap?.addMarker(marker240)

        val marker241 = MarkerOptions()
        marker241.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker241.position(LatLng(37.5972139,127.087453))
        marker241.title("닥터 아이펫 동물병원").snippet("02-2208-0329")
        googleMap?.addMarker(marker241)

        val marker242 = MarkerOptions()
        marker242.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker242.position(LatLng(37.6136192,127.060969))
        marker242.title("대학동물병원").snippet("02-918-3344")
        googleMap?.addMarker(marker242)

        val marker243 = MarkerOptions()
        marker243.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker243.position(LatLng(37.5961953,127.035138))
        marker243.title("강북동물병원").snippet("02-924-4555")
        googleMap?.addMarker(marker243)

        val marker244 = MarkerOptions()
        marker244.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker244.position(LatLng(37.5944964,127.016307))
        marker244.title("성신동물병원").snippet("02-927-7517")
        googleMap?.addMarker(marker244)

        val marker245 = MarkerOptions()
        marker245.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker245.position(LatLng(37.6119732,127.028757))
        marker245.title("더편한동물병원").snippet("02-982-7975")
        googleMap?.addMarker(marker245)

        val marker246 = MarkerOptions()
        marker246.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker246.position(LatLng(37.6212728,127.051698))
        marker246.title("퍼스트동물병원").snippet("02-911-7582")
        googleMap?.addMarker(marker246)

        val marker247 = MarkerOptions()
        marker247.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker247.position(LatLng(37.6101462,127.018167))
        marker247.title("스마트동물병원 성북길음점").snippet("02-941-5575")
        googleMap?.addMarker(marker247)

        val marker248 = MarkerOptions()
        marker248.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker248.position(LatLng(37.6042043,127.037723))
        marker248.title("포유동물병원").snippet("02-912-6555")
        googleMap?.addMarker(marker248)

        val marker249 = MarkerOptions()
        marker249.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker249.position(LatLng(37.5953434,127.015741))
        marker249.title("로이동물병원").snippet("02-927-7878")
        googleMap?.addMarker(marker249)

        val marker250 = MarkerOptions()
        marker250.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker250.position(LatLng(37.6074343,127.017963))
        marker250.title("카카오 N 동물병원").snippet("02-942-4883")
        googleMap?.addMarker(marker250)

        val marker251 = MarkerOptions()
        marker251.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker251.position(LatLng(37.6058205,127.024361))
        marker251.title("강북 24시 N 동물의료센터").snippet("02-984-0075")
        googleMap?.addMarker(marker251)

        val marker252 = MarkerOptions()
        marker252.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker252.position(LatLng(37.6087909,127.035755))
        marker252.title("넬 동물의료센터").snippet("02-6925-4347")
        googleMap?.addMarker(marker252)

        val marker253 = MarkerOptions()
        marker253.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker253.position(LatLng(37.5934432,127.001700))
        marker253.title("앙리동물병원").snippet("02-766-7577")
        googleMap?.addMarker(marker253)

        val marker254 = MarkerOptions()
        marker254.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker254.position(LatLng(37.5918207,127.013679))
        marker254.title("드림동물병원").snippet("02-928-7582")
        googleMap?.addMarker(marker254)

        val marker255 = MarkerOptions()
        marker255.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker255.position(LatLng(37.6070151,127.060658))
        marker255.title("태양 동물병원").snippet("02-969-0075")
        googleMap?.addMarker(marker255)

        val marker256 = MarkerOptions()
        marker256.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker256.position(LatLng(37.6001421,127.033656))
        marker256.title("쓰담쓰담 동물병원").snippet("02-927-7585")
        googleMap?.addMarker(marker256)

        val marker257 = MarkerOptions()
        marker257.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker257.position(LatLng(37.6067656,127.028506))
        marker257.title("호담동물병원").snippet("02-915-5301")
        googleMap?.addMarker(marker257)

        val marker258 = MarkerOptions()
        marker258.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker258.position(LatLng(37.6190676,127.046418))
        marker258.title("꿈의숲동물병원").snippet("02-915-5485")
        googleMap?.addMarker(marker258)

        val marker259 = MarkerOptions()
        marker259.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker259.position(LatLng(37.6018068,127.040429))
        marker259.title("도담도담동물병원").snippet("02-943-7582")
        googleMap?.addMarker(marker259)

        val marker260 = MarkerOptions()
        marker260.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker260.position(LatLng(37.6024330,127.023035))
        marker260.title("길음동물병원").snippet("02-913-1874")
        googleMap?.addMarker(marker260)

        val marker261 = MarkerOptions()
        marker261.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker261.position(LatLng(37.6110986,127.056493))
        marker261.title("행복한동물병원").snippet("02-915-7582")
        googleMap?.addMarker(marker261)

        val marker262 = MarkerOptions()
        marker262.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker262.position(LatLng(37.6089439,127.059221))
        marker262.title("중앙동물병원").snippet("02-967-0300")
        googleMap?.addMarker(marker262)

        val marker263 = MarkerOptions()
        marker263.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker263.position(LatLng(37.6028695,127.041992))
        marker263.title("비비펫 동물병원").snippet("02-912-0018")
        googleMap?.addMarker(marker263)

        val marker264 = MarkerOptions()
        marker264.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker264.position(LatLng(37.6105886,127.009356))
        marker264.title("보성통증동물병원").snippet("02-943-8376")
        googleMap?.addMarker(marker264)

        val marker265 = MarkerOptions()
        marker265.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker265.position(LatLng(37.6019921,127.019524))
        marker265.title("ZOO동물병원").snippet("02-914-5545")
        googleMap?.addMarker(marker265)

        val marker266 = MarkerOptions()
        marker266.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker266.position(LatLng(37.6109204,127.022494))
        marker266.title("우리동물병원").snippet("02-989-8575")
        googleMap?.addMarker(marker266)

        val marker267 = MarkerOptions()
        marker267.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker267.position(LatLng(37.5839183,127.019814))
        marker267.title("24시 애니동물병원").snippet("02-926-8275")
        googleMap?.addMarker(marker267)

        val marker268 = MarkerOptions()
        marker268.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker268.position(LatLng(37.6139863,127.045679))
        marker268.title("큐동물병원").snippet("")
        googleMap?.addMarker(marker268)

        val marker269 = MarkerOptions()
        marker269.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker269.position(LatLng(37.5903347,127.003817))
        marker269.title("한사랑동물병원").snippet("02-743-7582")
        googleMap?.addMarker(marker269)

        val marker270 = MarkerOptions()
        marker270.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker270.position(LatLng(37.5914154,127.013011))
        marker270.title("나래종합동물병원").snippet("02-929-5496")
        googleMap?.addMarker(marker270)

        val marker271 = MarkerOptions()
        marker271.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker271.position(LatLng(37.5870900,127.018173))
        marker271.title("서울종합동물병원").snippet("02-928-5599")
        googleMap?.addMarker(marker271)

        val marker272 = MarkerOptions()
        marker272.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker272.position(LatLng(37.5988124,127.013601))
        marker272.title("원러브동물의료센터").snippet("02-6956-0040")
        googleMap?.addMarker(marker272)

        val marker273 = MarkerOptions()
        marker273.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker273.position(LatLng(37.6095582,127.030614))
        marker273.title("24시루시드동물메디컬센터").snippet("02-941-7900")
        googleMap?.addMarker(marker273)

        val marker274 = MarkerOptions()
        marker274.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker274.position(LatLng(37.6170021,127.022088))
        marker274.title("아이사랑동물병원 건강검진센터").snippet("02-946-0011")
        googleMap?.addMarker(marker274)

        val marker275 = MarkerOptions()
        marker275.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker275.position(LatLng(37.6188926,127.029371))
        marker275.title("모네동물피부과").snippet("070-8691-3121")
        googleMap?.addMarker(marker275)

        val marker276 = MarkerOptions()
        marker276.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker276.position(LatLng(37.6175159,127.020434))
        marker276.title("솔샘동물병원").snippet("02-988-7582")
        googleMap?.addMarker(marker276)

        val marker277 = MarkerOptions()
        marker277.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker277.position(LatLng(37.6477356,127.014232))
        marker277.title("애니케어 동물병원 수유점").snippet("02-996-1007")
        googleMap?.addMarker(marker277)

        val marker278 = MarkerOptions()
        marker278.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker278.position(LatLng(37.6477356,127.014232))
        marker278.title("H 동물병원").snippet("02-984-7588")
        googleMap?.addMarker(marker278)

        val marker279 = MarkerOptions()
        marker279.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker279.position(LatLng(37.6433747,127.015682))
        marker279.title("초이스동물병원").snippet("02-993-8266")
        googleMap?.addMarker(marker279)

        val marker280 = MarkerOptions()
        marker280.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker280.position(LatLng(37.6458701,127.018198))
        marker280.title("대형동물병원").snippet("02-998-1785")
        googleMap?.addMarker(marker280)

        val marker281 = MarkerOptions()
        marker281.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker281.position(LatLng(37.6414367,127.022242))
        marker281.title("24시 수유 바른펫 동물의료센터").snippet("02-903-7582")
        googleMap?.addMarker(marker281)

        val marker282 = MarkerOptions()
        marker282.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker282.position(LatLng(37.6110980,127.033774))
        marker282.title("창문동물병원").snippet("02-988-0251")
        googleMap?.addMarker(marker282)

        val marker283 = MarkerOptions()
        marker283.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker283.position(LatLng(37.6267229,127.026282))
        marker283.title("위즈동물병원").snippet("02-987-7959")
        googleMap?.addMarker(marker283)

        val marker284 = MarkerOptions()
        marker284.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker284.position(LatLng(37.6208404,127.021047))
        marker284.title("삼양동물병원").snippet("02-989-9552")
        googleMap?.addMarker(marker284)

        val marker285 = MarkerOptions()
        marker285.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker285.position(LatLng(37.6170093,127.030379))
        marker285.title("중앙동물병원").snippet("02-982-8698")
        googleMap?.addMarker(marker285)

        val marker286 = MarkerOptions()
        marker286.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker286.position(LatLng(37.6347784,127.022512))
        marker286.title("동물병원 위드").snippet("02-907-1565")
        googleMap?.addMarker(marker286)

        val marker287 = MarkerOptions()
        marker287.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker287.position(LatLng(37.6453389,127.015308))
        marker287.title("사랑동물병원").snippet("02-991-4304")
        googleMap?.addMarker(marker287)

        val marker288 = MarkerOptions()
        marker288.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker288.position(LatLng(37.6437520,127.023013))
        marker288.title("현대종합동물병원").snippet("02-999-9110")
        googleMap?.addMarker(marker288)

        val marker289 = MarkerOptions()
        marker289.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker289.position(LatLng(37.6258413,127.018454))
        marker289.title("봉식동물병원").snippet("02-983-7000")
        googleMap?.addMarker(marker289)

        val marker290 = MarkerOptions()
        marker290.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker290.position(LatLng(37.6139300,127.020705))
        marker290.title("삼성동물병원").snippet("02-980-2340")
        googleMap?.addMarker(marker290)

        val marker291 = MarkerOptions()
        marker291.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker291.position(LatLng(37.6202917,127.015326))
        marker291.title("스마트 동물병원").snippet("02-987-7588")
        googleMap?.addMarker(marker291)

        val marker292 = MarkerOptions()
        marker292.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker292.position(LatLng(37.6408577,127.032733))
        marker292.title("동물을사랑하는사람들").snippet("02-990-7588")
        googleMap?.addMarker(marker292)

        val marker293 = MarkerOptions()
        marker293.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker293.position(LatLng(37.6288005,127.039876))
        marker293.title("강북동물병원").snippet("02-985-7525")
        googleMap?.addMarker(marker293)

        val marker294 = MarkerOptions()
        marker294.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker294.position(LatLng(37.6334277,127.017719))
        marker294.title("보노보노동물병원").snippet("02-903-5525")
        googleMap?.addMarker(marker294)

        val marker295 = MarkerOptions()
        marker295.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker295.position(LatLng(37.6119175,127.035349))
        marker295.title("호 동물병원").snippet("02-988-6543")
        googleMap?.addMarker(marker295)

        val marker296 = MarkerOptions()
        marker296.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker296.position(LatLng(37.6175159,127.020434))
        marker296.title("쿠키동물병원").snippet("02-986-9875")
        googleMap?.addMarker(marker296)

        val marker297 = MarkerOptions()
        marker297.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker297.position(LatLng(37.6170021,127.022088))
        marker297.title("아이사랑동물병원").snippet("02-946-0011")
        googleMap?.addMarker(marker297)

        val marker298 = MarkerOptions()
        marker298.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker298.position(LatLng(37.6585777,127.040277))
        marker298.title("별난동물병원").snippet("02-905-9119")
        googleMap?.addMarker(marker298)

        val marker299 = MarkerOptions()
        marker299.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker299.position(LatLng(37.6453443,127.033676))
        marker299.title("트윈스동물병원").snippet("02-906-7575")
        googleMap?.addMarker(marker299)

        val marker300 = MarkerOptions()
        marker300.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker300.position(LatLng(37.6825320,127.047477))
        marker300.title("해봄동물병원").snippet("02-954-6990")
        googleMap?.addMarker(marker300)

        val marker301 = MarkerOptions()
        marker301.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker301.position(LatLng(37.6579212,127.036401))
        marker301.title("태일동물종합병원").snippet("02-997-0075")
        googleMap?.addMarker(marker301)

        val marker302 = MarkerOptions()
        marker302.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker302.position(LatLng(37.6579212,127.036401))
        marker302.title("강북우리동물의료센터").snippet("02-997-0075")
        googleMap?.addMarker(marker302)

        val marker303 = MarkerOptions()
        marker303.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker303.position(LatLng(37.6594151,127.041751))
        marker303.title("행복한동물병원").snippet("02-996-8275")
        googleMap?.addMarker(marker303)

        val marker304 = MarkerOptions()
        marker304.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker304.position(LatLng(37.5343606,127.135964))
        marker304.title("강동24시 SKY 동물의료센터").snippet("02-472-7579")
        googleMap?.addMarker(marker304)

        val marker305 = MarkerOptions()
        marker305.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker305.position(LatLng(37.6629543,127.046343))
        marker305.title("365동물병원").snippet("02-6402-0365")
        googleMap?.addMarker(marker305)

        val marker306 = MarkerOptions()
        marker306.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker306.position(LatLng(37.6573557,127.028219))
        marker306.title("보듬동물병원").snippet("02-6954-7544")
        googleMap?.addMarker(marker306)

        val marker307 = MarkerOptions()
        marker307.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker307.position(LatLng(37.6013961,126.931331))
        marker307.title("치유동물의료센터").snippet("02-6964-8276")
        googleMap?.addMarker(marker307)

        val marker308 = MarkerOptions()
        marker308.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker308.position(LatLng(37.6612741,127.031688))
        marker308.title("유현동물병원").snippet("02-955-2882")
        googleMap?.addMarker(marker308)

        val marker309 = MarkerOptions()
        marker309.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker309.position(LatLng(37.6626970,127.034080))
        marker309.title("가나동물병원").snippet("02-3491-4748")
        googleMap?.addMarker(marker309)

        val marker310 = MarkerOptions()
        marker310.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker310.position(LatLng(37.6476599,127.033541))
        marker310.title("퓨리나동물병원").snippet("02-900-9929")
        googleMap?.addMarker(marker310)

        val marker311 = MarkerOptions()
        marker311.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker311.position(LatLng(37.6382524,127.037967))
        marker311.title("신창동물병원").snippet("02-992-9414")
        googleMap?.addMarker(marker311)

        val marker312 = MarkerOptions()
        marker312.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker312.position(LatLng(37.6637781,127.034624))
        marker312.title("화신종합동물병원").snippet("02-3491-7060")
        googleMap?.addMarker(marker312)

        val marker313 = MarkerOptions()
        marker313.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker313.position(LatLng(37.6401709,127.039769))
        marker313.title("한빛동물병원").snippet("02-992-0074")
        googleMap?.addMarker(marker313)

        val marker314 = MarkerOptions()
        marker314.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker314.position(LatLng(37.6826589,127.045516))
        marker314.title("도담동물병원").snippet("02-955-8400")
        googleMap?.addMarker(marker314)

        val marker315 = MarkerOptions()
        marker315.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker315.position(LatLng(37.6721803,37.6721803))
        marker315.title("빛가온동물병원").snippet("02-6369-0075")
        googleMap?.addMarker(marker315)

        val marker316 = MarkerOptions()
        marker316.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker316.position(LatLng(37.6643884,127.042037))
        marker316.title("현대종합동물병원").snippet("02-3491-8000")
        googleMap?.addMarker(marker316)

        val marker317 = MarkerOptions()
        marker317.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker317.position(LatLng(37.6694052,127.046471))
        marker317.title("김미경 동물병원").snippet("02-955-6530")
        googleMap?.addMarker(marker317)

        val marker318 = MarkerOptions()
        marker318.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker318.position(LatLng(37.6520254,127.046506))
        marker318.title("하비동물병원").snippet("02-2038-9980")
        googleMap?.addMarker(marker318)

        val marker319 = MarkerOptions()
        marker319.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker319.position(LatLng(37.6500558,127.035910))
        marker319.title("둘리동물병원").snippet("02-993-0075")
        googleMap?.addMarker(marker319)

        val marker320 = MarkerOptions()
        marker320.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker320.position(LatLng(37.6690821,127.043173))
        marker320.title("방학동물병원").snippet("02-3494-0075")
        googleMap?.addMarker(marker320)

        val marker321 = MarkerOptions()
        marker321.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker321.position(LatLng(37.6501101,127.035208))
        marker321.title("삼성동물종합병원").snippet("02-902-7582")
        googleMap?.addMarker(marker321)

        val marker322 = MarkerOptions()
        marker322.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker322.position(LatLng(37.6690821,127.043173))
        marker322.title("방학동물외과센터").snippet("02-3494-0075")
        googleMap?.addMarker(marker322)

        val marker323 = MarkerOptions()
        marker323.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker323.position(LatLng(37.6427089,127.066544))
        marker323.title("우리들동물종합병원").snippet("02-979-8275")
        googleMap?.addMarker(marker323)

        val marker324 = MarkerOptions()
        marker324.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        marker324.position(LatLng(37.6427089,127.066544))
        marker324.title("당고개동물병원").snippet("02-932-7582")
        googleMap?.addMarker(marker324)



    }

    //---------------------------------------------------------------------------------------

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            providerClient.lastLocation.addOnSuccessListener(
                this@MapActivity,
                object : OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let {
                            val latitude = p0.latitude
                            val longitude = p0.longitude
                            Log.d("KD", "$latitude, $longitude")
                            moveMap(latitude, longitude)
                        }
                    }
                }
            )
            apiClient.disconnect()



        }


    }


    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        loadHospital()

    }

    //API 가져오기---------------------------------------------------------------------------------



    fun loadHospital(){
        //레트로피 사용
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(OpenAPI.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        //레트로핏에 서비스 가져옴
        val service=retrofit.create(OpenService::class.java)

        service.getHospital(OpenAPI.API_KEY,1000)
            .enqueue(object :Callback<AnimalHospital>{
                override fun onFailure(call: Call<AnimalHospital>, t: Throwable) {
                    Toast.makeText(this@MapActivity,"데이터를 가져올 수 없습니다.",Toast.LENGTH_LONG).show()
                    Log.d("KD", "onFailure 에러: "  + t.message.toString())
                }

                override fun onResponse(call: Call<AnimalHospital>, response: Response<AnimalHospital>) {
                    val result = response.body()
                    showHospital(result)
                    Log.d("KD","API를 연동합니다."+response.body().toString())
                }
            })


    }

    //API 좌표랑 마커 설정
    fun showHospital(result: AnimalHospital?){
        result?.let{

            for(hospital in it.lOCALDATA020301!!.row!!){
                try{
                    val position = LatLng(hospital?.x?.toDouble()!!,hospital?.y?.toDouble()!!)

                    val marker = MarkerOptions()
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    marker.position(position)
                    marker.title(hospital.bPLCNM).snippet(hospital.sITETEL)

                    googleMap?.addMarker(marker)

                }catch (e: Exception){

                    Log.e("KD", "에러: "  + e.message.toString())
                    return@let
                }
            }

        }
    }


}

