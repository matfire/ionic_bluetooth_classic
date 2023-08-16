import { registerPlugin } from '@capacitor/core';

import type { BluetoothPlugin } from './definitions';

const Bluetooth = registerPlugin<BluetoothPlugin>('Bluetooth', {
  web: () => import('./web').then(m => new m.BluetoothWeb()),
});

export * from './definitions';
export { Bluetooth };
