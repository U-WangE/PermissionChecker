package com.uwange.permissionchecker

sealed interface Type {
    enum class BluetoothType: Type {
        Bluetooth,
        BluetoothScan,
        BluetoothConnect,
        BluetoothAdvertise,
        BluetoothALL
    }

    enum class LocationType: Type {
        Location,
        LocationAlways
    }

    enum class StorageType: Type {
        /**
         * [Android Developers - WRITE_EXTERNAL_STORAGE](https://developer.android.com/about/versions/11/privacy/storage?hl=ko#permissions-target-11)
         * API 레벨 30부터 이 권한은 더 이상 추가 액세스를 제공하지 않습니다.
         * Constant Value: WRITE_EXTERNAL_STORAGE
         */
        WriteForVerUnder30,
        /**
         * [Android Developers - WRITE_EXTERNAL_STORAGE](https://developer.android.com/about/versions/11/privacy/storage?hl=ko#permissions-target-11)
         * [Android Developers - READ_EXTERNAL_STORAGE](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE)
         * API 레벨 30부터 WRITE_EXTERNAL_STORAGE 권한은 더 이상 추가 액세스를 제공하지 않습니다.
         * Constant Value: WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
         */
        ReadWriteForVerUnder30,
        /**
         * [Android Developers - READ_EXTERNAL_STORAGE](https://developer.android.com/reference/android/Manifest.permission#READ_EXTERNAL_STORAGE)
         * API 레벨 33부터 이 권한은 효과가 없습니다.
         * Constant Value: READ_EXTERNAL_STORAGE
         */
        ReadForVerUnder33
    }

    enum class ETCType: Type {
        Overlay
    }
}