package com.example.coronaupdate.retrofit.structures

import com.google.gson.annotations.SerializedName

class CoronaResponse {
    @SerializedName("error")
    var error: Boolean? = null;

    @SerializedName("statusCode")
    var statusCode: Int? = null;

    @SerializedName("message")
    var message: String? = null;

    @SerializedName("data")
    val data: CoronaData? = null

    class CoronaData {
        @SerializedName("lastChecked")
        var lastChecked: String? = null;

        @SerializedName("covid19Stats")
        var covidStatsList: List<CovidStat>? = null;

        class CovidStat {
            @SerializedName("country")
            var country: String? = null;

            @SerializedName("lastUpdate")
            var lastUpdate: String? = null;

            @SerializedName("confirmed")
            var confirmed: Int? = null;

            @SerializedName("deaths")
            var deaths: Int? = null;

            @SerializedName("recovered")
            var recovered: Int? = null;
        }
    }

}