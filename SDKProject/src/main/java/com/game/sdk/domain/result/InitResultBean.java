package com.game.sdk.domain.result;


public class InitResultBean extends BaseResultBean {


    /**
     * "info": {
     *         "is_login": "Y",
     *         "is_reg": "Y",
     *         "is_fast_reg": "Y",
     *         "is_auth": "N"
     *         "pay_auth": "N"
     *     }
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
         *  "is_login": "Y", 是否开启登录功能 Y:开启 N:未开启
         *  "is_reg": "Y",  是否开启注册功能 Y:开启 N:未开启
         *  "is_fast_reg": "Y",  是否开启一键注册功能 Y:开启 N:未开启
         *  "is_auth": "N"  是否需要实名认证，Y:必须实名认证 N:无需实名认证 T:可跳过实名认证
         *  "pay_auth": "N" 充值是否需要实名认证，Y:必须实名认证 N:无需实名认证
         */
        private String is_login;
        private String is_reg;
        private String is_fast_reg;
        private String is_auth;
        private String is_bind;
        private String pay_auth;

        public String getIs_login() {
            return is_login;
        }

        public void setIs_login(String is_login) {
            this.is_login = is_login;
        }

        public String getIs_reg() {
            return is_reg;
        }

        public void setIs_reg(String is_reg) {
            this.is_reg = is_reg;
        }

        public String getIs_fast_reg() {
            return is_fast_reg;
        }

        public void setIs_fast_reg(String is_fast_reg) {
            this.is_fast_reg = is_fast_reg;
        }

        public String getIs_auth() {
            return is_auth;
        }

        public String getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(String is_bind) {
            this.is_bind = is_bind;
        }

        public void setIs_auth(String is_auth) {
            this.is_auth = is_auth;
        }

        public String getPay_auth() {
            return pay_auth;
        }

        public void setPay_auth(String pay_auth) {
            this.pay_auth = pay_auth;
        }
    }
}
