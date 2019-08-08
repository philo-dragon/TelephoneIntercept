package com.recall.uaplogin

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.recall.uaplogin.service.FloatViewService
import com.recall.uaplogin.utils.ContactsUtil


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }

    private fun requestPermission() {
        SoulPermission.getInstance().checkAndRequestPermissions(
            Permissions.build(Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE),
            object : CheckRequestPermissionsListener {
                override fun onAllPermissionOk(allPermissions: Array<Permission>) {
                    getFloatWindowPersimmion()
                }

                override fun onPermissionDenied(refusedPermissions: Array<Permission>) {
                    loop@ for (refusedPermission in refusedPermissions) {
                        when (refusedPermission.permissionName) {
                            Manifest.permission.READ_CALL_LOG -> {
                                if (refusedPermission.shouldRationale()) {
                                    AlertDialog.Builder(this@MainActivity)
                                        .setTitle("${getString(R.string.app_name)}需要通讯录权限")
                                        .setPositiveButton(
                                            "去授权"
                                        ) { dialog, _ ->
                                            toSetting(dialog)
                                        }
                                        .show()
                                } else {
                                    requestPermission()
                                }
                                break@loop
                            }
                            Manifest.permission.CALL_PHONE -> {
                                if (refusedPermission.shouldRationale()) {
                                    AlertDialog.Builder(this@MainActivity)
                                        .setTitle("${getString(R.string.app_name)}需要电话权限")
                                        .setPositiveButton(
                                            "去授权"
                                        ) { dialog, _ ->
                                            toSetting(dialog)
                                        }
                                        .show()
                                } else {
                                    requestPermission()
                                }
                                break@loop
                            }
                        }
                    }
                }
            })
    }

    private fun toSetting(dialog: DialogInterface) {
        dialog.dismiss()
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(intent)
    }

    private fun goHome() {
        val home = Intent(Intent.ACTION_MAIN)
        home.addCategory(Intent.CATEGORY_HOME)
        startActivity(home)
    }

    private fun getFloatWindowPersimmion() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                val intent = Intent(this@MainActivity, FloatViewService::class.java)
                startService(intent)
                goHome()
            }
        }
    }


}
