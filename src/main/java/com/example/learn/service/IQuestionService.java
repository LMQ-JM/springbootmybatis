package com.example.learn.service;

import com.example.learn.entity.Question;

/**
 * @author JC
 * @date 2021/4/29 10:41
 */
public interface IQuestionService {

    /**
     * 发布提问帖
     * @param question
     * @return
     */
    int addQuestion(Question question);
}
