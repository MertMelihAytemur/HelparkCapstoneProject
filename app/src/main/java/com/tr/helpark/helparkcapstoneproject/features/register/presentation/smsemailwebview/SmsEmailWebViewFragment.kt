package com.tr.helpark.helparkcapstoneproject.features.register.presentation.smsemailwebview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.util.Constants.HELPARK_WEB_SITE_URL
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentSmsEmailWebViewBinding


class SmsEmailWebViewFragment : Fragment() {

    private lateinit var binding: FragmentSmsEmailWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmsEmailWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
        setWebView()
        initListeners()
    }

    private fun initListeners() {
        with(binding){
            ivGoBack.setOnClickListener {
                navigateBack()
            }

            val onBackPressedCallBack = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateBack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                onBackPressedCallBack
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        with(binding.wvWebView) {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    // Show the progress bar when page loading starts
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Hide the progress bar when page loading finishes
                    binding.progressBar.visibility = View.GONE
                }
            }

            settings.apply {
                javaScriptEnabled = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                useWideViewPort = true
            }
            loadUrl(HELPARK_WEB_SITE_URL)
        }
    }

    private fun navigateBack() {
        if (binding.wvWebView.canGoBack()) {
            binding.wvWebView.goBack()
        } else {
            findNavController().popBackStack()
        }
    }
}