package com.game.sdk.domain.result;

public class PayGetLinkResultBean extends BaseResultBean {


    private IopInfo info;

    public IopInfo getInfo() {
        return info;
    }

    public void setInfo(IopInfo info) {
        this.info = info;
    }

    public static class IopInfo{
        private String iop;

        public String getIop() {
            return iop;
        }

        public void setIop(String iop) {
            this.iop = iop;
        }
    }

}
