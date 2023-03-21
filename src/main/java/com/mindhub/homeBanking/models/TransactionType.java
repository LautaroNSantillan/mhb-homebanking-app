package com.mindhub.homeBanking.models;

public enum TransactionType {
        CREDIT(true),
        DEBIT(false);

        private Boolean tType;

        TransactionType (Boolean tType) {
                this.tType = tType;
        }

        public Boolean getTypeBool() {
                return tType;
        }
}
