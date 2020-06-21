package com.example.android.dessertpusher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.dessertpusher.database.DataClass
import com.example.android.dessertpusher.database.DatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class MainViewModel(private val dataSource: DatabaseDao) : ViewModel() {

    private var latestPoints: DataClass? = null

    var revenue = latestPoints?.dataRevenue ?: 0
    var dessertsSold = latestPoints?.dataAmountSold ?: 0

    private val viewModelJob = Job()
    private val uiLauncher = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getData()
    }

    fun insertData(){
        uiLauncher.launch {
            withContext(Dispatchers.IO){
                val oldData = DataClass(dataRevenue = revenue, dataAmountSold = dessertsSold)
                dataSource.insertPoints(oldData)
                Timber.i("Inserted data. Revenue = ${oldData.dataRevenue}, dessertSold = ${oldData.dataAmountSold}")
            }
        }
    }

    fun getData(){
        uiLauncher.launch {
                latestPoints = getSuspendData()
                revenue = latestPoints?.dataRevenue ?: 0
                dessertsSold = latestPoints?.dataAmountSold ?: 0
                Timber.i("Get Data. Revenue = $revenue and latestPoints.revenue = ${latestPoints?.dataRevenue}. DessertSold = $dessertsSold and latestPoints.dessertsSold = ${latestPoints?.dataAmountSold}")
        }
    }

    private suspend fun getSuspendData(): DataClass?{
        return withContext(Dispatchers.IO){
            dataSource.getKcal()
        }
    }

    fun clearData(){
        uiLauncher.launch{
            withContext(Dispatchers.IO){
                dataSource.clearDatabase()
            }
        }
    }
}