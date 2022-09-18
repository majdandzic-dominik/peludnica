package dominikmajdandzic.ferit.hr.peludnica.listeners

interface RequestCitiesListener {
    fun onCitiesRequested(isSuccess: Boolean, msg: String)
}