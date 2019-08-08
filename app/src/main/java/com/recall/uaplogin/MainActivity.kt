package com.recall.uaplogin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.recall.uaplogin.app.PhoneApplication
import com.recall.uaplogin.service.FloatViewService
import com.recall.uaplogin.utils.ContactsUtil
import android.widget.Toast
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.recall.uaplogin.utils.MobileUtil


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dataList = ContactsUtil.getDataList()
    }

    override fun onResume() {
        super.onResume()
        SoulPermission.getInstance().checkAndRequestPermissions(
            Permissions.build(Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE),
            object : CheckRequestPermissionsListener {
                override fun onAllPermissionOk(allPermissions: Array<Permission>) {
                    Toast.makeText(
                        this@MainActivity, allPermissions.size.toString() + "permissions is ok" +
                                " \n  you can do your operations", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionDenied(refusedPermissions: Array<Permission>) {
                    Toast.makeText(
                        this@MainActivity,
                        refusedPermissions[0].toString() + " \n is refused you can not do next things",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    //**************授权信息
    private fun getPersimmionInfo() {
        if (Build.VERSION.SDK_INT >= 23) {
            //1. 检测是否添加权限   PERMISSION_GRANTED  表示已经授权并可以使用
            if (ContextCompat.checkSelfPermission(
                    PhoneApplication.getApplication(),
                    Manifest.permission.READ_CALL_LOG
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //手机为Android6.0的版本,权限未授权去i请授权
                //2. 申请请求授权权限
                //1. Activity
                // 2. 申请的权限名称
                // 3. 申请权限的 请求码
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_CALL_LOG//通话记录
                    ), 1005
                )
            } else {//手机为Android6.0的版本,权限已授权可以使用
                // 执行下一步
                getFloatWindowPersimmion()
            }
        } else {//手机为Android6.0以前的版本，可以使用
            getFloatWindowPersimmion()
        }
    }

    private fun getFloatWindowPersimmion(){
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
            }
        }
    }


}
