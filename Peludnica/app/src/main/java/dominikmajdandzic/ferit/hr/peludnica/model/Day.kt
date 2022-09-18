package dominikmajdandzic.ferit.hr.peludnica.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name="day", strict = false)
data class Day(
    @field:Path("date")
    @field:Text(required = false)
    @param:Path("date")
    @param:Text(required = false)
    var date: String? = null,

    @field:Path("level")
    @field:Text(required = false)
    @param:Path("level")
    @param:Text(required = false)
    var level: String? = null,

    @field:Path("value")
    @field:Text(required = false)
    @param:Path("value")
    @param:Text(required = false)
    var value: String? = null,

    @field:Path("type")
    @field:Text(required = false)
    @param:Path("type")
    @param:Text(required = false)
    var type: String? = null,

)
