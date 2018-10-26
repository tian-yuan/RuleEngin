package com.netease.push.rule;

import com.netease.push.destination.DestinationManager;
import com.netease.push.message.OnlineStatus;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzliuzebo on 2018/10/22.
 */
@Component
public class DynamicRuleParser {
    private static Logger logger = LogManager.getLogger(DynamicRuleParser.class);

    @Autowired
    private DestinationManager destinationManager;

    private String topic;
    private String drl;
    private StatelessKieSession statelessKieSession;
    private RuleMetadata ruleMetadata;

    private String applyRuleTemplate(Event event, Rule rule) throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();

        data.put("rule", rule);
        data.put("eventType", event.getClass().getName());

        return objectDataCompiler.compile(Arrays.asList(data), Thread.currentThread().getContextClassLoader().getResourceAsStream("rule-template.drl"));
    }

    public void update(RuleMetadata ruleMetadata) {
        String sql = ruleMetadata.getSql();
        // first parse sql
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            Select selectStatement = (Select)statement;
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
            // just use the first table name which is kafka topic
            this.topic = tableList.get(0);
            Rule topicRule = new Rule();

            Condition topicCondition = new Condition();
            topicCondition.setField("topic");
            topicCondition.setOperator(Condition.Operator.EQUAL_TO);
            topicCondition.setValue(this.topic);

            // In reality, you would have multiple rules for different types of events.
            // The eventType property would be used to find rules relevant to the event
            topicRule.setEventType(Rule.eventType.ORDER);

            topicRule.setConditions(Arrays.asList(topicCondition));

            try {
                this.drl = applyRuleTemplate(new OnlineStatus(), topicRule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
            return;
        }

        // second dynamic generate rule
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/rule.drl", drl);
        kieServices.newKieBuilder(kieFileSystem).buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        statelessKieSession = kieContainer.getKieBase().newStatelessKieSession();

        statelessKieSession.getGlobals().set("destinationManager", destinationManager);
        statelessKieSession.getGlobals().set("action", ruleMetadata.getAction());
    }

    public void evaluate(Event event) {
        statelessKieSession.execute(event);
    }
}
