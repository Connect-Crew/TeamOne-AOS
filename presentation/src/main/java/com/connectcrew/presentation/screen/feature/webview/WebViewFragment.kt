package com.connectcrew.presentation.screen.feature.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentWebViewBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(R.layout.fragment_web_view) {

    private val args: WebViewFragmentArgs by navArgs()

    private val webViewModel: WebViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = webViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        dataBinding.webView.apply {
            settings.apply {
                setSupportZoom(false)
                javaScriptEnabled = true // 자바스크립트 허용
                builtInZoomControls = false
                displayZoomControls = false
                loadWithOverviewMode = true // 메타태그 허용
                useWideViewPort = true
                displayZoomControls = false
                defaultTextEncodingName = "UTF-8"
                domStorageEnabled = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                // Setting Local Storage
                databaseEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                loadUrl(args.webUrl)
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlWebView.setNavigationOnClickListener {
                if (!findNavController().navigateUp()) {
                    requireActivity().finish()
                }
            }

            webView.run {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        webViewModel.setFinished(false)
                        return super.shouldOverrideUrlLoading(view, url)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        webViewModel.setFinished(true)
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        webViewModel.setProgress(newProgress)
                    }

                    override fun onReceivedTitle(view: WebView, title: String) {
                        super.onReceivedTitle(view, title)
                        webViewModel.setTitle(title)
                    }
                }
            }
        }
    }
}