package com.catastrophic.app.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.catastrophic.app.data.repository.CatRemoteMediator
import com.catastrophic.app.data.source.ApiServiceCat
import com.catastrophic.app.data.source.AppDatabase
import com.catastrophic.app.data.source.CatDao
import com.catastrophic.app.data.source.PreferencesManager
import com.catastrophic.app.ui.base.BaseViewModel
import com.catastrophic.app.ui.navigator.ListCatNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListCatViewModel @Inject constructor(
    database: AppDatabase,
    catDao: CatDao,
    preferencesManager: PreferencesManager,
    apiServiceCat: ApiServiceCat,
    app: Application
) : BaseViewModel<ListCatNavigator>(app) {

    @ExperimentalPagingApi
    val catsFlow = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = CatRemoteMediator(database, catDao, preferencesManager, apiServiceCat)
    ) {
        catDao.catsPagingSource()
    }.flow
        .cachedIn(viewModelScope)

}