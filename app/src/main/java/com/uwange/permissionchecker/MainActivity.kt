package com.uwange.permissionchecker

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_SCAN
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.permission1.tvName.text = "Location Permission"
        binding.permission2.tvName.text = "Bluetooth Permission"
        binding.permission3.tvName.text = "Bluetooth Permission"
        binding.permission4.tvName.text = "Bluetooth Permission"
        binding.permission5.tvName.text = "Bluetooth Permission"
        binding.permission6.tvName.text = "Bluetooth Permission"
        binding.permission7.tvName.text = "Bluetooth Permission"
        binding.permission8.tvName.text = "Bluetooth Permission"

        val permissionChecker = PermissionChecker(this)
        permissionChecker.result {

        }
        permissionChecker.request(Type.Bluetooth)

    }
}