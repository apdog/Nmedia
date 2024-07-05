package ru.netology.nmedia.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityCreatePostBinding
import ru.netology.nmedia.presentation.viewModel.MainActivityViewModel

class CreatePostActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialText = intent?.getStringExtra(Intent.EXTRA_TEXT)
        val isEditMode = intent?.getBooleanExtra("IS_EDIT_MODE", false) ?: false

        binding.editTextContent.setText(initialText)
        binding.textViewTitle.text = if (isEditMode) getString(R.string.edit_post) else getString(R.string.add_a_new_post)
        binding.buttonSubmit.text = if (isEditMode) getString(R.string.update) else getString(R.string.add_post)

        with(binding) {
            editTextContent.requestFocus()
            buttonSubmit.setOnClickListener {
                val resultIntent = Intent()
                if (editTextContent.text.isNullOrBlank()) {
                    setResult(Activity.RESULT_CANCELED, resultIntent)
                } else {
                    val content = editTextContent.text.toString()
                    resultIntent.putExtra(Intent.EXTRA_TEXT, content)
                    setResult(Activity.RESULT_OK, resultIntent)
                }
                finish()
            }

            buttonCancel.setOnClickListener {
                viewModel.cancelEditing()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }
}
