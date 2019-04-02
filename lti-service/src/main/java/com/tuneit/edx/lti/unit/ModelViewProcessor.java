package com.tuneit.edx.lti.unit;

import com.tuneit.courses.Task;
import com.tuneit.courses.TaskGeneratorService;
import com.tuneit.edx.lti.config.WebConfig;
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

@Slf4j
@Component
@Profile({"dev", "prod"})
public class ModelViewProcessor {

    private static final String PATH_TO_MAIN_PAGE = "index";
    private static final String PATH_TO_RESULTS_PAGE = "result";
    // TODO see ticket #3
    private static int variant = 0;
    @Autowired
    private TaskGeneratorService service;
    @Autowired
    private ScoreSender scoreSender;

    private static final Object SESSION_LOCK = new Object();

    public String renderMain(String labId, String sourcedId, String serviceUrl,
                             HttpServletRequest request, Map<String, Object> model, int taskId) {
        EdxUserInfo edxUserInfo = new EdxUserInfo();
        String[] splittedSourceId = sourcedId.split(":");
        edxUserInfo.setUsername(splittedSourceId[splittedSourceId.length - 1]);
        request.setAttribute(WebConfig.ATTRIBUTE_USER_INFO, edxUserInfo);

        String username = getUsername(request);

        HttpSession session = request.getSession();

        /*  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("numberOfLab", labId);

        // TODO temporary hardcode. See ticket #3 and #2
        // TODO FIX variant increment
        Task[] tasks = service.getTasks(username, labId, String.valueOf(/*variant++*/variant), 0);
        model.put("task", tasks[taskId].getQuestion());
        model.put("taskId", taskId);

        synchronized (SESSION_LOCK) {
            session.setAttribute("task" + taskId, tasks[taskId]);
        }

        checkLisParams(sourcedId, serviceUrl, session, taskId);

        TasksForm queryForm = new TasksForm();
        model.put("query", queryForm);

        return PATH_TO_MAIN_PAGE;
    }

    public String renderResult(String labId, HttpServletRequest request,
                               Map<String, Object> model, TasksForm queryForm, int taskId) {
        /**  вставляем параметры, необходимые для рендера страницы в мапу  */
        model.put("numberOfLab", labId);

        Task task = (Task) request.getSession().getAttribute("task" + taskId);
        task.setAnswer(queryForm.getTextQuery());

        task.setComplete(!(task.getAnswer() == null || task.getAnswer().isEmpty()));

        service.checkTasks(task);

        model.put("queryText", getSQLStringWithComments(task));

        model.put("rating", String.format("%.2f", task.getRating() * 100) + "%");

        try {
            String serviceUrl = (String) request.getSession().getAttribute(LIS_OUTCOME_URL_NAME + taskId);
            String sourcedId = (String) request.getSession().getAttribute(LIS_SOURCED_ID_NAME + taskId);

            log.debug("Push score to URL: " + serviceUrl);

            int result = scoreSender.push(sourcedId, serviceUrl, task.getRating());
            log.debug("RETURN CODE = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PATH_TO_RESULTS_PAGE;
    }

    public void checkLisParams(String sourcedId, String outcomeUrl, HttpSession session, int taskId) {
        String sessionSourcedId = (String) session.getAttribute(LIS_SOURCED_ID_NAME + taskId);
        String sessionOutcomeUrl = (String) session.getAttribute(LIS_OUTCOME_URL_NAME + taskId);
        synchronized (SESSION_LOCK) {
            if (sourcedId != null && !sourcedId.equals(sessionSourcedId)) {
                session.setAttribute(LIS_SOURCED_ID_NAME + taskId, sourcedId);
            }
            if (outcomeUrl != null && !outcomeUrl.equals(sessionOutcomeUrl)) {
                session.setAttribute(LIS_OUTCOME_URL_NAME + taskId, outcomeUrl);
            }
        }
    }

    // TODO move to Task.class or some TaskUtil

    private String getSQLStringWithComments(Task task) {
        return new StringBuilder()
                .append("# \n# ")
                .append(task.getQuestion())
                .append("\n# \n# ")
                .append((task.getRating() < 0.1f) ? "Ответ неверный" : "Ответ верный")
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
