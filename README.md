# Raja Api
Library ini menggunakan **Kotlin Coroutines** dan **Async Service (Callback)**. 

[![](https://jitpack.io/v/inibukanadit/raja-api.svg)](https://jitpack.io/#inibukanadit/raja-api)

## Instalasi
1. Tambahkan _maven repository_ di _root_ `build.gradle`
```gradle
allprojects {
  repositories {
    //...
    maven { url "https://jitpack.io" }
  }
}
```
2. Tambahkan _dependencies_ pada `build.gradle` di tingkat _module_
```gradle
dependencies {
  //...
  implementation 'com.github.inibukanadit:raja-api:x.x.x'
  //...
}
```

## Penggunaan
Lihat **demo** untuk melihat penggunaan _library_ secara keseluruhan. Atau baca dokumentasinya di Wiki : https://github.com/addeeandra/androidlib-raja-api/wiki

## Penggunaan - _Coroutines Service_

#### Inisialisasi _Coroutine Service_ dari Wilayah API
```kotlin
val mApiInstance = WilayahApiCoroutineService.instance()
```

Pastikan untuk memanggil fungsi di bawah ini dengan `GlobalScope.launch { ... }` atau `GlobalScope.async { ... }`


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

Lebih lengkapnya bisa Anda baca di Wiki : https://github.com/addeeandra/androidlib-raja-api/wiki/2.-Coroutine-Service

## Penggunaan - Async Service (Callback)

#### Inisialisasi Async Service dari Wilayah API
```kotlin
  val mApiInstance = WilayahApiAsyncService.instance()
```

#### API : Mendapatkan Kode Unik
```kotlin
  mApiInstance
      .getKodeUnik()
      .execute(object : WilayahApiAsyncWrapper.Callback<String> {
        override fun onResult(data: String?, error: String?) {
          data?.let { mUniqueCode = it }
        }
      })
```

#### API : Mendapatkan Provinsi
```kotlin
  mApiInstance
      .getProvinsi(mUniqueCode)
      .execute(object : WilayahApiAsyncWrapper.Callback<List<Area>> {
        override fun onResult(data: List<Area>?, error: String?) {
          data?.let { showProvince(it) }
        }
      })
```

Lebih lengkapnya bisa Anda baca di Wiki : https://github.com/addeeandra/androidlib-raja-api/wiki/3.-Async-Service-(Callback)

## Kontribusi
Silakan laporkan jika ada bugs. Jika ada fitur yang ingin ditambahkan, silakan buat _issue_ baru atau lakukan _pull request_. :)

Semoga bermanfaat~
