package com.game.sdk.domain.result;


import java.util.List;

public class RechargeRecordResultBean extends BaseResultBean {

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        private String count;
        private int page;
        private List<ListBean> list;

        public String getCount() {  return count;  }

        public void setCount(String count) {  this.count = count;  }

        public int getPage() { return page; }

        public void setPage(int page) { this.page = page; }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String code;
            private String created;
            private String payed_fee;
            private String payment;
            private String app_name;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getPayed_fee() {
                return payed_fee;
            }

            public void setPayed_fee(String payed_fee) {
                this.payed_fee = payed_fee;
            }

            public String getPayment() {
                return payment;
            }

            public void setPayment(String payment) {
                this.payment = payment;
            }

            public String getApp_name() {
                return app_name;
            }

            public void setApp_name(String app_name) {
                this.app_name = app_name;
            }

            public ListBean(String code, String created, String payed_fee, String payment, String app_name) {
                this.code = code;
                this.created = created;
                this.payed_fee = payed_fee;
                this.payment = payment;
                this.app_name = app_name;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "code='" + code + '\'' +
                        ", created='" + created + '\'' +
                        ", payed_fee='" + payed_fee + '\'' +
                        ", payment='" + payment + '\'' +
                        ", app_name='" + app_name + '\'' +
                        '}';
            }
        }


        @Override
        public String toString() {
            return "InfoBean{" +
                    "count='" + count + '\'' +
                    ", page=" + page +
                    ", list=" + list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RechargeRecordResultBean{" +
                "info=" + info +
                '}';
    }
}
