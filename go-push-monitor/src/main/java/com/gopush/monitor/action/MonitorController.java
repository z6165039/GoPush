package com.gopush.monitor.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/15 下午5:15
 */
@Controller
public class MonitorController {

    @RequestMapping("/monitor")
    public String monitor(){
        return "/monitor";
    }
}
