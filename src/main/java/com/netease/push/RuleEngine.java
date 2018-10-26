package com.netease.push;

import com.netease.push.rule.RuleEngineManager;
import com.netease.push.utils.MonitorControl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class RuleEngine implements CommandLineRunner {
    private static Logger logger = LogManager.getLogger(RuleEngine.class);

    @Autowired
    private RuleEngineManager ruleEngineManager;

    public static void main( String[] args ) {
        // do not need start web server
        // ConfigurableApplicationContext context = new SpringApplicationBuilder(PushApnsApplication.class).web(false).run(args);
        SpringApplication.run(RuleEngine.class, args);
        try {
            waitForShutdown(9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        ruleEngineManager.start();
        registerShutdownHook();
    }

    private void registerShutdownHook() {
        final class MonitorShutdown extends Thread {
            private MonitorShutdown() {
                super("PushMonitorShutdown");
            }

            public void run() {
                logger.info("push monitor agent application exit.");
                if (ruleEngineManager.isStarted()) {
                    ruleEngineManager.stop();
                }
            }
        }
        Runtime.getRuntime().addShutdownHook(new MonitorShutdown());
    }

    private static void waitForShutdown(int port) throws IOException {
        new MonitorControl().start(port);
    }
}
