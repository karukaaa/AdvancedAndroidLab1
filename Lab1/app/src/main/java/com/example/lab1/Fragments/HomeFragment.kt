package com.example.lab1.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lab1.R
import com.example.lab1.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root  // ✅ Use binding.root instead of inflating manually
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.intents.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_intentsFragment)
        }

        binding.broadcast.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_broadcastReceiverFragment)
        }

        binding.foreground.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foregroundServiceFragment)
        }

        binding.contentProvider.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_contentProviderFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // ✅ Prevent memory leaks
    }
}
