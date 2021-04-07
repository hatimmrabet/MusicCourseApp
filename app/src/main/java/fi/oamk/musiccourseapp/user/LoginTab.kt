package fi.oamk.musiccourseapp.user

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fi.oamk.musiccourseapp.databinding.LoginTabBinding

class LoginTab : Fragment(){

    private var _binding: LoginTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LoginTabBinding.inflate(inflater, container, false)
        return binding.root
    }
}