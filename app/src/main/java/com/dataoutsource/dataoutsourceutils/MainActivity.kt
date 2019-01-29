package com.dataoutsource.dataoutsourceutils

import android.os.Bundle
import androidx.navigation.Navigation
import com.dataoutsource.dataoutsourceutil.data.ui.Dataoutsource

class MainActivity : Dataoutsource() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp()
            = Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
}
