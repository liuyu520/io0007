package com.common.util;

import com.string.widget.util.ValueWidget;

import java.util.ArrayList;
import java.util.List;

/***
 * 复制数组
 * @param <E>
 */
public class CopyList<E> {
    /***
     * 复制之后的数组将与hibernate断开连接,不会触发更新<br />
     * 12.2.3.3. Bidirectional many-to-many
     Hibernate treats bidirectional many-to-many associations owned by a read-only entity the same as when owned by an entity that is not read-only.

     Hibernate dirty-checks bidirectional many-to-many associations.
     * @param list
     * @return
     */
    public List<E> copy(List list) {

        List<E> agentListNew = new ArrayList<>();
        if (ValueWidget.isNullOrEmpty(list)) {
            return agentListNew;
        }
        int size2 = list.size();
        for (int i = 0; i < size2; i++) {
            E agent = (E) list.get(i);
            agentListNew.add(agent);
        }
        return agentListNew;
    }

}
