/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.dessertpusher

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.android.dessertpusher.databinding.ActivityMainBinding
import timber.log.Timber


const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private var revenue = 0
    private var dessertsSold = 0


    private var timer = 0
    private var handler = Handler()
    private lateinit var runnable: Runnable

    private lateinit var allDesserts: List<Dessert>
    private lateinit var currentDessert: Dessert

    // Views
    private lateinit var dessertButton: ImageButton
    private lateinit var numberSoldTextView: TextView
    private lateinit var revenueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate Called")

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        allDesserts = listOf(
                Dessert(R.drawable.ic_cupcake, 5, 0),
                Dessert(R.drawable.ic_donut, 10, 5),
                Dessert(R.drawable.ic_eclair, 15, 20),
                Dessert(R.drawable.ic_froyo, 30, 50),
                Dessert(R.drawable.ic_gingerbread, 50, 100),
                Dessert(R.drawable.ic_honeycomb, 100, 200),
                Dessert(R.drawable.ic_icecreamsandwich, 500, 500),
                Dessert(R.drawable.ic_jellybean, 1000, 1000),
                Dessert(R.drawable.ic_kitkat, 2000, 2000),
                Dessert(R.drawable.ic_lollipop, 3000, 4000),
                Dessert(R.drawable.ic_marshmallow, 4000, 8000),
                Dessert(R.drawable.ic_nougat, 5000, 16000),
                Dessert(R.drawable.ic_oreo, 6000, 20000)
        )

        numberSoldTextView = binding.numberSold
        revenueTextView = binding.numberRevenue
        dessertButton = binding.clickerButton

        dessertButton.setOnClickListener {
            onDessertClicked()
        }

        if (savedInstanceState != null) {
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            numberSoldTextView.text = revenue.toString()
            revenueTextView.text = dessertsSold.toString()
        }

        currentDessert = allDesserts[allDesserts.lastIndex]
        showCurrentDessert()
        lifecycle.addObserver(this)
    }

    private fun onDessertClicked() {
        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        revenueTextView.text = revenue.toString()
        numberSoldTextView.text = dessertsSold.toString()

        // Show the next dessert
        showCurrentDessert()
    }


    private fun showCurrentDessert() {
        var newDessert: Dessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount)
                newDessert = dessert
            else break
        }
        if ( newDessert != currentDessert ) {
            currentDessert = newDessert
            dessertButton.setImageResource(newDessert.imageId)
        }
    }

    private fun onShare() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, dessertsSold, revenue))
        shareIntent.type = "text/plain"
        startActivity(shareIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    /** Lifecycle Observation **/

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleEvent() {
        Timber.d("The Lifecycle State is ${lifecycle.currentState}")
    }

    /** Lifecycle Methods **/

    override fun onStart() {
        super.onStart()
        Timber.d("onStart Called")
        //setUpClock()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop Called")
        //tearDownClock()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart Called")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(KEY_REVENUE, revenue)
        outState?.putInt(KEY_DESSERT_SOLD, dessertsSold)
        Timber.d("onSaveInstanceState Called")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.d("onRestoreInstanceState Called")
    }

    /** Dessert data class **/
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)
}
