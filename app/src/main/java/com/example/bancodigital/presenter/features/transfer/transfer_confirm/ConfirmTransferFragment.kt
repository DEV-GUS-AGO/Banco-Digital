package com.example.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.MainGraphDirections
import com.example.bancodigital.R
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.databinding.FragmentTransferConfirmBinding
import com.example.bancodigital.presenter.features.deposit.DepositFormFragmentDirections
import com.example.bancodigital.util.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmTransferFragment : Fragment() {

    private var _binding: FragmentTransferConfirmBinding? = null
    private val binding get() = _binding!!

    private val confirmTransferViewModel: ConfirmTransferViewModel by viewModels()

    private val args: ConfirmTransferFragmentArgs by navArgs()

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, true)

        configData()

        initListeners()
    }

    private fun getBalance() {
        confirmTransferViewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Sucess -> {
                    if ((stateView.data ?: 0f) >= args.amount) {

                        val transfer = Transfer(
                            idUserReceived = args.user.id,
                            idUserSent = FirebaseHelper.getUserId(),
                            amount = args.amount
                        )

                        saveTransfer(transfer)

                    } else {
                        binding.btnConfirm.isEnabled = true
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = getString(R.string.text_message_insufficient_balance_confirm_transfer_fragment))
                    }
                }

                is StateView.Error -> {
                    binding.btnConfirm.isEnabled = true
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun saveTransfer(transfer: Transfer) {
        confirmTransferViewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Sucess -> {
                    updateTransfer(transfer)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun updateTransfer(transfer: Transfer) {
        confirmTransferViewModel.updateTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Sucess -> {
                    saveTransaction(transfer)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun saveTransaction(transfer: Transfer) {
        confirmTransferViewModel.saveTransaction(transfer)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {

                    }

                    is StateView.Sucess -> {
                        updateTransferTransaction(transfer)
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = stateView.message)
                    }
                }
            }
    }

    private fun updateTransferTransaction(transfer: Transfer) {
        confirmTransferViewModel.updateTransferTransaction(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Sucess -> {
                    val action = MainGraphDirections
                        .actionGlobalReceiptTransferFragment(transfer.id, false)

                    findNavController().navigate(action)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            binding.btnConfirm.isEnabled = false
            getBalance()
        }
    }

    private fun configData() {
        if (args.user.image.isNotEmpty()) {
            Picasso.get()
                .load(args.user.image)
                .tag(tagPicasso)
                .fit().centerCrop()
                .into(binding.imageUser, object : Callback {
                    override fun onSuccess() {
                        binding.progressImage.isVisible = false
                        binding.imageUser.isVisible = true
                    }

                    override fun onError(e: java.lang.Exception?) {

                    }
                })
        } else {
            binding.progressImage.isVisible = false
            binding.imageUser.isVisible = true
            binding.imageUser.setImageResource(R.drawable.ic_user_place_holder)
        }

        binding.textUserName.text = args.user.name
        binding.textAmountTransaction.text =
            getString(R.string.text_formated_value, GetMask.getFormatedValue(args.amount))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }

}