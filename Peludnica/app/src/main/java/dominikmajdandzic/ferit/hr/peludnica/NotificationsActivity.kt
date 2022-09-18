package dominikmajdandzic.ferit.hr.peludnica

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import dominikmajdandzic.ferit.hr.peludnica.constants.ConstantsHolder
import dominikmajdandzic.ferit.hr.peludnica.databinding.ActivityNotificationsBinding


class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding

    private lateinit var cityAdapter: ArrayAdapter<String>
    lateinit var plantAdapter: ArrayAdapter<String>
    var cityIndex: Int = -1
    var plantIndex: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Obavijesti"

        loadData()

        setUpCitiesList()
        setUpPlantList()
        setUpCheckbox()
    }

    private fun setUpCheckbox() {
        val isChecked = binding.cbNotificationToggle.isChecked
        binding.tilCity.isEnabled = isChecked
        binding.tilPlant.isEnabled = isChecked
        binding.acCity.isEnabled = isChecked
        binding.acPlant.isEnabled = isChecked

        binding.cbNotificationToggle.setOnCheckedChangeListener{
                _, isChecked ->
            binding.tilCity.isEnabled = isChecked
            binding.tilPlant.isEnabled = isChecked
            binding.acCity.isEnabled = isChecked
            binding.acPlant.isEnabled = isChecked
            if(isChecked){
                Toast.makeText(applicationContext, "Obavijesti su UKLJUČENE", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext, "Obavijesti su ISKLJUČENE", Toast.LENGTH_SHORT).show()
            }
            saveData()
        }
    }

    private fun setUpCitiesList(){
        cityAdapter = ArrayAdapter<String>(this, R.layout.list_item, ConstantsHolder.VALID_CITIES_LIST)

        binding.acCity.setAdapter(cityAdapter)
        if(cityIndex >= 0){
            binding.acCity.setText(ConstantsHolder.VALID_CITIES_LIST[cityIndex], false)
        }

        binding.acCity.onItemClickListener = AdapterView
            .OnItemClickListener{
                    parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position)
                    .toString()
                // Display the clicked item using toast
                Toast.makeText(applicationContext, "Odabrali ste: $selectedItem", Toast.LENGTH_SHORT).show()

                saveData()
            }
    }

    private fun setUpPlantList(){
        plantAdapter = ArrayAdapter<String>(this, R.layout.list_item, ConstantsHolder.ALLERGEN_PLANTS_LIST)

        binding.acPlant.setAdapter(plantAdapter)
        if(plantIndex >= 0){
            binding.acPlant.setText(ConstantsHolder.ALLERGEN_PLANTS_LIST[plantIndex], false)
        }

        binding.acPlant.onItemClickListener = AdapterView
            .OnItemClickListener{
                    parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position)
                    .toString()
                // Display the clicked item using toast
                Toast.makeText(applicationContext, "Odabrali ste: $selectedItem", Toast.LENGTH_SHORT).show()

                saveData()
            }
    }

    private fun saveData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {
            putBoolean(ConstantsHolder.NOTIFICATION_CHECK_KEY, binding.cbNotificationToggle.isChecked)
            putString(ConstantsHolder.NOTIFICATION_SELECTED_CITY_KEY, binding.acCity.text.toString())
            putString(ConstantsHolder.NOTIFICATION_SELECTED_POLANT_KEY, binding.acPlant.text.toString())
        }.apply()
    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val savedNotificationCheck = sharedPreferences.getBoolean(ConstantsHolder.NOTIFICATION_CHECK_KEY, false)
        val savedCityString = sharedPreferences.getString(ConstantsHolder.NOTIFICATION_SELECTED_CITY_KEY, "")
        val savedPollenString = sharedPreferences.getString(ConstantsHolder.NOTIFICATION_SELECTED_POLANT_KEY, "")

        binding.cbNotificationToggle.isChecked = savedNotificationCheck
        cityIndex = ConstantsHolder.VALID_CITIES_LIST.indexOf(savedCityString)
        plantIndex = ConstantsHolder.ALLERGEN_PLANTS_LIST.indexOf(savedPollenString)
    }



}