package com.quizapp.ui.auth.role_selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quizapp.R

class RoleSelectionFragment : Fragment() {
// First time login, need to select role (Student/Teacher)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_role_selection, container, false)
    }

}