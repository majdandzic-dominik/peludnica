package dominikmajdandzic.ferit.hr.peludnica.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dominikmajdandzic.ferit.hr.peludnica.MainActivity
import dominikmajdandzic.ferit.hr.peludnica.R
import dominikmajdandzic.ferit.hr.peludnica.constants.ConstantsHolder
import dominikmajdandzic.ferit.hr.peludnica.listeners.RequestCitiesListener
import dominikmajdandzic.ferit.hr.peludnica.listeners.RequestPlantDataListener
import dominikmajdandzic.ferit.hr.peludnica.model.Day
import dominikmajdandzic.ferit.hr.peludnica.model.Plant
import dominikmajdandzic.ferit.hr.peludnica.retrofit.RetrofitInstance

class AlarmReceiver() : BroadcastReceiver(), RequestCitiesListener, RequestPlantDataListener {

    var notificationsEnabled: Boolean = false
    lateinit var savedCity: String
    lateinit var savedPlant: String
    lateinit var plantData: Plant
    lateinit var currentContext: Context
    lateinit var currentIntent: Intent
    lateinit var plantDataToday: Day

    override fun onReceive(context: Context?, intent: Intent?) {
        currentContext = context!!
        currentIntent = intent!!

        //get notification settings from sharedprefs
        val sharedPreferences = context!!.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        notificationsEnabled =
            sharedPreferences.getBoolean(ConstantsHolder.NOTIFICATION_CHECK_KEY, false)
        savedCity =
            sharedPreferences.getString(ConstantsHolder.NOTIFICATION_SELECTED_CITY_KEY, "")
                .toString()
        savedPlant =
            sharedPreferences.getString(ConstantsHolder.NOTIFICATION_SELECTED_POLANT_KEY, "")
                .toString()

        //notifications enabled
        if (notificationsEnabled && savedCity != "" && savedPlant != "") {
            //internet available
            if (InternetAccessInstance.isOnline(context!!)) {
                RetrofitInstance.setListeners(this, this)
                RetrofitInstance.loadCityList()
            }
        }


    }

    private fun buildNotification(context: Context?, intent: Intent?) {
        //oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val builder =
                NotificationCompat.Builder(context!!, ConstantsHolder.NOTIFICATION_ID)
                    .setContentTitle("PELUDNA PROGRNOZA ZA GRAD: " + savedCity.uppercase())
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setContentText("OPREZ!\n" + plantDataToday.level!!.uppercase() +  " razina peludi za biljku: " + plantData.name!!.uppercase())
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)


            val notificationManagerCompat = NotificationManagerCompat.from(context!!)
            notificationManagerCompat.notify(123, builder.build())
        }
    }

    override fun onCitiesRequested(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            val index = RetrofitInstance.cityNamesList.indexOf(savedCity)
            RetrofitInstance.loadPollenData(RetrofitInstance.cityList?.get(index)?.link.toString())
        } else {
            Log.d("NOTIFICATION_GET_CITY", "Failed to get cities for notification")
        }
    }

    override fun onPlantDataRequested(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            val index = RetrofitInstance.plantNamesList.indexOf(savedPlant)
            //check that saved plant is in todays data
            if (index != -1) {
                plantData = RetrofitInstance.plantDataList[index]
                plantDataToday = plantData.daily?.daysList!![0]

                //if selected pollent count is high or medium
                if(plantDataToday.level != ConstantsHolder.POLLEN_COUNT_LOW){
                    buildNotification(currentContext, currentIntent)
                }
            }
        } else {
            Log.d("NOTIFICATION_GET_PLANT", "Failed to get plant for notification")
        }
    }
}