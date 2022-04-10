package com.catastrophic.app.data.source


import androidx.paging.PagingSource
import androidx.room.*
import com.catastrophic.app.data.model.Cat

@Dao
interface CatDao {
    @Query("SELECT * FROM cat")
    fun catsPagingSource(): PagingSource<Int,Cat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCat(cats: List<Cat>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun appendCats(cats: List<Cat>?)

    @Update(entity = Cat::class)
    fun updateImageBlob(cat: Cat)

    @Query("DELETE from cat")
    fun clearCats()
}