package com.prometheus_service.midas.core.data.features.remote_domains.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RemoteDomainsDto(
    @SerializedName("data") val data: Data
)
@Keep
data class Data(
    @SerializedName("data") val data: RemoteDomainsData,
    @SerializedName("name") val name: String,
    @SerializedName("key") val key: String
)
@Keep
data class RemoteDomainsData(
    @SerializedName("update_stored_domains_enabled") val updateStoredDomainsEnabled: String,
    @SerializedName("report_failing_domains_enabled") val reportFailingDomainsEnabled: String,
    @SerializedName("reserve_config_domains") val reserveConfigDomains: List<String>,
    @SerializedName("reserve_app_domains") val reserveAppDomains: List<String>,
    @SerializedName("active_config_domains") val activeConfigDomains: List<String>,
)
