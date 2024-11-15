package com.uwange.permissionchecker

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.databinding.ActivityMainBinding
import com.uwange.permissionchecker.databinding.IncludeItemBinding
import com.uwange.permissionchecker.manager.BluetoothPermission
import com.uwange.permissionchecker.manager.LocationPermission
import com.uwange.permissionchecker.Type

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var bluetoothType: Type? = null
    private var locationType: Type? = null

    private var bluetoothSelectedView: TextView? = null
    private var locationSelectedView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
        requestPermission()
    }

    fun setUI() {
        with(binding) {
            val permissionViews: Array<Triple<IncludeItemBinding, String, Type?>> =
                arrayOf(
                    Triple(permission1, "Location Permission", LocationType.Location),
                    Triple(permission2, "Bluetooth Permission", BluetoothType.Bluetooth),
                    Triple(permission3, "Bluetooth Scan Permission", BluetoothType.BluetoothScan),
                    Triple(permission4, "Bluetooth Connect Permission", BluetoothType.BluetoothConnect),
                    Triple(permission5, "Bluetooth Advertise Permission", BluetoothType.BluetoothAdvertise),
                    Triple(permission6, "BluetoothALL Permission", BluetoothType.BluetoothALL),
                    Triple(permission7, "- Permission", null),
                    Triple(permission8, "- Permission", null)
                )

            permissionViews.forEachIndexed { index, triple ->
                try {
                    triple.first.tvName.text = triple.second
                    triple.first.root.setOnClickListener {
                        if (index == 0) {
                            selectMasking(triple.first, triple.third)
                        } else if (index >= 1 && index <= 5) {
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
        val bluetoothPermission = BluetoothPermission(this)
        val locationPermission = LocationPermission(this)

        bluetoothPermission.result {
            Log.d("BLUETOOTH", it.toString())
            bluetoothType = null

            locationType?.let {
                locationPermission.request(it)
            }
        }
        locationPermission.result {
            Log.d("LOCATION", it.toString())
            locationType = null

            bluetoothType?.let {
                bluetoothPermission.request(it)
            }
        }


        binding.btnRequest.setOnClickListener {
            bluetoothType?.let {
                bluetoothPermission.request(it)
            }?: locationType?.let {
                locationPermission.request(it)
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
            is LocationType -> {
                if (locationSelectedView != view.tvNumber)
                    locationSelectedView?.isSelected = false

                locationSelectedView = view.tvNumber
                locationType = type
            }
            is BluetoothType -> {
                if (bluetoothSelectedView != view.tvNumber)
                    bluetoothSelectedView?.isSelected = false

                bluetoothSelectedView = view.tvNumber
                bluetoothType = type
            }
            else -> return
        }
    }
}