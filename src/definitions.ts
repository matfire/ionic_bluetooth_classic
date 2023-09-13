import type { PermissionState, PluginListenerHandle } from '@capacitor/core';

export enum DeviceStatus {
  DISCONNECTED = 0,
  CONNECTED = 1,
  CONNECTING = 2,
}

export interface IDevice {
  address: string;
  name?: string;
}

export interface IDataReceived {
  bytes: string;
  size: number;
}

export interface IDeviceStatus {
  deviceId: string;
  status: DeviceStatus;
}

export interface BluetoothPlugin {
  checkPermissions(): Promise<PermissionStatus>;
  requestPermissions(): Promise<PermissionStatus>;
  startListening(): Promise<boolean>;
  stopListening(): Promise<boolean>;
  write(data: { message: string }): Promise<boolean>;
  addListener(
    eventName: 'deviceConnectionStatusChanged',
    listenerFunc: ({ data }: { data: IDeviceStatus }) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  addListener(
    eventName: 'deviceDataReceived',
    listenerFunc: ({ data }: { data: IDataReceived }) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  addListener(
    eventName: 'deviceDiscovered',
    listenerFunc: ({ device }: { device: IDevice }) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  getPairedDevices(): Promise<{ devices: any[] }>;
  connect(data: { deviceId: string; appUUID: string }): Promise<boolean>;
  disconnect(): Promise<boolean>
  //isConnected(deviceId: string): Promise<boolean>
}

export interface PermissionStatus {
  bluetooth: PermissionState;
  location: PermissionState;
}
