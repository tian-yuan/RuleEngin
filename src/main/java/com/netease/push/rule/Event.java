package com.netease.push.rule;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
public abstract class Event {
    private Action action;

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
