package com.example.bancodigital.presenter.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Recharge
import com.example.bancodigital.databinding.FragmentRechargeReceiptBinding
import com.example.bancodigital.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {

    private var _binding: FragmentRechargeReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: RechargeReceiptFragmentArgs by navArgs()

    private val rechargeReceiptViewModel: RechargeReceiptViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, homeAsUpEnabled = args.homeAsUpEnabled)

        getRecharge()

        initListeners()
    }

    private fun getRecharge() {
        rechargeReceiptViewModel.getRecharge(args.idRecharge)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {

                    }

                    is StateView.Sucess -> {
                        stateView.data?.let { configData(it) }
                    }

                    is StateView.Error -> {
                        Toast.makeText(requireContext(), "Ocorreu um erro.", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                }
            }
    }

    private fun initListeners() {
        binding.btnContinue.setOnClickListener {
            if (args.homeAsUpEnabled) {
                findNavController().popBackStack()
            } else {
                val navOptions: NavOptions =
                    NavOptions.Builder().setPopUpTo(R.id.rechargeFormFragment, true).build()
                findNavController().navigate(R.id.action_global_homeFragment, null, navOptions)
            }
        }
    }

    private fun configData(recharge: Recharge) {
        binding.textCodeTransaction.text = recharge.id
        binding.textDateTransaction.text =
            GetMask.getFormatedDate(recharge.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        binding.textAmountTransaction.text =
            getString(R.string.text_formated_value, GetMask.getFormatedValue(recharge.amount))

        binding.textPhoneNumber.text = Mask.mask(Constants.Mask.MASK_PHONE, recharge.number)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}