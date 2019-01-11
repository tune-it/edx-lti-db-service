package com.tuneit.edx.lti.rest;

import com.tuneit.edx.lti.config.WebConfig;
import com.tuneit.edx.lti.to.EdxUserInfo;
import com.tuneit.edx.lti.to.SqlQueryForm;
import org.imsglobal.aspect.Lti;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

@Controller
public class Lab {

    public static final String LIS_SOURCED_ID_NAME  = "lis_result_sourcedid";

    public static final String LIS_OUTCOME_URL_NAME = "lis_outcome_service_url";

    public static final String CURRENT_LAB_ID_NAME  = "";

    /**
     * Метод для отладки (для dev режима). Используется
     * движком themyleaf при GET запросах (когда запросы
     * ходят напрямую из браузеров).
     *
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return название themyleaf-шаблона, который будет обработан, как результат текущего запроса
     */
    @GetMapping("/debug/edx/home")
    public String doGet(@RequestParam(name=LIS_SOURCED_ID_NAME, required = false) String sourcedId,
                        @RequestParam(name=LIS_OUTCOME_URL_NAME, required = false) String serviceUrl,
                        HttpSession session,
                        Map<String, Object> model) {

        // вся обработка в режиме отладки эквивалентна продакшн режиму
        return doPost (
                "1",
                sourcedId == null ? "DEBUG_ID" : sourcedId,
                serviceUrl == null ? "DEBUG_URL" : serviceUrl,
                session,
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
                         @RequestParam(name=LIS_SOURCED_ID_NAME) String sourcedId,
                         @RequestParam(name=LIS_OUTCOME_URL_NAME) String serviceUrl,
                         HttpSession session,
                         Map<String, Object> model) {

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("numberOfLab", String.valueOf(rnd.nextInt(4)) );


        checkLisParams(sourcedId, serviceUrl, session);

        SqlQueryForm queryForm = new SqlQueryForm();
        model.put("query", queryForm);

        /**  передаем управление движку themyleaf (см. classpath:/templates/index.html)  */
        return "index";
    }

    @Lti
    @PostMapping("/api/rest/lti/{labId}/result")
    public String doResult( @PathVariable("labId") String labId,
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
            String serviceUrl = (String) request.getSession().getAttribute(LIS_OUTCOME_URL_NAME);
            String sourcedId = (String) request.getSession().getAttribute(LIS_SOURCED_ID_NAME);

            System.out.println("Push score to URL: " + serviceUrl);
            int result = Score.push(sourcedId, serviceUrl);
            System.out.println("RETURN CODE = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "result";
    }

    public void checkLisParams(String sourcedId, String outcomeUrl, HttpSession session) {
        String sessionSourcedId = (String) session.getAttribute(LIS_SOURCED_ID_NAME);
        String sessionOutcomeUrl = (String) session.getAttribute(LIS_OUTCOME_URL_NAME);
        if(sourcedId != null && !sourcedId.equals(sessionSourcedId)) {
            session.setAttribute(LIS_SOURCED_ID_NAME, sourcedId);
        }
        if(outcomeUrl != null && !outcomeUrl.equals(sessionOutcomeUrl)) {
            session.setAttribute(LIS_OUTCOME_URL_NAME, outcomeUrl);
        }
    }

    private static Random rnd = new Random();
}
