package com.example.quotes

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.quotes.API.ApiController
import com.example.quotes.API.ApiInterface
import com.example.quotes.RCV_Classes.QuotesAdapter
import com.example.quotes.RCV_Classes.QuotesModel
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*

class MainActivity : AppCompatActivity() {
    private var progressBar: KProgressHUD? = null
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Checking Internet Connection */
        if (!internet()) {
            showSnackbar()
        } else {
            showProgressBar()
            getApiData()
        }

        swipeRefreshlayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            /** Checking Internet Connection */
            if (internet()) {
                getApiData()
                snackbar?.dismiss()
            } else {
                getApiData()
                swipeRefreshlayout.isRefreshing = false
                showSnackbar()
            }
        })

    }

    private fun getApiData() {
        val retrofit = ApiController().retrofit
        val apiInterface = retrofit.create(ApiInterface::class.java)

        apiInterface.getData().enqueue(object : Callback<List<QuotesModel>> {
            override fun onResponse(
                call: Call<List<QuotesModel>>,
                response: Response<List<QuotesModel>>
            ) {
                if (response.isSuccessful) {
                    /** Setting data on Recycler View */
                    progressBar?.dismiss()
                    swipeRefreshlayout.isRefreshing = false

                    val adapter = response.body()?.let { QuotesAdapter(this@MainActivity, it) }
                    quotesRCV.setHasFixedSize(true)
                    quotesRCV.layoutManager =
                        StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                    quotesRCV.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<QuotesModel>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error : No Internet Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun internet(): Boolean {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val connManager =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnectedOrConnecting
            }
        }
        return false
    }

    private fun showSnackbar() {
        snackbar = Snackbar.make(parentLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)

        snackbar?.setAction("Retry?"){
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        }
        snackbar?.show()
    }

    private fun showProgressBar() {
        progressBar = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
//            .setDetailsLabel("Loading...")
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
    }

}