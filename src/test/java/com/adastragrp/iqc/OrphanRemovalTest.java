package com.adastragrp.iqc;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.repository.AnswerRepository;
import com.adastragrp.iqc.repository.InterviewRepository;
import com.adastragrp.iqc.repository.QuestionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfig.class)
@WebAppConfiguration
public class OrphanRemovalTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private InterviewRepository interviewRepository;


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.answerRepository.deleteAll();
        this.questionRepository.deleteAll();
        this.interviewRepository.deleteAll();

    }

    @Test
    public void NotFound() throws Exception {
        mockMvc.perform(get("api/interviews/1")
                .contentType(contentType))
                .andExpect(status().isNotFound())
                .andDo(print());        //print response to console if you want to see it
    }

    @Test
    public void OrphanRemovalViaRest() throws Exception {

        final List<Answer> answers = new ArrayList<>();
        final List<Question> questions = new ArrayList<>();


        mockMvc
                .perform(post("/api/questions").contentType(contentType)
                        .content(toJson(new Question().setText("text").setPrivateText("private text").setPosition(1))))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.text", is("text")))
                .andExpect(jsonPath("$.privateText", is("private text")))
                .andExpect(jsonPath("$.questionType", is(Question.QuestionType.CHECKBOX.toString())))

                .andDo((MvcResult r) -> {
                    questions.add(toObject(r.getResponse().getContentAsString(), Question.class));
                });


        String answerJson1 = toJson(new Answer().setText("answer 1").setQuestion(questions.get(0)).setRight(true));
        answerJson1 = answerJson1.replaceFirst("\\{", "\\{ \"question\" : \"/api/questions/" + questions.get(0).getId() + "\",");   // TODO how come that it's not set by prev method

        mockMvc
                .perform(post("/api/answers").contentType(contentType).content(answerJson1))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.text", is("answer 1")))

                .andDo((MvcResult r) -> {
                    answers.add(toObject(r.getResponse().getContentAsString(), Answer.class));
                });


        String answerJson2 = toJson(new Answer().setText("answer 2").setQuestion(questions.get(0)).setRight(false));       // TODO how come that it's not set by prev method
        answerJson2 = answerJson2.replaceFirst("\\{", "\\{ \"question\" : \"/api/questions/" + questions.get(0).getId() + "\",");

        mockMvc
                .perform(post("/api/answers").contentType(contentType).content(answerJson2))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.text", is("answer 2")))

                .andDo((MvcResult r) -> {
                    answers.add(toObject(r.getResponse().getContentAsString(), Answer.class));
                });


        mockMvc.perform(delete("/api/questions/" + questions.get(0).getId()).contentType(contentType))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("api/questions/" + questions.get(0).getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("api/answers/" + answers.get(0).getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("api/answers/" + answers.get(1).getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());


    }

    @Test
    @Transactional
    public void OrphanRemovalViaRepository() throws Exception {

        Question question = questionRepository.save(new Question().setText("text").setPrivateText("private text"));
        Answer answer1 = answerRepository.save(new Answer().setText("answer 1").setQuestion(question).setRight(true));
        Answer answer2 = answerRepository.save(new Answer().setText("answer 2").setQuestion(question).setRight(true));

        questionRepository.delete(question);

        mockMvc.perform(get("api/questions/" + question.getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("api/answers/" + answer1.getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("api/answers/" + answer2.getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());

    }




    protected String toJson(final Object o) throws IOException {

        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected <T> T toObject(String json, final Class<T> clazz) throws IOException {

        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(json.getBytes());
        return (T) this.mappingJackson2HttpMessageConverter.read(clazz, mockHttpInputMessage);

    }
}