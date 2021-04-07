package fi.oamk.musiccourseapp.user

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fi.oamk.musiccourseapp.databinding.FragmentMessagesBinding

class LoginTab : Fragment(){

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }
}