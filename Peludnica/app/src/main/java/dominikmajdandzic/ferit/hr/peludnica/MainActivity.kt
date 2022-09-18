package dominikmajdandzic.ferit.hr.peludnica

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dominikmajdandzic.ferit.hr.peludnica.adapters.PlantAdapter
import dominikmajdandzic.ferit.hr.peludnica.constants.ConstantsHolder
import dominikmajdandzic.ferit.hr.peludnica.databinding.ActivityMainBinding
import dominikmajdandzic.ferit.hr.peludnica.listeners.RequestCitiesListener
import dominikmajdandzic.ferit.hr.peludnica.listeners.RequestPlantDataListener
import dominikmajdandzic.ferit.hr.peludnica.notification.AlarmReceiver
import dominikmajdandzic.ferit.hr.peludnica.notification.InternetAccessInstance
import dominikmajdandzic.ferit.hr.peludnica.retrofit.RetrofitInstance
import java.util.*


class MainActivity : AppCompatActivity(), RequestCitiesListener, RequestPlantDataListener {

    private lateinit var binding: ActivityMainBinding
    var savedCityString: String = ""
    lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        setAlarm()
        createNotificationChannel()

    }

    private fun setupUI() {
        loadData()
        binding.bRefresh.setOnClickListener {
            refreshView()
        }
        if(InternetAccessInstance.isOnline(this)){
            binding.tvConnectionStatus.visibility = View.INVISIBLE
            binding.bRefresh.visibility = View.INVISIBLE
            binding.bRefresh.isEnabled = false
            RetrofitInstance.setListeners(this, this)
            RetrofitInstance.loadCityList()
        }
        else{
            binding.tvConnectionStatus.visibility = View.VISIBLE
            binding.bRefresh.visibility = View.VISIBLE
            binding.bRefresh.isEnabled = true
        }
    }

    private fun refreshView(){
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemNotifications -> {
                navigateToNotifications()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToNotifications() {
        val intent: Intent = Intent(this, NotificationsActivity::class.java)
        startActivity(intent)
    }

    override fun onCitiesRequested(isSuccess: Boolean, msg: String) {
        if(isSuccess){
            val spinnerAdapter = ArrayAdapter<String>(this,
                R.layout.spinner_selected_item,
                RetrofitInstance.cityNamesList)
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

            binding.spCities.adapter = spinnerAdapter

            if(savedCityString != ""){
                binding.spCities.setSelection(RetrofitInstance.cityNamesList.indexOf(savedCityString))
            }

            binding.spCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    saveData()
                    RetrofitInstance.loadPollenData(RetrofitInstance.cityList?.get(position)?.link.toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
        else{
            Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_SHORT)
        }
    }


    override fun onPlantDataRequested(isSuccess: Boolean, msg: String) {
        if(isSuccess){
            if(RetrofitInstance.plantDataList == null)            {
                Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_SHORT).show()
            }
            else{
                setupRecyclerView()
            }

        }
        else{
            Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(){
        binding.rvPollen.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvPollen.adapter = PlantAdapter(RetrofitInstance.plantDataList)
    }


    private fun saveData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {
            putString(ConstantsHolder.LIST_SELECTED_CITY_KEY, binding.spCities.selectedItem.toString())
        }.apply()
    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        savedCityString = sharedPreferences.getString(ConstantsHolder.LIST_SELECTED_CITY_KEY, "").toString()
    }

    private fun setAlarm() {
        //set time when notification is shown
        var calender: Calendar = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, 8)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val thuReq: Long = Calendar.getInstance().timeInMillis + 1
        var reqReqCode = thuReq.toInt()

        //increment day
        if (calender.timeInMillis < System.currentTimeMillis()) {
            calender.add(Calendar.DAY_OF_YEAR, 1)
        }
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val pendingIntent = PendingIntent.getBroadcast(this, reqReqCode, intent, 0)

        //set interval of repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            24 * 60 * 60 * 1000,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val description = " Pollent notification manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(ConstantsHolder.NOTIFICATION_ID, name, importance)
            notificationChannel.description = description
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}