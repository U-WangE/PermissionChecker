package com.uwange.permissionchecker

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.databinding.ActivityMainBinding
import com.uwange.permissionchecker.databinding.IncludeItemBinding
import com.uwange.permissionchecker.manager.PermissionChecker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var type: Type? = null

    private var bluetoothSelectedView: TextView? = null
    private var locationSelectedView: TextView? = null

    private lateinit var permissionChecker: PermissionChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionChecker = PermissionChecker(this)

        setUI()
        requestPermission()
    }

    fun setUI() {
        with(binding) {
            val permissionViews: Array<Triple<IncludeItemBinding, String, Type?>> =
                arrayOf(
                    Triple(permission1, "Location Permission", Type.LocationType.Location),
                    Triple(permission2, "LocationAlways Permission", Type.LocationType.LocationAlways),
                    Triple(permission3, "Bluetooth Permission", Type.BluetoothType.Bluetooth),
                    Triple(permission4, "Bluetooth Scan Permission", Type.BluetoothType.BluetoothScan),
                    Triple(permission5, "Bluetooth Connect Permission", Type.BluetoothType.BluetoothConnect),
                    Triple(permission6, "Bluetooth Advertise Permission", Type.BluetoothType.BluetoothAdvertise),
                    Triple(permission7, "BluetoothALL Permission", Type.BluetoothType.BluetoothALL),
                    Triple(permission8, "- Permission", null)
                )

            permissionViews.forEachIndexed { index, triple ->
                try {
                    triple.first.tvName.text = triple.second
                    triple.first.root.setOnClickListener {
                        if (index >= 0 && index <= 1) {
                            selectMasking(triple.first, triple.third)
                        } else if (index >= 2 && index <= 6) {
                            selectMasking(triple.first, triple.third)
                        }else {}
                    }
                } catch (e:Exception) {
                    Log.e("Matching Failure", e.toString())
                }
            }
        }
    }

    fun requestPermission() {
        permissionChecker.result {
            Log.d(type.toString(), it.toString())
            type = null
        }

        binding.btnRequest.setOnClickListener {
            type?.let {
                permissionChecker.request(it)
            }

            bluetoothSelectedView?.isSelected = false
            locationSelectedView?.isSelected = false
            bluetoothSelectedView = null
            locationSelectedView = null
        }
    }

    fun selectMasking(view: IncludeItemBinding, type: Type?) {
        if (!view.tvNumber.isSelected) {
            view.tvNumber.isSelected = true
        } else {
            view.tvNumber.isSelected = false
        }

        when (type) {
            is Type.LocationType -> {
                if (locationSelectedView != view.tvNumber)
                    locationSelectedView?.isSelected = false

                locationSelectedView = view.tvNumber
                this.type = type
            }
            is Type.BluetoothType -> {
                if (bluetoothSelectedView != view.tvNumber)
                    bluetoothSelectedView?.isSelected = false

                bluetoothSelectedView = view.tvNumber
                this.type = type
            }
            else -> return
        }
    }

    fun alert(callback:(Boolean) -> Unit): AlertDialog {
        // AlertDialog 만들기
        val dialog = AlertDialog.Builder(this)
            .setTitle("알림")
            .setMessage("이것은 간단한 AlertDialog 예시입니다.")
            .setPositiveButton("확인") { dialog, which ->
                println("확인 버튼이 클릭되었습니다.")
                callback(true)
            }
            .setNeutralButton("취소") { dialog, which ->
                println("확인 버튼이 클릭되었습니다.")
                callback(false)
            }
            .create()

        // 다이얼로그 표시
        return dialog
    }
}

//package com.uwange.permissionchecker
//
//import android.app.AlertDialog
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.uwange.permissionchecker.databinding.ActivityMainBinding
//import com.uwange.permissionchecker.databinding.IncludeItemBinding
//import com.uwange.permissionchecker.tool.BluetoothPermission
//import com.uwange.permissionchecker.tool.LocationPermission
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//
//    private var bluetoothType: Type? = null
//    private var locationType: Type? = null
//
//    private var bluetoothSelectedView: TextView? = null
//    private var locationSelectedView: TextView? = null
//
//    private lateinit var bluetoothPermission: BluetoothPermission
//    private lateinit var locationPermission: LocationPermission
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setUI()
//        requestPermission()
//    }
//
//    fun setUI() {
//        with(binding) {
//            val permissionViews: Array<Triple<IncludeItemBinding, String, Type?>> =
//                arrayOf(
//                    Triple(permission1, "Location Permission", Type.LocationType.Location),
//                    Triple(permission2, "Bluetooth Permission", Type.BluetoothType.Bluetooth),
//                    Triple(permission3, "Bluetooth Scan Permission", Type.BluetoothType.BluetoothScan),
//                    Triple(permission4, "Bluetooth Connect Permission", Type.BluetoothType.BluetoothConnect),
//                    Triple(permission5, "Bluetooth Advertise Permission", Type.BluetoothType.BluetoothAdvertise),
//                    Triple(permission6, "BluetoothALL Permission", Type.BluetoothType.BluetoothALL),
//                    Triple(permission7, "- Permission", null),
//                    Triple(permission8, "- Permission", null)
//                )
//
//            permissionViews.forEachIndexed { index, triple ->
//                try {
//                    triple.first.tvName.text = triple.second
//                    triple.first.root.setOnClickListener {
//                        if (index == 0) {
//                            selectMasking(triple.first, triple.third)
//                        } else if (index >= 1 && index <= 5) {
//                            selectMasking(triple.first, triple.third)
//                        }else {}
//                    }
//                } catch (e:Exception) {
//                    Log.e("Matching Failure", e.toString())
//                }
//            }
//        }
//    }
//
//    fun requestPermission() {
//        bluetoothPermission = BluetoothPermission(this)
//        locationPermission = LocationPermission(this)
//
//        bluetoothPermission.result {
//            Log.d("BLUETOOTH", it.toString())
//            bluetoothType = null
//
//            locationType?.let {
//                locationPermission.request(it)
//            }
//        }
//        locationPermission.result {
//            Log.d("LOCATION", it.toString())
//            locationType = null
//
//            bluetoothType?.let {
//                bluetoothPermission.request(it)
//            }
//        }
//
//
//        binding.btnRequest.setOnClickListener {
//            bluetoothType?.let {
//                bluetoothPermission.request(it)
//            }?: locationType?.let {
//                locationPermission.request(it)
//            }
//            bluetoothSelectedView?.isSelected = false
//            locationSelectedView?.isSelected = false
//            bluetoothSelectedView = null
//            locationSelectedView = null
//        }
//    }
//
//    fun selectMasking(view: IncludeItemBinding, type: Type?) {
//        if (!view.tvNumber.isSelected) {
//            view.tvNumber.isSelected = true
//        } else {
//            view.tvNumber.isSelected = false
//        }
//
//        when (type) {
//            is Type.LocationType -> {
//                if (locationSelectedView != view.tvNumber)
//                    locationSelectedView?.isSelected = false
//
//                locationSelectedView = view.tvNumber
//                locationType = type
//            }
//            is Type.BluetoothType -> {
//                if (bluetoothSelectedView != view.tvNumber)
//                    bluetoothSelectedView?.isSelected = false
//
//                bluetoothSelectedView = view.tvNumber
//                bluetoothType = type
//            }
//            else -> return
//        }
//    }
//
//    fun alert(callback:(Boolean) -> Unit): AlertDialog {
//        // AlertDialog 만들기
//        val dialog = AlertDialog.Builder(this)
//            .setTitle("알림")
//            .setMessage("이것은 간단한 AlertDialog 예시입니다.")
//            .setPositiveButton("확인") { dialog, which ->
//                println("확인 버튼이 클릭되었습니다.")
//                callback(true)
//            }
//            .setNeutralButton("취소") { dialog, which ->
//                println("확인 버튼이 클릭되었습니다.")
//                callback(false)
//            }
//            .create()
//
//        // 다이얼로그 표시
//        return dialog
//    }
//}