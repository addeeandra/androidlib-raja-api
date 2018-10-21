package me.inibukanadit.rajaapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import me.inibukanadit.rajaapi.wilayah.Result
import me.inibukanadit.rajaapi.wilayah.WilayahApiCoroutineService
import me.inibukanadit.rajaapi.wilayah.model.*

class MainActivity : AppCompatActivity() {

    private lateinit var mUniqueCode: String

    private val mAdapterProvince: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
    }
    private val mAdapterCity: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
    }
    private val mAdapterDistrict: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
    }
    private val mAdapterVilage: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
    }

    private var mProvinceList = mutableListOf<Area>()
    private var mCityList = mutableListOf<Area>()
    private var mDistrictList = mutableListOf<Area>()
    private var mVillageList = mutableListOf<Area>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUniqueCode()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initUniqueCode() {
        GlobalScope.launch(Dispatchers.Main) {
            val result = WilayahApiCoroutineService.getKodeUnik()
            when (result) {
                is Result.Success<*> -> {
                    mUniqueCode = result.data as String
                    loadProvinces(mUniqueCode)
                }
                is Result.Error -> showMessage(result.message)
            }
        }
    }

    suspend fun loadProvinces(uniqueCode: String) {
        val result = WilayahApiCoroutineService.getProvinsi(uniqueCode)
        when (result) {
            is Result.Success<*> -> {
                val data = result.data as List<Area>
                showProvinces(data)
            }
            is Result.Error -> showMessage(result.message)
        }
    }

    suspend fun loadCities(uniqueCode: String, provinceId: Int) {
        val result = WilayahApiCoroutineService.getKabupaten(uniqueCode, provinceId)
        when (result) {
            is Result.Success<*> -> {
                val data = result.data as List<Area>
                showCities(data)
            }
            is Result.Error -> showMessage(result.message)
        }
    }

    suspend fun loadDistricts(uniqueCode: String, cityId: Int) {
        val result = WilayahApiCoroutineService.getKecamatan(uniqueCode, cityId)
        when (result) {
            is Result.Success<*> -> {
                val data = result.data as List<Area>
                showDistricts(data)
            }
            is Result.Error -> showMessage(result.message)
        }
    }

    suspend fun loadVillages(uniqueCode: String, districtId: Int) {
        val result = WilayahApiCoroutineService.getKelurahan(uniqueCode, districtId)
        when (result) {
            is Result.Success<*> -> {
                val data = result.data as List<Area>
                showVillages(data)
            }
            is Result.Error -> showMessage(result.message)
        }
    }

    fun showProvinces(provinces: List<Area>) {
        mProvinceList = provinces.toMutableList()
        showData(sp_province, mAdapterProvince, mProvinceList)

        sp_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                GlobalScope.launch(Dispatchers.Main) {
                    loadCities(mUniqueCode, mProvinceList[position].id)
                }
                clearCities()
            }
        }
    }

    fun showCities(cities: List<Area>) {
        mCityList = cities.toMutableList()
        showData(sp_city, mAdapterCity, mCityList)

        sp_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                GlobalScope.launch(Dispatchers.Main) { loadDistricts(mUniqueCode, mCityList[position].id) }
                clearDistricts()
            }
        }
    }

    fun showDistricts(districts: List<Area>) {
        mDistrictList = districts.toMutableList()
        showData(sp_district, mAdapterDistrict, mDistrictList)

        sp_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                GlobalScope.launch(Dispatchers.Main) { loadVillages(mUniqueCode, mDistrictList[position].id) }
                clearVillages()
            }
        }
    }

    fun showVillages(villages: List<Area>) {
        mVillageList = villages.toMutableList()
        showData(sp_village, mAdapterVilage, mVillageList)

        sp_village.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
    }

    fun showData(spinner: Spinner, adapter: ArrayAdapter<String>, areas: List<Area>) {
        spinner.adapter = adapter.apply {
            clear()
            addAll(areas.map { it.name })
        }
    }

    fun clearProvinces() {
        showProvinces(listOf())
        clearCities()
    }

    fun clearCities() {
        showCities(listOf())
        clearDistricts()
    }

    fun clearDistricts() {
        showDistricts(listOf())
        clearVillages()
    }

    fun clearVillages() {
        showVillages(listOf())
    }


}
