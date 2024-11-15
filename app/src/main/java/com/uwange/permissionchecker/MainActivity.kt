package com.uwange.permissionchecker

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.databinding.ActivityMainBinding
import com.uwange.permissionchecker.databinding.IncludeItemBinding

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
            val permissionViews: Array<IncludeItemBinding> = arrayOf(
                permission1,
                permission2,
                permission3,
                permission4,
                permission5,
                permission6,
                permission7,
                permission8
            )

            val textTitles = arrayOf(
                "Location Permission",
                "Bluetooth Permission",
                "Bluetooth Scan Permission",
                "Bluetooth Connect Permission",
                "Bluetooth Advertise Permission",
                "- Permission",
                "- Permission",
                "- Permission"
            )

            permissionViews.forEachIndexed { index, view ->
                try {
                    view.tvName.text = textTitles[index]
                    view.root.setOnClickListener {
                        if (index == 0) {
                            selectMasking(view, Type.Location)
                        } else if (index >= 1 && index <= 4) {
                            selectMasking(view, Type.Bluetooth)
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
            locationType?.let {
                locationPermission.request(it)
            }
        }
        locationPermission.result {
            Log.d("LOCATION", it.toString())
            locationType?.let {
                bluetoothPermission.request(it)
            }
        }

        binding.btnRequest.setOnClickListener {
            bluetoothType?.let {
                bluetoothType = null
                bluetoothPermission.request(it)
            }?: locationType?.let {
                locationType = null
                locationPermission.request(it)
            }
        }
    }

    fun selectMasking(view: IncludeItemBinding, type: Type) {
        if (!view.tvNumber.isSelected) {
            view.tvNumber.isSelected = true
        } else {
            view.tvNumber.isSelected = false
        }

        when (type) {
            Type.Location -> {
                locationSelectedView?.isSelected = false
                locationSelectedView = view.tvNumber
                locationType = Type.Location
            }
            Type.Bluetooth -> {
                bluetoothSelectedView?.isSelected = false
                bluetoothSelectedView = view.tvNumber
                bluetoothType = Type.Bluetooth
            }
            else -> return
        }
    }
}