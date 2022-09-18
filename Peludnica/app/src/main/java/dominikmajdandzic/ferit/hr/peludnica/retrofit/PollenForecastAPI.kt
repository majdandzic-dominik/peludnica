package dominikmajdandzic.ferit.hr.peludnica.retrofit


import dominikmajdandzic.ferit.hr.peludnica.model.Cities
import dominikmajdandzic.ferit.hr.peludnica.model.Measure
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface PollenForecastAPI {
    @GET("https://www.plivazdravlje.hr/alergije/prognoza?xml2")
    fun getCities(): Call<Cities>

    @GET
    fun getMeasure(@Url url: String): Call<Measure>

}