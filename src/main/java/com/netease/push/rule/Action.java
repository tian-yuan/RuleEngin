package com.netease.push.rule;

import lombok.Data;

/**
 * Created by hzliuzebo on 2018/10/18.
 */
@Data
public class Action {
    private Tsdb tsdb;

    private Kafka kafka;
}
