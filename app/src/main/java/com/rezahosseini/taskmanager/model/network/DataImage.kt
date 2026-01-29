package com.rezahosseini.taskmanager.model.network

class DataImage(
    public var id:Long,
    public var uriImage:String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataImage

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
        return "DataImage(id=$id, uriImage='$uriImage')"
    }

}