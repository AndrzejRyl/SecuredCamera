package com.ryl.securedcamera.presentation.main

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ryl.securedcamera.R
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.mainScreenFragmentContainer) as NavHostFragment
    }
    private val navController: NavController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewsWithPermissionCheck()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun setViews() {
        mainScreenBottomNavigationView.setupWithNavController(navController)
    }

    // TODO Change this to proper dialog. Toast is disappearing when system dialog appears
    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) = request.proceed()

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() = showToast(R.string.camera_permission_denied).also { finish() }

    private fun showToast(@StringRes textResId: Int) {
        Toast.makeText(this, textResId, Toast.LENGTH_LONG).show()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() = showToast(R.string.camera_permission_denied).also { finish() }

}