package com.netease.push.rule;

import lombok.Data;

import java.util.List;

/**
 * Created by hzliuzebo on 2018/10/18.
 */
@Data
public class RuleMetadata {
    private String sql;

    private Boolean ruleDisabled;

    private String npnsIotSqlVersion;

    private List<Action> actions;
}
