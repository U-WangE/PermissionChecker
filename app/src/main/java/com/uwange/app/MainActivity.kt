package com.uwange.app

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uwange.app.databinding.IncludeItemBinding
import com.uwange.permissionchecker.Type
import com.uwange.app.databinding.ActivityMainBinding
import com.uwange.permissionchecker.manager.PermissionChecker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var type: Type? = null


    private val permissionViews: MutableList<Triple<IncludeItemBinding, String, Type?>> = mutableListOf()
    private var selectedView: TextView? = null

    private lateinit var permissionChecker: PermissionChecker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionChecker = PermissionChecker(this)

        testPermissionChecker()

        setUI()
    }

    private fun testPermissionChecker() {
        permissionChecker.result {
            Log.i(type.toString(), it.toString())
            type = null
        }

        binding.btnRequest.setOnClickListener {
            type?.let {
                permissionChecker.request(it)
            }

            selectedView?.isSelected = false
            selectedView = null
        }
    }

    fun setUI() {
        with(binding) {
            addReferenceView("Location Permission", Type.LocationType.Location)
            addReferenceView("LocationAlways Permission", Type.LocationType.LocationAlways)
            addReferenceView("Bluetooth Permission", Type.BluetoothType.Bluetooth)
            addReferenceView("Bluetooth Scan Permission", Type.BluetoothType.BluetoothScan)
            addReferenceView("Bluetooth Connect Permission", Type.BluetoothType.BluetoothConnect)
            addReferenceView("Bluetooth Advertise Permission", Type.BluetoothType.BluetoothAdvertise)
            addReferenceView("BluetoothALL Permission", Type.BluetoothType.BluetoothALL)

        }
    }

    private fun addReferenceView(name: String, type: Type) {
        val includeView =
            IncludeItemBinding.inflate(LayoutInflater.from(this), binding.main, false)
                .apply {
                    root.id = View.generateViewId()
                    tvName.text = name
                    root.setOnClickListener {
                        selectMasking(this, type)
                    }
                }

        permissionViews.add(Triple(includeView, name, type))
        binding.root.addView(includeView.root)
        binding.flLayout.referencedIds += includeView.root.id
    }

    private fun selectMasking(view: IncludeItemBinding, type: Type?) {
        if (!view.tvNumber.isSelected) {
            view.tvNumber.isSelected = true
        } else {
            view.tvNumber.isSelected = false
        }

        if (selectedView != view.tvNumber)
            selectedView?.isSelected = false

        selectedView = view.tvNumber
        this.type = type
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