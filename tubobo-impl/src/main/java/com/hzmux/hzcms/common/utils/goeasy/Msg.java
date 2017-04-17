package com.hzmux.hzcms.common.utils.goeasy;

import java.util.HashMap;

public class Msg extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    
    private static final String TYPE_TASK_TODO = "t_tt";

    private Msg(){};
    
    public static Msg instanceTaskTodo(String menuId, int num) {
        Msg msg = new Msg();
        msg.put("type", TYPE_TASK_TODO);
        msg.put("menuId", menuId);
        msg.put("num", String.valueOf(num));

        return msg;
    }
}
