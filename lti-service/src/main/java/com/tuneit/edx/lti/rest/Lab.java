package com.tuneit.edx.lti.rest;

import com.tuneit.edx.lti.config.WebConfig;
import com.tuneit.edx.lti.to.EdxUserInfo;
import com.tuneit.edx.lti.to.SqlQueryForm;
import org.imsglobal.aspect.Lti;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;

@Controller
public class Lab {

    /**
     * Метод для отладки (для dev режима). Используется
     * движком themyleaf при GET запросах (когда запросы
     * ходят напрямую из браузеров).
     *
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return название themyleaf-шаблона, который будет обработан, как результат текущего запроса
     */
    @GetMapping("/debug/edx/home")
    public String doGet(@RequestParam(name="lis_result_sourcedid", required = false) String sourcedId,
                        @RequestParam(name="lis_outcome_service_url", required = false) String serviceUrl,
                        Map<String, Object> model) {

        // вся обработка в режиме отладки эквивалентна продакшн режиму
        return doPost (
                "1",
                sourcedId == null ? "DEBUG_ID" : sourcedId,
                serviceUrl == null ? "DEBUG_URL" : serviceUrl,
                model
        );
    }

    /**
     * Обработчик запросов к LTI-сервису
     *
     * @param labId       идендификатор задания
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return
     */
    @Lti
    @PostMapping("/api/rest/lti/{labId}")
    public String doPost(@PathVariable("labId") String labId,
                         @RequestParam(name="lis_result_sourcedid") String sourcedId,
                         @RequestParam(name="lis_outcome_service_url") String serviceUrl,
                         Map<String, Object> model) {

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("numberOfLab", String.valueOf(rnd.nextInt(4)) );

        SqlQueryForm queryForm = new SqlQueryForm();
        model.put("query", queryForm);

        /**  передаем управление движку themyleaf (см. classpath:/templates/index.html)  */
        return "index";
    }

    @Lti
    @PostMapping("/api/rest/lti/{labId}/result")
    public String doResult( @PathVariable("labId") String labId,
                            @RequestParam(name="lis_result_sourcedid", required = false) String sourcedId,
                            @RequestParam(name="lis_outcome_service_url", required = false) String serviceUrl,
                            HttpServletRequest request,
                            Map<String, Object> model,
                            @ModelAttribute SqlQueryForm queryForm) {

        EdxUserInfo userInfo = (EdxUserInfo) request.getAttribute(WebConfig.ATTRIBUTE_USER_INFO);

        String username = userInfo == null ? "Unknown" :
                ( userInfo.getUsername() == null ? "Guest" : userInfo.getUsername() );

        /**
         * DEBUG output
         */
        if(queryForm != null && queryForm.getTextQuery() != null)
            System.out.println(queryForm.getTextQuery());

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("username", username);
        model.put("numberOfLab", labId );

        try {
            System.out.println("Push score to URL: " + serviceUrl);
            int result = Score.push(sourcedId, serviceUrl);
            System.out.println("RETURN CODE = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "result";
    }

    private static Random rnd = new Random();
}
