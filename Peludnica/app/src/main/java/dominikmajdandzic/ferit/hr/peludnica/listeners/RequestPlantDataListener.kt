package dominikmajdandzic.ferit.hr.peludnica.listeners

interface RequestPlantDataListener {
    fun onPlantDataRequested(isSuccess: Boolean, msg: String)
}