package bit.linux.tinyspacex

import org.json.JSONArray
import org.json.JSONObject

class Content {

    var rocketName: String? = null
    var description: String? = null
    var imagesUrl : MutableList<String> = arrayListOf()
    var imageFront : String? = null
    var country: String? = null
    var rocketType: String? = null
    var stages: String? = null
    var costPerLaunch: Long? = null

    constructor(
        rocketName: String?,
        description: String?,
        country: String?,
        rocketType: String?,
        stages: String?,
        costPerLaunch: Long?
    ) {
        this.rocketName = rocketName
        this.description = description
        this.country = country
        this.rocketType = rocketType
        this.stages = stages
        this.costPerLaunch = costPerLaunch
    }

    constructor() {

    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("rocket_name", rocketName)
        jsonObject.put("description", description)
        jsonObject.put("stages", stages)
        jsonObject.put("country", country)
        jsonObject.put("cost_per_launch", costPerLaunch)

        // meter um array em json
        var jsonArray = JSONArray(imagesUrl)
        jsonObject.put("flickr_images", jsonArray)

        return  jsonObject
    }

    companion object {
        fun fromJSon(jsonObject: JSONObject) : Content{
            val content: Content = Content()
            content.rocketName = jsonObject.getString("rocket_name")
            content.description = jsonObject.getString("description")
            content.costPerLaunch = jsonObject.getLong("cost_per_launch")
            content.country = jsonObject.getString("country")
            content.stages = jsonObject.getString("stages")


            var images = jsonObject.getJSONArray("flickr_images")
            for (index in 0 until images.length()){
                content.imagesUrl.add(images[index].toString())
            }
            content.imageFront = content.imagesUrl[0]

            return content
        }
    }
}