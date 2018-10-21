# Raja Api
Note : This library are available with **Kotlin Coroutines** and **async callback**.

[![](https://jitpack.io/v/inibukanadit/raja-api.svg)](https://jitpack.io/#inibukanadit/raja-api)

## Installation
1. Add maven repository on your root `build.gradle`
```gradle
allprojects {
  repositories {
    //...
    maven { url "https://jitpack.io" }
  }
}
```
2. Add the dependencies on module level `build.gradle`
```gradle
dependencies {
  //...
  implementation 'com.github.inibukanadit:raja-api:x.x.x'
  //...
}
```

## How to use - Kotlin Coroutines

Ensure to call it inside Coroutines - `launch { ... }` or `async { ... }`

#### First - Get the API Instance
```kotlin
val mApiInstance = WilayahApiCoroutineService.instance()
```

#### API : Mendapatkan Kode Unik
```kotlin
  val result = mApiInstance.getKodeUnik().await()
  val uniqueCode = WilayahApi.getUniqueCode(result)
  // save the unique code somewhere
```

#### API : Mendapatkan Daftar Provinsi
```kotlin
  val result = mApiInstance.getProvinsi(uniqueCode).await()
  val provinces = WilayahApi.getAreaList(result) // List<Area>
```

#### API : Mendapatkan Daftar Kabupaten
```kotlin
  val result = mApiInstance.getKabupaten(uniqueCode, provinceId).await()
  val cities = WilayahApi.getAreaList(result) // List<Area>
```

#### API : Mendapatkan Daftar Kecamatan
```kotlin
  val result = mApiInstance.getKecamatan(uniqueCode, kabupatenId).await()
  val districts = WilayahApi.getAreaList(result) // List<Area>
```

#### API : Mendapatkan Daftar Kelurahan
```kotlin
  val result = mApiInstance.getKelurahan(uniqueCode, kecamatanId).await()
  val villages = WilayahApi.getAreaList(result) // List<Area>
```

## Contribute
I would love to know what you need. Issues and pull requests would be helped.
