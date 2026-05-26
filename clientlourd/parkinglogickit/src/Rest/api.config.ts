import { Capacitor } from '@capacitor/core';

const NATIVE_REST_API_URL = 'https://plk.niavlysdev.fr/ParkingLogicKitServeur/rest';
const WEB_REST_API_URL = '/ParkingLogicKit/rest';

export const REST_API_URL = Capacitor.isNativePlatform() ? NATIVE_REST_API_URL : WEB_REST_API_URL;
