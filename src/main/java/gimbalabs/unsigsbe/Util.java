package gimbalabs.unsigsbe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static gimbalabs.unsigsbe.Constants.*;

public class Util {

    public static Map<String, Object> newPagedResponseMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(RESULT_LIST, Collections.emptyList());
        map.put(LIST_SIZE, 0);
        map.put(TOTAL_PAGES, 0);
        map.put(HAS_NEXT_PAGE, false);
        return map;
    }
}
