package com.catastrophic.app.data.source

import androidx.room.TypeConverter
import com.catastrophic.app.data.model.Cat
import com.google.gson.Gson

class RoomConverter {
    @TypeConverter
    fun listCatToJson(value: List<Cat>): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToListCat(value: String) =
        Gson().fromJson(value, Array<Cat>::class.java)
            .toList()

    @TypeConverter
    fun listBreedToJson(value: List<Cat.Breed?>): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToListBreed(value: String) =
        Gson().fromJson(value, Array<Cat.Breed?>::class.java)
            .toList()

    @TypeConverter
    fun listCategoryToJson(value: List<Cat.Category>?): String =
        Gson().toJson(value ?: ArrayList<Cat.Category>())

    @TypeConverter
    fun jsonToListCategory(value: String?) =
        Gson().fromJson(value ?: "[]", Array<Cat.Category>::class.java)
            .toList()
}