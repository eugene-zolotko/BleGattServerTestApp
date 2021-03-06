package com.example.eugenezolotko.blegattservertestapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final UUID INCLUDED_SERVICE_1_UUID =
            UUID.fromString("00001236-0000-1000-8000-00805f9b34fb");
    private static final UUID SERVICE_1_UUID =
            UUID.fromString("00001234-0000-1000-8000-00805f9b34fb");

    private BluetoothGattServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openServer();
    }

    @Override
    protected void onDestroy() {
        closeServer();
        super.onDestroy();
    }

    private void openServer() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        server = bluetoothManager.openGattServer(getApplicationContext(), callback);
        createServices();
    }

    private void createServices() {
        final BluetoothGattService includedService1 = new BluetoothGattService(
                INCLUDED_SERVICE_1_UUID,
                BluetoothGattService.SERVICE_TYPE_SECONDARY);
        final BluetoothGattCharacteristic characteristic2 = new BluetoothGattCharacteristic(
                UUID.fromString("00001237-0000-1000-8000-00805f9b34fb"),
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);
        includedService1.addCharacteristic(characteristic2);
        server.addService(includedService1);
    }

    private void closeServer() {
        server.close();
    }

    private final BluetoothGattServerCallback callback = new BluetoothGattServerCallback() {

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            if (service.getUuid().equals(INCLUDED_SERVICE_1_UUID)) {
                final BluetoothGattService service1 = new BluetoothGattService(
                        SERVICE_1_UUID,
                        BluetoothGattService.SERVICE_TYPE_PRIMARY);
                final BluetoothGattCharacteristic characteristic1 = new BluetoothGattCharacteristic(
                        UUID.fromString("00001235-0000-1000-8000-00805f9b34fb"),
                        BluetoothGattCharacteristic.PROPERTY_READ,
                        BluetoothGattCharacteristic.PERMISSION_READ);
                service1.addService(service);
                service1.addCharacteristic(characteristic1);
                server.addService(service1);
            }
        }
    };
}
