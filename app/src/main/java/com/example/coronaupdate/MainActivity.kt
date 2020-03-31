package com.example.coronaupdate

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coronaupdate.retrofit.ApiClient
import com.example.coronaupdate.retrofit.ApiInterface
import com.example.coronaupdate.retrofit.structures.CoronaResponse

class MainActivity : AppCompatActivity() {
    private lateinit var tvReported: TextView;
    private lateinit var tvTitle: TextView;
    private lateinit var tvRecovered: TextView;
    private lateinit var tvDeaths: TextView;
    private lateinit var searchView: SearchView;
    private lateinit var progress: ProgressBar;
    private lateinit var selectedCountry: String;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        tvReported = findViewById(R.id.tvReported)
        tvTitle = findViewById(R.id.tvRHeaders)
        tvDeaths = findViewById(R.id.tvDeaths)
        tvRecovered = findViewById(R.id.tvRecovered)
        searchView = findViewById(R.id.searchView)
        progress = findViewById(R.id.progressBar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                startSearchAction(query)
                return false
            }

        })
    }

    private fun startSearchAction(country: String) {
        progress.visibility = View.VISIBLE;
        //get retrofit client instance
        var client: ApiClient = ApiClient()

        //Create Service
        val apiService = client.getClient()!!.create(ApiInterface::class.java)

        // Create Call
        val call: retrofit2.Call<CoronaResponse> = apiService.getCoronaUpdates(country.trim())

        // Observe Call
        call.enqueue(object : retrofit2.Callback<CoronaResponse> {
            override fun onFailure(call: retrofit2.Call<CoronaResponse>?, t: Throwable?) {
                progress.visibility = View.INVISIBLE;
                val errrorMsg = t!!.localizedMessage;
                showError(errrorMsg)
            }

            override fun onResponse(
                call: retrofit2.Call<CoronaResponse>?,
                response: retrofit2.Response<CoronaResponse>?
            ) {
                progress.visibility = View.INVISIBLE;
                if (response!!.isSuccessful) {
                    selectedCountry = country;
                    val result = (response.body()!!);
                    if (result!!.message!!.contains("Country not found")) {
                        showError("Country not found. Please Ensure the first letter is in Capital")
                    } else {
                        if (result!!.data!!.covidStatsList!!.size > 1) {
                            showError("Country Has Updates Per Province. We are currently able to show for whole country")
                        } else {
                            setCovidDataOnViews(result)
                        }
                    }

                } else {
                    showError("Error Finding Updates. Please Try Again Later")
                }
            }

        })

    }

    private fun showError(message: String) {
        val error: String;
        if (message.contains("Unable to resolve host")) {
            error = "Please Ensure Your Data is On"
        } else {
            error = message;
        }
        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show();
    }

    private fun setCovidDataOnViews(result: CoronaResponse) {
        tvReported.visibility = View.VISIBLE
        tvTitle.text = "Confirmed Cases in $selectedCountry"
        tvReported.text = "${result.data!!.covidStatsList!![0].confirmed}"
        tvRecovered.text =
            "Recovered : ${result.data!!.covidStatsList!![0].recovered}"
        tvDeaths.text = "Deaths : ${result.data!!.covidStatsList!![0].deaths}"
    }
}
