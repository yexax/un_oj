package com.example.backend.mapper;

import com.example.backend.model.entities.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface QuestionMapper {
    boolean addQuestion(Question question);
    Question getQuestionById(Long id);
    Boolean updateQuestionById(Question question);
    Boolean deleteQuestionById(Long id);
    List<Question> listQuestionByPage(@Param("x") Integer x, @Param("y") Integer y);
    Integer getCount();
    List<Question> searchQuestionByPage(@Param("x") Integer x, @Param("y") Integer y,
                                     @Param("title") String title,@Param("tags")String tags);
    Integer getCountBySearch(@Param("title") String title,@Param("tags")String tags);
    Boolean statistics(@Param("id") Long questionId,@Param("isAccept")int isAccept);
}
