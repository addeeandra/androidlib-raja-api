# Raja Api
_Library_ ini menggunakan _**Kotlin Coroutines**_ dan _**Async Callback**_.

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
Lihat _demo_ untuk melihat penggunaan _library_ secara keseluruhan

## Penggunaan - _Kotlin Coroutines_

#### Inisialisasi _Coroutine Service_ dari Wilayah API
```kotlin
val mApiInstance = WilayahApiCoroutineService.instance()
```

Pastikan untuk memanggil fungsi di bawah ini dengan `launch { ... }` atau `async { ... }`


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

## Penggunaan - _Async Callback_

#### Inisialisasi _Async Service_ dari Wilayah API
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

#### API : Mendapatkan Wilayah Lain
Penerapan lainnya sama seperti _API : Mendapatkan Provinsi_

```kotlin
  mApiInstance.getKabupaten(mUniqueCode, provinsiId).execute( ... )
  mApiInstance.getKecamatan(mUniqueCode, kabupatenId).execute( ... )
  mApiInstance.getKelurahan(mUniqueCode, kecamatanId).execute( ... )
```

## Kontribusi
Silakan laporkan jika ada _bugs_ ataupun _additional feature_ yang perlu ditambahkan ke dalam _issues tracker_ ataupun _pull request_.

Semoga bermanfaat :D
