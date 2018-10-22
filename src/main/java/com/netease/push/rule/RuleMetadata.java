package com.netease.push.rule;

import lombok.Data;


/**
 * Created by hzliuzebo on 2018/10/18.
 */
@Data
public class RuleMetadata {
    private String sql;

    private Boolean ruleDisabled;

    private String npnsIotSqlVersion;

    private Action action;
}
