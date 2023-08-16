import type { ListenerCallback, PluginListenerHandle} from '@capacitor/core';
import { WebPlugin } from '@capacitor/core';

import type { BluetoothPlugin, PermissionStatus } from './definitions';

export class BluetoothWeb extends WebPlugin implements BluetoothPlugin {
  init(): Promise<void> {
    throw this.unavailable('Not implemented on web.');
  }
  startListening(): Promise<boolean> {
    throw this.unavailable('Not implemented on web.');
  }
  async stopListening(): Promise<boolean> {
    throw this.unavailable('Not implemented on web.');
  }

  async checkPermissions(): Promise<PermissionStatus> {
    throw this.unavailable('Not implemented on web.');
  }
  async requestPermissions(): Promise<PermissionStatus> {
    throw this.unavailable('Not implemented on web.');
  }
  async getPairedDevices(): Promise<{ devices: any[] }> {
    throw this.unavailable('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async write(_data: { message: string; }): Promise<boolean> {
    throw this.unavailable('Not implemented on web.');
  }
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  async connect(_data: {deviceId: string}): Promise<boolean> {
    throw this.unavailable('Not implemented on web.');
  }

  async disconnect(): Promise<boolean> {
    throw this.unavailable('Not implemented on web.');
  }
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  addListener(_eventName: string, _listenerFunc: ListenerCallback): Promise<PluginListenerHandle> & PluginListenerHandle {
    throw this.unavailable('Not implemented on web.');
  }
}
