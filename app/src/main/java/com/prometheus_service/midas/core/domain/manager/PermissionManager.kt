package com.prometheus_service.midas.core.domain.manager

import androidx.fragment.app.FragmentActivity

interface PermissionManager {

    fun isStoragePermissionGranted(): Boolean
    fun isStoragePermissionRationale(activity: FragmentActivity): Boolean
    fun requestStoragePermission(activity: FragmentActivity): Boolean

}