package com.catastrophic.app.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.catastrophic.app.data.model.Cat
import com.catastrophic.app.data.source.ApiServiceCat
import com.catastrophic.app.data.source.AppDatabase
import com.catastrophic.app.data.source.CatDao
import com.catastrophic.app.data.source.PreferencesManager
import com.catastrophic.app.utils.getByteImageFromURL
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.util.*


@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator (
    private val database: AppDatabase,
    private val catDao: CatDao,
    private val preferencesManager: PreferencesManager,
    private val apiServiceCat: ApiServiceCat
) : RemoteMediator<Int, Cat>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Cat>
    ): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.

            val loadKey = when (loadType) {
                LoadType.REFRESH -> {

                    preferencesManager.prefRemoteKey = 1
                    preferencesManager.prefRemoteKey
                }
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

                    if(preferencesManager.prefRemoteKey == null) {
                        preferencesManager.prefRemoteKey = 1
                    }
                    preferencesManager.prefRemoteKey
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = apiServiceCat.getCatsImageAsync(page = loadKey ?: 1).await()


            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    catDao.clearCats()
                    preferencesManager.prefRemoteKey = 1
                }
                preferencesManager.prefRemoteKey = loadKey?.plus(1)
                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                for(i in response.indices){
                    coroutineScope {
                      val bytes =   getByteImageFromURL(response[i].url).await()
                        response[i].image_blob = bytes
                    }
                }
                catDao.insertAllCat(response)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.isEmpty()
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}