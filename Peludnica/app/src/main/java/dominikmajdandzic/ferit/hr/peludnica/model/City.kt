package dominikmajdandzic.ferit.hr.peludnica.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name="city", strict = false)
data class City(
    @field:Element(name = "name")
    @param:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "link")
    @param:Element(name = "link")
    var link: String? = null
)
