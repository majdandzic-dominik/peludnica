package dominikmajdandzic.ferit.hr.peludnica.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name="measure", strict = false)
data class Measure(

    @field:ElementList(name = "plant", required = false, inline = true)
    @param:ElementList(name = "plant", required = false, inline = true)
    var plantsList: MutableList<Plant>? = null
)
