package dominikmajdandzic.ferit.hr.peludnica.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name="cities", strict = false)
data class Cities(

    @field:ElementList(name = "city", required = false, inline = true)
    @param:ElementList(name = "city", required = false, inline = true)
    var citiesList: MutableList<City>? = null
)
