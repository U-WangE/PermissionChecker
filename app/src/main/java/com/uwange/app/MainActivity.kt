package com.uwange.app

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.uwange.app.databinding.IncludeItemBinding
import com.uwange.permissionchecker.Type
import com.uwange.app.databinding.ActivityMainBinding
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
                    Triple(permission2, "LocationAlways Permission",
                        Type.LocationType.LocationAlways
                    ),
                    Triple(permission3, "Bluetooth Permission", Type.BluetoothType.Bluetooth),
                    Triple(permission4, "Bluetooth Scan Permission",
                        Type.BluetoothType.BluetoothScan
                    ),
                    Triple(permission5, "Bluetooth Connect Permission",
                        Type.BluetoothType.BluetoothConnect
                    ),
                    Triple(permission6, "Bluetooth Advertise Permission",
                        Type.BluetoothType.BluetoothAdvertise
                    ),
                    Triple(permission7, "BluetoothALL Permission", Type.BluetoothType.BluetoothALL),
                    Triple(permission8, "- Permission", null)
                )

            permissionViews.forEachIndexed { index, triple ->
                try {
                    triple.first.tvName.text = triple.second
                    triple.first.root.setOnClickListener {
                        if (index in 0..6)
                            selectMasking(triple.first, triple.third)
                        else {}
                    }
                } catch (e:Exception) {
                    Log.e("Matching Failure", e.toString())
                }
            }
        }
    }

    fun requestPermission() {
        permissionChecker.result {
            Log.i(type.toString(), it.toString())
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