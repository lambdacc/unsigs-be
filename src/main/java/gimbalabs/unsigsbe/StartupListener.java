package gimbalabs.unsigsbe;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class StartupListener implements
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(StartupListener.class.getName());

    private final UnsigsService unsigsService;

    public StartupListener(UnsigsService unsigsService) {
        this.unsigsService = unsigsService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("Initialising Unsigs master data..");
        boolean res = false;
        try {
            res = unsigsService.loadMasterData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Executed master data init with result: " + res);
    }
}
