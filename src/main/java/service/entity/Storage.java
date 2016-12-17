package service.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by student on 17.12.16.
 */
public interface Storage {

    default List<String> getList(String from) {
        if (from.equals(""))
            return new ArrayList<String>();
        if (!from.contains(";"))
            return Arrays.asList(from);
        return Arrays.stream(from.split(";")).collect(Collectors.toList());
    }

    default String add(String from, String toadd) {
        if (from.equals("")) {
            from += toadd;
        } else if (!from.contains(";")) {
            if (!from.equals(toadd)) {
                from += ";" + toadd;
            }
        } else {
            List<String> s2 = Arrays.stream(from.split(";"))
                    .filter(s -> s.equals(toadd))
                    .collect(Collectors.toList());
            if (s2.size() == 0) {
                from += ";" + toadd;
            }

        }
        return from;
    }

    default String delete(String from, String todel) {
        if (from.contains(todel)) {
            if (!from.contains(";")) {
                from = "";
            } else {
                List<String> s2 = Arrays.stream(from.split(";"))
                        .filter(s -> !s.equals(todel))
                        .collect(Collectors.toList());
                if (s2.size() == 1) {
                    from = s2.get(0);
                } else {
                    from = s2.get(0);
                    for (int i = 1; i < s2.size(); i++) {
                        from += ";" + s2.get(i);
                    }
                }
            }
        }
        return from;
    }
}
