package com.uwange.permissionchecker

sealed interface Type {
    enum class BluetoothType : Type {
        Bluetooth, BluetoothScan, BluetoothConnect, BluetoothAdvertise, BluetoothALL
    }

    enum class LocationType : Type {
        Location, LocationAlways
    }

    enum class ETCType : Type {
        Overlay
    }
}