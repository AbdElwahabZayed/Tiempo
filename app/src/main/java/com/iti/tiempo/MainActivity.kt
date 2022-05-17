package com.iti.tiempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iti.tiempo.base.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}