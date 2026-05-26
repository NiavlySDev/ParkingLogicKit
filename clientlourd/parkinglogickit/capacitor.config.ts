import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'ParkingLogicKit.LML',
  appName: 'ParkingLogicKit',
  webDir: 'dist/ParkingLogicKit/browser',
  plugins: {
    CapacitorHttp: {
      enabled: true
    }
  }
};

export default config;
