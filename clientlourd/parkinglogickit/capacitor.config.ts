import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  // SÉCURISATION & NORMALISATION : Passage au format de domaine inversé standard (obligatoire pour Android/iOS)
  appId: 'fr.parkinglogickit.lml',
  appName: 'ParkingLogicKit',
  webDir: 'dist/ParkingLogicKit/browser',
  plugins: {
    CapacitorHttp: {
      enabled: true,
    },
  },
  // OPTIMISATION SÉCURITÉ : Empêche le cache de la WebView de stocker des requêtes sensibles localement
  server: {
    allowNavigation: [], // Limite la navigation uniquement à l'intérieur de l'application packagée
  },
};

export default config;
