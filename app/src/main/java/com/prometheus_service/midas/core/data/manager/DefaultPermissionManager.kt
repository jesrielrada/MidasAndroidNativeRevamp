package com.prometheus_service.midas.core.data.manager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.prometheus_service.midas.core.domain.manager.PermissionManager
import javax.inject.Inject

class DefaultPermissionManager @Inject constructor(
    private val context: Context
) : PermissionManager {
    override fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE
        ) == androidx.core.content.PermissionChecker.PERMISSION_GRANTED
    }

    override fun isStoragePermissionRationale(activity: FragmentActivity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            WRITE_EXTERNAL_STORAGE
        )
    }

    override fun requestStoragePermission(activity: FragmentActivity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            WRITE_EXTERNAL_STORAGE
        )
    }
}