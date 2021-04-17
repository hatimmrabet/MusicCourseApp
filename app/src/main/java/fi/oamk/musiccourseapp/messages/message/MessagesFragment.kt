package fi.oamk.musiccourseapp.messages.message

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import fi.oamk.musiccourseapp.databinding.FragmentMessagesBinding


class MessagesFragment : Fragment() {
    private val TAG = "MessagesFragment"

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    val args: MessagesFragmentArgs by navArgs()

    private val viewModel: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setMessagesRef(args.id)
        viewModel.messages.observe(viewLifecycleOwner) {messages ->
            binding.recyclerView.adapter = ItemAdapter(messages)
        }
        binding.sendButton.setOnClickListener {
            viewModel.addMessage(binding.editText.text.toString())
            binding.editText.text?.clear()
            it.hideKeyboard()
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}