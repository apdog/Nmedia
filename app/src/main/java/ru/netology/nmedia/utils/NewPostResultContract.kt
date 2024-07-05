package ru.netology.nmedia.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.presentation.view.CreatePostActivity

class NewPostResultContract : ActivityResultContract<Pair<String?, Boolean>, String?>() {

    override fun createIntent(context: Context, input: Pair<String?, Boolean>): Intent {
        val intent = Intent(context, CreatePostActivity::class.java)
        intent.putExtra(Intent.EXTRA_TEXT, input.first)
        intent.putExtra("IS_EDIT_MODE", input.second)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}
