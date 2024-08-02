package com.example.bancodigital.data.enum

enum class TransactionOperation {
    DEPOSIT,
    RECHARGE,
    TRANSFER;

    companion object {
        fun getOperation(operation: TransactionOperation): String {
            return when (operation) {
                DEPOSIT -> {
                    "DEPÓSITO"
                }
                RECHARGE -> {
                    "RECARGA DE TELEFONE"
                }
                TRANSFER -> {
                    "TRANSFERÊNCIA"
                }
            }
        }
    }

}