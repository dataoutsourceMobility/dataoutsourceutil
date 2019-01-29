package com.dataoutsource.dataoutsourceutils


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.dataoutsource.dataoutsourceutil.data.ui.CustomWebView
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if((activity as MainActivity).checkCookies()){
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.action_toExperimentFragment)
        }
        else{
            wvLogin.settings.javaScriptEnabled = true
            wvLogin.settings.domStorageEnabled = true
            wvLogin.webViewClient = CustomWebView({ it -> saveCookies(it) })
            wvLogin.loadUrl((activity as MainActivity).traingUrl)
        }
    }

    private fun saveCookies(cookies: String){
        (activity as MainActivity).saveCookies(cookies)
        if((activity as MainActivity).checkCookies())
            Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigate(R.id.action_toExperimentFragment)
    }

}
