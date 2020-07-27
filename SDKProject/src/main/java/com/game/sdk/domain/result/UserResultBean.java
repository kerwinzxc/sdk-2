package com.game.sdk.domain.result;


public class UserResultBean extends BaseResultBean {


    /**
     * info : {"uid":"1","uname":"d59e21e3dd626c","session":"d5d7cf84b70a906226d539e3c372e438"}
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
         * uid : Int
         * uname : String
         * pwd : String
         * session : String
         * auth : boole
         * mobile : String
         * idcard : String
         * realname : String
         * bind : boole 是否已绑定手机号 true:已绑定 false:未绑定
         */

        private String uid;
        private String uname;
        private String pwd;
        private String session;
        private boolean auth;
        private String mobile;
        private String idcard;
        private String realname;
        private boolean bind;

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public boolean isAuth() {
            return auth;
        }

        public void setAuth(boolean auth) {
            this.auth = auth;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public boolean isBind() {
            return bind;
        }

        public void setBind(boolean bind) {
            this.bind = bind;
        }
    }
}
