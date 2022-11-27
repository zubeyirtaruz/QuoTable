package com.zubeyirtaruz.quotable.template

import com.zubeyirtaruz.quotable.adapter.TemplateAdapter
import com.zubeyirtaruz.quotable.viewmodel.QuoteViewModel

class TemplateViewBinding (viewModel: QuoteViewModel) {
    val adapter = TemplateAdapter(
        quoteInfo = viewModel.quoteInfo,
        listener = viewModel
    )
    val itemDecoration = TemplateDecoration()
}