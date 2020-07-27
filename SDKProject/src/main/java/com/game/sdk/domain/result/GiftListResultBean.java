package com.game.sdk.domain.result;


import java.util.List;

public class GiftListResultBean extends BaseResultBean {

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {

        private String app_id;
        private String app_name;
        private String app_icon;
        private List<ListBean> gift_list;

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

        public List<ListBean> getGift_list() {
            return gift_list;
        }

        public void setGift_list(List<ListBean> gift_list) {
            this.gift_list = gift_list;
        }

        public static class ListBean {
            /**
             * "id" , "name" , "intro" , "content" , "instr" , "type" , "term" , "is_get"
             */
            private String id;
            private String name;
            private String intro;
            private String content;
            private String instr;
            private int type;
            private String term;
            private String is_get;

            public String getId() {
                return id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getInstr() {
                return instr;
            }

            public void setInstr(String instr) {
                this.instr = instr;
            }

            public String getTerm() {
                return term;
            }

            public void setTerm(String term) {
                this.term = term;
            }

            public String getIs_get() {
                return is_get;
            }

            public void setIs_get(String is_get) {
                this.is_get = is_get;
            }
        }


    }

}
