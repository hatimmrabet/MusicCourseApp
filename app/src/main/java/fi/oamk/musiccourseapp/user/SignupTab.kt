package fi.oamk.musiccourseapp.user

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fi.oamk.musiccourseapp.databinding.SignupTabBinding

class SignupTab : Fragment(){

    private var _binding: SignupTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SignupTabBinding.inflate(inflater, container, false)
        return binding.root
    }

}