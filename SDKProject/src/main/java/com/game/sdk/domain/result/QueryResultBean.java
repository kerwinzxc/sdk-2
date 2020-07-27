package com.game.sdk.domain.result;


import java.io.Serializable;

public class QueryResultBean extends BaseResultBean implements Serializable {


    /**
     * info : {"order_code":"201711021818337516","out_trade_no":"xxxxxxxxxxxx","status":"1","cpstatus":"1","payed_fee":"200.00"}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        @Override
        public String toString() {
            return "InfoBean{" +
                    "order_code='" + order_code + '\'' +
                    ", out_trade_no='" + out_trade_no + '\'' +
                    ", status='" + status + '\'' +
                    ", cpstatus='" + cpstatus + '\'' +
                    ", payed_fee='" + payed_fee + '\'' +
                    '}';
        }

        /**
         * order_code : 201711021818337516
         * out_trade_no : xxxxxxxxxxxx
         * status : 1
         * cpstatus : 1
         * payed_fee : 200.00
         */

        private String order_code;
        private String out_trade_no;
        private String status;
        private String cpstatus;
        private String payed_fee;

        public String getOrderCode() {
            return order_code;
        }

        public void setOrderCode(String order_code) {
            this.order_code = order_code;
        }

        public String getOutTradeNo() {
            return out_trade_no;
        }

        public void setOutTradeNo(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCpstatus() {
            return cpstatus;
        }

        public void setCpstatus(String cpstatus) {
            this.cpstatus = cpstatus;
        }

        public String getPayedFee() {
            return payed_fee;
        }

        public void setPayedFee(String payed_fee) {
            this.payed_fee = payed_fee;
        }

    }
}
