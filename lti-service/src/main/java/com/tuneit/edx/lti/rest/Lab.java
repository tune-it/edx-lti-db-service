package com.tuneit.edx.lti.rest;

import com.tuneit.edx.lti.config.WebConfig;
import com.tuneit.edx.lti.to.EdxUserInfo;
import org.imsglobal.aspect.Lti;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class Lab {

    /**
     * Метод для отладки (для dev режима). Используется
     * движком themyleaf при GET запросах (когда запросы
     * ходят напрямую из браузеров).
     *
     * @param x           параметр, заданный, как кастомный на стороне openedx
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return название themyleaf-шаблона, который будет обработан, как результат текущего запроса
     */
    @GetMapping("/debug/edx/home")
    public String doGet(@RequestParam(name = "custom_x", required = false) Integer x,
                        @RequestParam(name="lis_result_sourcedid") String sourcedId,
                        @RequestParam(name="lis_outcome_service_url") String serviceUrl,
                        HttpServletRequest request,
                        Map<String, Object> model) {

        // вся обработка в режиме отладки эквивалентна продакшн режиму
        return doPost("1", x, sourcedId, serviceUrl, request, model);
    }

    /**
     * Обработчик запросов к LTI-сервису
     *
     * @param labId       идендификатор задания
     * @param x           параметр, заданный, как кастомный на стороне openedx
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return
     */
    @Lti
    @PostMapping("/api/rest/lti/{labId}")
    public String doPost(@PathVariable("labId") String labId,
                         @RequestParam(name = "custom_x", required = false) Integer x,
                         @RequestParam(name="lis_result_sourcedid") String sourcedId,
                         @RequestParam(name="lis_outcome_service_url") String serviceUrl,
                         HttpServletRequest request,
                         Map<String, Object> model) {

        EdxUserInfo userInfo = (EdxUserInfo) request.getAttribute(WebConfig.ATTRIBUTE_USER_INFO);

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("username", (userInfo.getUsername() == null ? "Guest" : userInfo.getUsername()));
        model.put("custom_param", (x == null ? "nothing" : x));

        try {
            System.out.println("Push score to URL: " + serviceUrl);
            int result = Score.push(sourcedId, serviceUrl);
            System.out.println("RETURN CODE = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**  передаем управление движку themyleaf (см. classpath:/templates/index.html)  */
        return "index";
    }
}
