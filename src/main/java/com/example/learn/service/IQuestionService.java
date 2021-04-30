package com.example.learn.service;

import com.example.learn.entity.Question;
import com.example.learn.vo.QuestionTagVo;

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

    /**
     * 根据id查询提问帖详情
     * @param id
     * @param userId
     * @return
     */
    QuestionTagVo queryQuestionById(int id,int userId);

    /**
     * 提问帖点赞
     * @param id
     * @param userId
     * @return
     */
    int giveLike(int id,int userId);

    /**
     * 提问帖收藏
     * @param id
     * @param userId
     * @return
     */
    int giveCollect(int id,int userId);
}
