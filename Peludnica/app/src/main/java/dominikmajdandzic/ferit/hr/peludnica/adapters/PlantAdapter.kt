package dominikmajdandzic.ferit.hr.peludnica.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dominikmajdandzic.ferit.hr.peludnica.R
import dominikmajdandzic.ferit.hr.peludnica.constants.ConstantsHolder
import dominikmajdandzic.ferit.hr.peludnica.databinding.PollenItemBinding
import dominikmajdandzic.ferit.hr.peludnica.model.Plant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlantAdapter(private val plantDataList: MutableList<Plant>) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pollen_item, parent, false)
        return PlantViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantDataList[position]
        holder.bind(plant)
    }

    override fun getItemCount(): Int {
        return plantDataList.size
    }

    class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(plant: Plant){
            val itemBinding = PollenItemBinding.bind(itemView)
            itemBinding.tvPollenName.text = plant.name

            itemBinding.tvTodayDate.text = getChangedDateFormat(plant.daily?.daysList!![0].date)
            itemBinding.tvTodayLevel.text = plant.daily?.daysList!![0].level
            setColorFromLevel(itemBinding.tvTodayLevel)

            itemBinding.tvTomorrowDate.text = getChangedDateFormat(plant.daily?.daysList!![1].date)
            itemBinding.tvTomorrowLevel.text = plant.daily?.daysList!![1].level
            setColorFromLevel(itemBinding.tvTomorrowLevel)

            itemBinding.tvOvermorrowDate.text = getChangedDateFormat(plant.daily?.daysList!![2].date)
            itemBinding.tvOvermorrowLevel.text = plant.daily?.daysList!![2].level
            setColorFromLevel(itemBinding.tvOvermorrowLevel)
        }
        private fun setColorFromLevel(levelTextView: TextView){
            when (levelTextView.text) {
                ConstantsHolder.POLLEN_COUNT_LOW -> {
                    levelTextView.setTextColor(Color.parseColor("#00FF00"))
                }
                ConstantsHolder.POLLEN_COUNT_MED -> {
                    levelTextView.setTextColor(Color.parseColor("#FAFF00"))
                }
                ConstantsHolder.POLLEN_COUNT_HIGH -> {
                    levelTextView.setTextColor(Color.parseColor("#FF0000"))
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getChangedDateFormat(date: String?): String {
            val startDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            return startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }


}