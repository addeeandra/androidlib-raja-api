# Raja Api
Note : This library are using **Kotlin Coroutines**.

[![](https://jitpack.io/v/inibukanadit/raja-api.svg)](https://jitpack.io/#inibukanadit/raja-api)

## Installation
1. Add `maven { url "https://jitpack.io" }` on your root `build.gradle`
```gradle
allprojects {
  repositories {
    //...
    maven { url "https://jitpack.io" }
  }
}
```
2. Add `implementation 'com.github.inibukanadit:raja-api:x.x.x'` on module level `build.gradle`
```gradle
dependencies {
  //...
  implementation 'com.github.inibukanadit:raja-api:x.x.x'
  //...
}
```

Replace **x.x.x** with latest version releases : [See here](https://github.com/inibukanadit/raja-api/releases)

## How to use
#### Import the Singleton Object
```kotlin
import me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService
```

#### API : Mendapatkan Kode Unik
```kotlin
//...
  GlobalScope.launch {
    val code = WilayahApi.getKodeUnik()
    saveTheCodeSomewhere(code) // save it to later use
  }
//...
```
#### API : Mendapatkan Daftar Provinsi
```kotlin
//...
  GlobalScope.launch {
    val result = WilayahApi.getProvinsi(kodeUnik)
    when(result) {
      is Result.Success<*> -> doSomething(result.data as List<Area>)
      is Result.Error -> showErrorMessage(result.message)
    }
  }
//...
```

#### API : Mendapatkan Daftar Kabupaten
```kotlin
//...
  GlobalScope.launch {
    val result = WilayahApi.getKabupaten(kodeUnik, provinsiId)
    when(result) {
      is Result.Success<*> -> doSomething(result.data as List<Area>)
      is Result.Error -> showErrorMessage(result.message)
    }
  }
//...
```

#### API : Mendapatkan Daftar Kecamatan
```kotlin
//...
  GlobalScope.launch {
    val result = WilayahApi.getKabupaten(kodeUnik, kabupatenId)
    when(result) {
      is Result.Success<*> -> doSomething(result.data as List<Area>)
      is Result.Error -> showErrorMessage(result.message)
    }
  }
//...
```

#### API : Mendapatkan Daftar Kelurahan
```kotlin
//...
  GlobalScope.launch {
    val result = WilayahApi.getKabupaten(kodeUnik, kecamatanId)
    when(result) {
      is Result.Success<*> -> doSomething(result.data as List<Area>)
      is Result.Error -> showErrorMessage(result.message)
    }
  }
//...
```

## Contribute
I would love to know what you need. Issues and pull requests would be helped.
