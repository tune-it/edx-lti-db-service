package com.tuneit.edx.lti.unit;

import com.tuneit.courses.Task;
import com.tuneit.courses.TaskGeneratorService;
import com.tuneit.edx.lti.config.WebConfig;
import com.tuneit.edx.lti.rest.in.OAuthHeaders;
import com.tuneit.edx.lti.rest.out.ScoreSender;
import com.tuneit.edx.lti.to.EdxUserInfo;
import com.tuneit.edx.lti.to.TasksForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.tuneit.edx.lti.rest.in.LtiHandler.LIS_OUTCOME_URL_NAME;
import static com.tuneit.edx.lti.rest.in.LtiHandler.LIS_SOURCED_ID_NAME;
import static com.tuneit.edx.lti.rest.in.LtiHandler.OAUTH_HEADERS;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class ModelViewProcessor {

    private static final String PATH_TO_MAIN_PAGE    = "index";
    private static final String PATH_TO_RESULTS_PAGE = "result";

    @Autowired
    private TaskGeneratorService service;

    @Autowired
    private ScoreSender scoreSender;

    // TODO see ticket #3
    private static int variant = 0;

    public String renderMain(String labId, String sourcedId, String serviceUrl,
                                HttpServletRequest request, Map<String, Object> model) {

        String username = getUsername(request);

        HttpSession session  = request.getSession();

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("numberOfLab", labId );

        // TODO temporary hardcode. See ticket #3 and #2
        Task[] tasks = service.getTasks(username, labId, String.valueOf(variant++), 0);
        model.put("task1", tasks[0].getQuestion());
        model.put("task2", tasks[1].getQuestion());
        model.put("task3", tasks[2].getQuestion());
        model.put("task4", tasks[3].getQuestion());
        model.put("task5", tasks[4].getQuestion());
        model.put("task6", tasks[5].getQuestion());
        model.put("task7", tasks[6].getQuestion());
        model.put("task8", tasks[7].getQuestion());
        model.put("task9", tasks[8].getQuestion());
        model.put("task10", tasks[9].getQuestion());
        model.put("task11", tasks[10].getQuestion());
        model.put("task12", tasks[11].getQuestion());

        session.setAttribute("tasks", tasks);

        checkLisParams(sourcedId, serviceUrl, session);

        TasksForm queryForm = new TasksForm();
        model.put("query", queryForm);

        return PATH_TO_MAIN_PAGE;
    }

    public String renderResult(String labId, HttpServletRequest request,
                           Map<String, Object> model, TasksForm queryForm) {

        String username = getUsername(request);

        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("username", username);
        model.put("numberOfLab", labId );

        Task[] tasks = (Task[]) request.getSession().getAttribute("tasks");
        for (int i = 0; i < tasks.length; i++) {
            tasks[i].setAnswer(queryForm.asArray()[i]);
        }

        for(Task t : tasks) {
            t.setComplete( !(t.getAnswer() == null || t.getAnswer().isEmpty()) );
        }

        service.checkTasks(tasks);

        for (int i = 0; i < 12; i++) {
            if (i == 0) {
                model.put("queryText", getSQLStringWithComments(tasks[0]));
            } else {
                model.put("queryText" + (i + 1), getSQLStringWithComments(tasks[i]));
            }
        }

        float x = 0;
        for(Task t : tasks) {
            x += t.getRating();
        }

        model.put("rating", String.format("%.2f", x * 100 / tasks.length) + "%");

        try {
            String serviceUrl = (String) request.getSession().getAttribute(LIS_OUTCOME_URL_NAME);
            String sourcedId = (String) request.getSession().getAttribute(LIS_SOURCED_ID_NAME);

            log.debug("Push score to URL: " + serviceUrl);

            int result = scoreSender.push(sourcedId, serviceUrl, x / tasks.length, (OAuthHeaders) request.getSession().getAttribute(OAUTH_HEADERS));
            log.debug("RETURN CODE = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PATH_TO_RESULTS_PAGE;
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

    // TODO move to Task.class or some TaskUtil

    private String getSQLStringWithComments(Task task) {
        return new StringBuilder()
                .append("# \n# ")
                .append(task.getQuestion())
                .append("\n# \n# ")
                .append( (task.getRating() < 0.1f) ? "Ответ неверный" : "Ответ верный" )
                .append("\n# \n")
                .append(task.getAnswer())
                .toString();
    }

    private String getUsername(HttpServletRequest request) {
        EdxUserInfo userInfo = (EdxUserInfo) request.getAttribute(WebConfig.ATTRIBUTE_USER_INFO);

        return userInfo == null ? "Unknown" :
                (userInfo.getUsername() == null ? "Guest" : userInfo.getUsername());
    }
}
