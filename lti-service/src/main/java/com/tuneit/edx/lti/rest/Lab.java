package com.tuneit.edx.lti.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuneit.edx.lti.to.EdxUserInfo;
import org.imsglobal.aspect.Lti;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.Map;

@Controller
public class Lab {

    /**
     * Метод для отладки (для dev режима). Используется
     * движком themyleaf при GET запросах (когда запросы
     * ходят напрямую из браузеров).
     *
     * @param x           параметр, заданный, как кастомный на стороне openedx
     * @param userId      параметр в теле запроса с данными текущего пользователя
     * @param edxUserInfo json-строка с информацией о пользователе. Лежит в cookie.
     * @param headers     объект со всем множеством заголовков
     * @param body        строка с plain text'ом запроса
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return название themyleaf-шаблона, который будет обработан, как результат текущего запроса
     */
    @GetMapping("/debug/edx/home")
    public String doGet(@RequestParam(name = "custom_x", required = false) Integer x,
                        @RequestParam(name = "user_id", required = false) String userId,
                        @CookieValue(value = "edx-user-info", required = false) Cookie edxUserInfo,
                        @RequestHeader HttpHeaders headers,
                        @RequestBody(required = false) String body,
                        Map<String, Object> model) {
        // В режиме отладки многое из параметров может оказаться недоступным
        if(body == null || body.isEmpty()) {
            body = "Да прибудет дом сей в чистоте и порядке!";
        }
        if(edxUserInfo == null) {
            edxUserInfo = new Cookie("edx-user-info", "{}");
        }
        // вся обработка в режиме отладки эквивалентна продакшн режиму
        return doPost("1", x, userId, edxUserInfo, headers, body, model);
    }

    /**
     * Обработчик запросов к LTI-сервису
     *
     * @param labId       идендификатор задания
     * @param x           параметр, заданный, как кастомный на стороне openedx
     * @param userId      параметр в теле запроса с данными текущего пользователя
     * @param edxUserInfo json-строка с информацией о пользователе. Лежит в cookie.
     * @param headers     объект со всем множеством заголовков
     * @param body        строка с plain text'ом запроса
     * @param model       объекты, которые нудны будут для рендеринга html-странички (themyleaf)
     * @return
     */
    @Lti
    @PostMapping("/api/rest/lti/{labId}")
    public String doPost(@PathVariable("labId") String labId,
                         @RequestParam(name = "custom_x", required = false) Integer x,
                         @RequestParam(name = "user_id", required = false) String userId,
                         @CookieValue(name = "edx-user-info", required = false) Cookie edxUserInfo,
                         @RequestHeader HttpHeaders headers,
                         @RequestBody String body,
                         Map<String, Object> model) {


        /** ****************************************************  *
         ** *********************** DEBUG **********************  *
         ** ****************************************************  */
        System.out.println("####################################### REQUEST BODY");
        System.out.println(body);
        System.out.println("####################################### REQUEST HEADERS");
        for(String key : headers.keySet()) {
            if(headers.getValuesAsList(key).isEmpty()) {
                continue;
            }
            headers.getValuesAsList(key).forEach(value ->
                System.out.println(key + " ===> " + value)
            );
        }

        

        /**  ********** PARSE 'edx-user-info' COOKIE **********  */
        if(edxUserInfo != null && edxUserInfo.getValue() != null) {
            System.out.println("Cookie exists");
            try {
                ObjectMapper om = new ObjectMapper();
                EdxUserInfo userInfo = om.readValue(edxUserInfo.getValue(), EdxUserInfo.class);
                if (userInfo != null) {
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println(userInfo.toString());
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        /**  ********************** DEBUG **********************  */

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("username", (userId == null ? "Guest" : userId));
        model.put("custom_param", (x == null ? "nothing" : x));

        /**  передаем управление движку themyleaf (см. classpath:/templates/index.html)  */
        return "index";
    }

}
