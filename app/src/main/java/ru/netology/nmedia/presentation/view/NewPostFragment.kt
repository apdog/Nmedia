package ru.netology.nmedia.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.presentation.viewModel.PostViewModel
import ru.netology.nmedia.utils.StringArg

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private lateinit var binding: FragmentNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)

        // Получение аргументов
        val initialText = arguments?.textArg
        val isEditMode = arguments?.getBoolean("IS_EDIT_MODE", false) ?: false

        // Установка текста и заголовков в зависимости от режима
        binding.editTextContent.setText(initialText)
        binding.textViewTitle.text = if (isEditMode) getString(R.string.edit_post) else getString(R.string.add_a_new_post)
        binding.buttonSubmit.text = if (isEditMode) getString(R.string.update) else getString(R.string.add_post)


        with(binding) {
            editTextContent.requestFocus()
            buttonSubmit.setOnClickListener {
                if (editTextContent.text.isNotBlank()) {
                    viewModel.changePostContent(editTextContent.text.toString())
                    viewModel.save()
                    findNavController().navigateUp()
                }
            }

            buttonCancel.setOnClickListener {
                viewModel.cancelEditing()
                activity?.setResult(Activity.RESULT_CANCELED)
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }

}
