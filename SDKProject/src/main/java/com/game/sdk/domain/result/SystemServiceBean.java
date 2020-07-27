package com.game.sdk.domain.result;


public class SystemServiceBean extends BaseResultBean {

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {


        private String tel;
        private String email;
        private String qq;
        private String qq_group;
        private String time;

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getQq_group() {
            return qq_group;
        }

        public void setQq_group(String qq_group) {
            this.qq_group = qq_group;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
