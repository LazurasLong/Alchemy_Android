package com.etbrady.alchemy.apis

import com.etbrady.alchemy.models.Class
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import java.util.*

class ClassDeserializer: JsonDeserializer<Class> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Class {
        val root = json as JsonObject

        val name = root.get("name").asString
        val startDate = Date(root.get("start_at").asLong)
        val endDate = Date(root.get("end_at").asLong)
        val locationId = root.get("location_id").asInt
        val staffMembers = root.get("staff_members").asJsonArray
        val staffMember = staffMembers[0].asJsonObject
        val instructorName = staffMember.get("name").asString

        return Class(name, startDate, endDate, locationId, instructorName)
    }

}