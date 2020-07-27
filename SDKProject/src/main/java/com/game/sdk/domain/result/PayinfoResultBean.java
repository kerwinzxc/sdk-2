package com.game.sdk.domain.result;


import java.util.List;

public class PayinfoResultBean extends BaseResultBean {


    /**
     * info : {"order_code":"201710301385365646","pay_fee":"0.2","miss":"1","pay_token":"ac414f362f6d247a69e18cbd97507806"}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * order_code : 201710301385365646
         * pay_fee : 0.2
         * miss : 1
         * pay_token : ac414f362f6d247a69e18cbd97507806
         */

        private String order_code;
        private String pay_fee;
        private String miss;
        private String pay_token;
        private String desc;
        private String total;
        private List<OthersBean> others;

        public String getOrderCode() {
            return order_code;
        }

        public void setOrderCode(String order_code) {
            this.order_code = order_code;
        }

        public String getPayFee() {
            return pay_fee;
        }

        public void setPayFee(String pay_fee) {
            this.pay_fee = pay_fee;
        }

        public String getMiss() {
            return miss;
        }

        public void setMiss(String miss) {
            this.miss = miss;
        }

        public String getPayToken() {
            return pay_token;
        }

        public void setPayToken(String pay_token) {
            this.pay_token = pay_token;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<OthersBean> getOthers() {
            return others;
        }

        public void setOthers(List<OthersBean> others) {
            this.others = others;
        }

        public static class OthersBean{
            private String type;
            private String img;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            @Override
            public String toString() {
                return "OthersBean{" +
                        "type='" + type + '\'' +
                        ", img='" + img + '\'' +
                        '}';
            }
        }
    }
}
