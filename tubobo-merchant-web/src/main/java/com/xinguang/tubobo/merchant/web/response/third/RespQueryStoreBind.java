package com.xinguang.tubobo.merchant.web.response.third;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/6/30.
 */
public class RespQueryStoreBind implements Serializable {
    private boolean eleBound;
    private boolean mtBound;
    private boolean yzBound;

    public boolean isEleBound() {
        return eleBound;
    }

    public void setEleBound(boolean eleBound) {
        this.eleBound = eleBound;
    }

    public boolean isMtBound() {
        return mtBound;
    }

    public void setMtBound(boolean mtBound) {
        this.mtBound = mtBound;
    }

    public boolean isYzBound() {
        return yzBound;
    }

    public void setYzBound(boolean yzBound) {
        this.yzBound = yzBound;
    }
}
