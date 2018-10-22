package com.netease.push.message;

import com.netease.push.rule.Action;
import com.netease.push.rule.Event;
import lombok.Data;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
@Data
public class OnlineStatus extends Event {
    private String topic;

    private String deviceId;

    private Integer onlineStatus;

    private Long eventTime;

    private String domain;
}
