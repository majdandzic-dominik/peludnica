package dominikmajdandzic.ferit.hr.peludnica.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name="plant", strict = false)
data class Plant(
    @field:Element(name = "name", required = false)
    @param:Element(name = "name", required = false)
    var name: String? = null,

    @field:Element(name = "daily", required = false)
    @param:Element(name = "daily", required = false)
    var daily: Daily? = null
    )
