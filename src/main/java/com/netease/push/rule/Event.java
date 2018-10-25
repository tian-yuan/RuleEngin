package com.netease.push.rule;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
public abstract class Event {
    @JSONField(serialize=false)
    private Action action;

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
