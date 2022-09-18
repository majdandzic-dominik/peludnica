package dominikmajdandzic.ferit.hr.peludnica.retrofit

import dominikmajdandzic.ferit.hr.peludnica.constants.ConstantsHolder
import dominikmajdandzic.ferit.hr.peludnica.listeners.RequestCitiesListener
import dominikmajdandzic.ferit.hr.peludnica.listeners.RequestPlantDataListener
import dominikmajdandzic.ferit.hr.peludnica.model.Cities
import dominikmajdandzic.ferit.hr.peludnica.model.City
import dominikmajdandzic.ferit.hr.peludnica.model.Measure
import dominikmajdandzic.ferit.hr.peludnica.model.Plant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object RetrofitInstance {

    var cityList = mutableListOf<City>()
    var cityNamesList = mutableListOf<String>()
    var plantDataList = mutableListOf<Plant>()
    var plantNamesList = mutableListOf<String>()
    lateinit var requestCitiesListener: RequestCitiesListener
    lateinit var requestPlantDataListener: RequestPlantDataListener

    fun setListeners(
        citiesListener: RequestCitiesListener,
        plantDataListener: RequestPlantDataListener
    ) {
        requestCitiesListener = citiesListener
        requestPlantDataListener = plantDataListener
    }

    fun loadCityList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        val pollenForecastAPI: PollenForecastAPI = retrofit.create(PollenForecastAPI::class.java)
        val citiesData = pollenForecastAPI.getCities()
        citiesData.enqueue(object : Callback<Cities?> {
            override fun onResponse(call: Call<Cities?>?, response: Response<Cities?>?) {
                cityList.clear()
                cityNamesList.clear()
                val tempCityList = response?.body()?.citiesList!!
                for (city in tempCityList) {
                    if (ConstantsHolder.VALID_CITIES_LIST.contains(city.name)) {
                        cityList.add(city)
                        cityNamesList.add(city.name.toString())
                    }
                }
                requestCitiesListener.onCitiesRequested(true, "")
            }

            override fun onFailure(call: Call<Cities?>?, t: Throwable?) {
                requestCitiesListener.onCitiesRequested(
                    false,
                    ConstantsHolder.CITIES_RESPONSE_FAIL_ERR_MSG
                )
            }
        })
    }

    fun loadPollenData(url: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        val pollenForecastAPI: PollenForecastAPI = retrofit.create(PollenForecastAPI::class.java)
        val plantData = pollenForecastAPI.getMeasure(url)

        plantData.enqueue(object : Callback<Measure?> {
            override fun onResponse(call: Call<Measure?>?, response: Response<Measure?>?) {
                plantDataList.clear()
                plantNamesList.clear()
                val tempPlantDataList = response?.body()?.plantsList!!
                for (plantData in tempPlantDataList) {
                    plantDataList.add(plantData)
                    plantNamesList.add(plantData.name.toString())
                }

                requestPlantDataListener.onPlantDataRequested(true, "")
            }

            override fun onFailure(call: Call<Measure?>?, t: Throwable?) {
                requestPlantDataListener.onPlantDataRequested(
                    false,
                    ConstantsHolder.POLLEN_DATA_RESPONSE_FAIL_ERR_MSG
                )
            }
        })
    }

}