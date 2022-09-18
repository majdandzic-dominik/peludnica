package dominikmajdandzic.ferit.hr.peludnica.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "daily", strict = false)
data class Daily(
    @field:ElementList(name = "day", required = false, inline = true)
    @param:ElementList(name = "day", required = false, inline = true)
    var daysList: MutableList<Day>? = null
)
