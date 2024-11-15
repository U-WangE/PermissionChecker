package com.uwange.permissionchecker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.databinding.ActivityMainBinding
import com.uwange.permissionchecker.databinding.IncludeItemBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var permissionCheckList = ArrayList<Type>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.permission1.tvName.text = "Location Permission"
        binding.permission2.tvName.text = "Bluetooth Permission"
        binding.permission3.tvName.text = "Bluetooth Admin Permission"
        binding.permission4.tvName.text = "Bluetooth Scan Permission"
        binding.permission5.tvName.text = "Bluetooth Connect Permission"
        binding.permission6.tvName.text = "Bluetooth Advertise Permission"
        binding.permission7.tvName.text = "- Permission"
        binding.permission8.tvName.text = "- Permission"

        val bluetoothPermission = BluetoothPermission(this)
        val locationPermission = LocationPermission(this)


        bluetoothPermission.result {
            Log.d("BLUETOOTH", it.toString())
            locationPermission.request(Type.Location)
        }
        locationPermission.result {
            Log.d("LOCATION", it.toString())
            bluetoothPermission.request(Type.BluetoothScan)
        }
        binding.permission1.root.setOnClickListener {
            selectMasking(binding.permission1, Type.Location)
        }
        binding.permission2.root.setOnClickListener {
            selectMasking(binding.permission2, Type.Bluetooth)
        }
        binding.permission3.root.setOnClickListener {
            selectMasking(binding.permission3, Type.BluetoothScan)
        }
        binding.permission4.root.setOnClickListener {
            selectMasking(binding.permission4, Type.BluetoothConnect)
        }
        binding.permission5.root.setOnClickListener {
            selectMasking(binding.permission5, Type.BluetoothAdvertise)
        }
        binding.permission6.root.setOnClickListener {
//            selectMasking(binding.permission6, Type.)
        }
        binding.permission7.root.setOnClickListener {
//            selectMasking(binding.permission7, Type.)
        }
        binding.permission8.root.setOnClickListener {
//            selectMasking(binding.permission8, Type.)
        }

        binding.btnRequest.setOnClickListener {
            bluetoothPermission.request(Type.Bluetooth)
        }
    }

    fun selectMasking(view: IncludeItemBinding, type: Type) {
        if (!view.tvNumber.isSelected) {
            view.tvNumber.isSelected = true
        } else {
            view.tvNumber.isSelected = false
        }
    }
}