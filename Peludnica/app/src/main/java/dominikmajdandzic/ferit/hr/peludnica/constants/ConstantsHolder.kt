package dominikmajdandzic.ferit.hr.peludnica.constants

object ConstantsHolder {
    val VALID_CITIES_LIST = listOf<String>("Dubrovnik", "Osijek", "Pula", "Split", "Virovitica", "Zadar", "Zagreb")

    val ALLERGEN_PLANTS_LIST = listOf<String>("Borovi", "Breza", "Brijest", "Čempresi",
        "Grab", "Hrast", "Hrast crnika", "Jasen",
        "Joha", "Lijeska", "Lipa", "Maslina",
        "Orah", "Pitomi kesten", "Platana", "Topola",
        "Vrba", "Trave", "Ambrozija", "Crkvina",
        "Kiselica", "Koprive", "Loboda", "Pelin",
        "Trputac")


    const val CITIES_RESPONSE_FAIL_ERR_MSG = "Dohvaćaje gradova nije uspješno."
    const val POLLEN_DATA_RESPONSE_FAIL_ERR_MSG = "Dohvaćanje peludne prognoze nije moguće"

    const val POLLEN_COUNT_HIGH = "visoka"
    const val POLLEN_COUNT_MED = "umjerena"
    const val POLLEN_COUNT_LOW = "niska"

    const val NOTIFICATION_SELECTED_CITY_KEY = "NOTIFICATION_SELECTED_CITY"
    const val NOTIFICATION_SELECTED_POLANT_KEY = "NOTIFICATION_SELECTED_PLANT"
    const val NOTIFICATION_CHECK_KEY = "NOTIFICATION_CHECK"

    const val LIST_SELECTED_CITY_KEY = "LIST_SELECTED_CITY"

    const val NOTIFICATION_ID = "NOTIFICATION_ID"

}