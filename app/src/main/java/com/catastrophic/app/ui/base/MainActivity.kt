package com.catastrophic.app.ui.base

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.catastrophic.app.databinding.ActivityMainBinding
import com.telkom.myindihomex.ui.base.MainView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun toolbarState(state: Boolean) {
        if (state) {
            binding.toolbar.visibility = View.VISIBLE
        } else {
            binding.toolbar.visibility = View.GONE
        }
    }


}