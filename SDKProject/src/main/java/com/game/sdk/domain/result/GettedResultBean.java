package com.game.sdk.domain.result;


import java.util.List;

public class GettedResultBean extends BaseResultBean {

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        private String count;
        private int total;
        private int page;
        private List<ListBean> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * "app_id" , "app_name" , "app_icon" , "gift_name" , "gift_term" , "gift_type" , "gift_code"
             */
            private String app_id;
            private String app_name;
            private String app_icon;
            private String gift_name;
            private String gift_term;
            private int gift_type;
            private String gift_code;
            private String gift_intro;

            public String getApp_id() {
                return app_id;
            }

            public void setApp_id(String app_id) {
                this.app_id = app_id;
            }

            public String getApp_name() {
                return app_name;
            }

            public void setApp_name(String app_name) {
                this.app_name = app_name;
            }

            public String getApp_icon() {
                return app_icon;
            }

            public void setApp_icon(String app_icon) {
                this.app_icon = app_icon;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_term() {
                return gift_term;
            }

            public void setGift_term(String gift_term) {
                this.gift_term = gift_term;
            }

            public int getGift_type() {
                return gift_type;
            }

            public void setGift_type(int gift_type) {
                this.gift_type = gift_type;
            }

            public String getGift_code() {
                return gift_code;
            }

            public void setGift_code(String gift_code) {
                this.gift_code = gift_code;
            }

            public String getGift_intro() {
                return gift_intro;
            }

            public void setGift_intro(String gift_intro) {
                this.gift_intro = gift_intro;
            }
        }


    }

}
