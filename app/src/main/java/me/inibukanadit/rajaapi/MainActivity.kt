package me.inibukanadit.rajaapi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import me.inibukanadit.rajaapi.wilayah.*
import me.inibukanadit.rajaapi.wilayah.model.Area

class MainActivity : AppCompatActivity() {

    private lateinit var mUniqueCode: String

    private val mWilayahApiCoroutineService by lazy { WilayahApiCoroutineService.instance() }
    private val mWilayahApiAsyncService by lazy { WilayahApiAsyncService.instance() }

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
            val result = mWilayahApiCoroutineService.getKodeUnik().await()
            val code = WilayahApi.getUniqueCode(result)

            if (code != null) {
                mUniqueCode = code
                loadProvinces(code)
            } else {
                showMessage(WilayahApi.getDataMessage(result))
            }
        }
    }

    private fun loadProvinces(uniqueCode: String) {
        // using async callback
        mWilayahApiAsyncService
                .getProvinsi(uniqueCode)
                .execute(object : WilayahApiAsyncWrapper.Callback<List<Area>> {
                    override fun onResult(data: List<Area>?, error: String?) {
                        if (error == null) showProvinces(data as List<Area>)
                        else showMessage(error)
                    }
                })
    }

    private fun loadCities(uniqueCode: String, provinceId: Int) {
        // using async callback
        mWilayahApiAsyncService
                .getKabupaten(uniqueCode, provinceId)
                .execute(object : WilayahApiAsyncWrapper.Callback<List<Area>> {
                    override fun onResult(data: List<Area>?, error: String?) {
                        if (error == null) showCities(data as List<Area>)
                        else showMessage(error)
                    }
                })
    }

    private fun loadDistricts(uniqueCode: String, cityId: Int) {
        // using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val result = mWilayahApiCoroutineService.getKecamatan(uniqueCode, cityId).await()
            val data = WilayahApi.getAreaList(result)

            if (data != null) showDistricts(data)
            else showMessage(WilayahApi.getDataMessage(result))
        }
    }

    private fun loadVillages(uniqueCode: String, districtId: Int) {
        // using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            val result = mWilayahApiCoroutineService.getKelurahan(uniqueCode, districtId).await()
            val data = WilayahApi.getAreaList(result)

            if (data != null) showVillages(data)
            else showMessage(WilayahApi.getDataMessage(result))
        }
    }

    private fun showProvinces(provinces: List<Area>) {
        mProvinceList = provinces.toMutableList()
        showData(sp_province, mAdapterProvince, mProvinceList)

        sp_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadCities(mUniqueCode, mProvinceList[position].id)
                clearCities()
            }
        }
    }

    private fun showCities(cities: List<Area>) {
        mCityList = cities.toMutableList()
        showData(sp_city, mAdapterCity, mCityList)

        sp_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadDistricts(mUniqueCode, mCityList[position].id)
                clearDistricts()
            }
        }
    }

    private fun showDistricts(districts: List<Area>) {
        mDistrictList = districts.toMutableList()
        showData(sp_district, mAdapterDistrict, mDistrictList)

        sp_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadVillages(mUniqueCode, mDistrictList[position].id)
                clearVillages()
            }
        }
    }

    private fun showVillages(villages: List<Area>) {
        mVillageList = villages.toMutableList()
        showData(sp_village, mAdapterVilage, mVillageList)

        sp_village.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
    }

    private fun showData(spinner: Spinner, adapter: ArrayAdapter<String>, areas: List<Area>) {
        spinner.adapter = adapter.apply {
            clear()
            addAll(areas.map { it.name })
        }
    }

    private fun clearCities() {
        showCities(listOf())
        clearDistricts()
    }

    private fun clearDistricts() {
        showDistricts(listOf())
        clearVillages()
    }

    private fun clearVillages() {
        showVillages(listOf())
    }

//    ini cuma komen

}
