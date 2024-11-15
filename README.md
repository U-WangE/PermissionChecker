# Permission Checker

## Description

### 1. Type
필요로 하는 권한 Permission Group 화 하여 Type 으로 명시적으로 표시함

<details><summary>USE</summary>
<ul>
  <li>
    <details><summary>Location Type</summary>
      <table>
        <tr>
          <td rowspan="2">Type.Location</td>
          <td>android.permission.ACCESS_FINE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.ACCESS_COARSE_LOCATION</td>
        </tr>
      </table>
    </details>
  </li>
  <li>
    <details><summary>Bluetooth Type</summary>
      <table>
        <tr>
          <td>Type.Bluetooth</td>
          <td></td>
          <td>android.permission.BLUETOOTH</td>
        </tr>
        <tr><td colspan="3">&nbsp;</td></tr>
        <tr>
          <td rowspan="7">Type.BluetoothScan</td>
          <td rowspan="3">Device Api Level > Api Level 30</td>
          <td>android.permission.ACCESS_FINE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.ACCESS_COARSE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH_SCAN</td>
        </tr>
        <tr>
          <td rowspan="4">Device Api Level <= Api Level 30</td>
          <td>android.permission.ACCESS_FINE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.ACCESS_COARSE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH_ADMIN</td>
        </tr>
        <tr><td colspan="3">&nbsp;</td></tr>
        <tr>
          <td rowspan="3">Type.BluetoothConnect</td>
          <td>Device Api Level > Api Level 30</td>
          <td>android.permission.CONNECT</td>
        </tr>
        <tr>
          <td rowspan="2">Device Api Level <= Api Level 30</td>
          <td>android.permission.BLUETOOTH</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH_ADMIN</td>
        </tr>
        <tr><td colspan="3">&nbsp;</td></tr>
        <tr>
          <td rowspan="3">Type.BluetoothAdvertise</td>
          <td>Device Api Level > Api Level 30</td>
          <td>android.permission.BLUETOOTH_ADVERTISE</td>
        </tr>
        <tr>
          <td rowspan="2">Device Api Level <= Api Level 30</td>
          <td>android.permission.BLUETOOTH</td>
        </tr>
        <tr>
          <td>Type.BluetoothALL</td>
        </tr>
        <tr><td colspan="3">&nbsp;</td></tr>
        <tr>
          <td rowspan="9">Type.BluetoothAdvertise</td>
          <td rowspan="5">Device Api Level > Api Level 30</td>
          <td>android.permission.ACCESS_FINE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.ACCESS_COARSE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH_SCAN</td>
        </tr>
        <tr>
          <td>android.permission.CONNECT</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH_ADVERTISE</td>
        </tr>
        <tr>
          <td rowspan="4">Device Api Level <= Api Level 30</td>
          <td>android.permission.ACCESS_FINE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.ACCESS_COARSE_LOCATION</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH</td>
        </tr>
        <tr>
          <td>android.permission.BLUETOOTH_ADMIN</td>
        </tr>
      </table>
    </details>
  </li>
</ul>
</details>

### 2. Request

### 3. Result

### 4. Destroy
