# YelpApp
Examen técnico para Desarrollador Android

Esta aplicación fue desarrollada como parte de una prueba técnica en donde debemos consumir la API de Yelp
para mostrar información sobre restaurantes cercanos a nuestra posición actual así como su imágen y su descripción.

<img src="app/src/main/ic_launcher-playstore.png" alt="YelpApp"/>

## Requisitos
- Min SDK API 21 (Android 5.0)
- Target SDK API 31 (Android 12.0)

## Arquitectura:
- La aplicación fue desarrollada bajo la arquitectura MVVM (Model View View-Model) con Kotlin

## Características:
- Inicio de sesión y Registro con Firebase Auth.
- Integración con Firebase Analytics y Firebase Crashlytics.
- Integración Room - RecyclerView - Paging3 - LiveData.
- Pantalla principal con buscador (Vista de Lista y Vista de Mapa).
- Consumo de API Yelp.
- Pantalla de historial de visualización.
- Navegación entre pantallas con Navigation Component.
- Vinculación de vista con ViewBinding y DataBinding.

## Librerías utilizadas:
- Room (2.4.2)
- Navigation Component (2.4.1)
- AppCompat (1.4.1)
- Material Design (1.5.0)
- Coroutines (1.6.0-native-mt)
- LifecycleExtension (2.2.0)
- Lifecycle (2.4.1)
- Paging (3.1.1)
- MultiDex (2.0.1)'
- Google Mobile Services Location (19.0.1)
- Google Mobile Services Maps (18.0.2)
- Firebase Core (20.1.2)
- Firebase Analytics (20.1.2)
- Firebase Crashlytics (18.2.9)
- Firebase Auth (21.0.3)
- Glide (4.13.1)
- CircleImageView (3.1.0)
- GSON (2.9.0)
- Retrofit (2.9.0)
- OkHttp (5.0.0-alpha.6)

# Notas
- Se incluye APK firmada en la raíz del repositorio. 