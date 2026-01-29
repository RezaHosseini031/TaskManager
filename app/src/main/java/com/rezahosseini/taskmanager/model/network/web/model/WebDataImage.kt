package com.rezahosseini.taskmanager.model.network.web.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WebDataImage(
    @SerializedName("id")
    var id:Long,
    @SerializedName("uriImage")
    var uriImage:String
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebDataImage

        if (id != other.id) return false
        if (uriImage != other.uriImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uriImage.hashCode()
        return result
    }

    override fun toString(): String {
        return "WebServiceDataImage(id=$id, uriImage='$uriImage')"
    }
}