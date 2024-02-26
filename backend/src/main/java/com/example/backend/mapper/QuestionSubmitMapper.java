package com.example.backend.mapper;

import com.example.backend.model.entities.QuestionSubmit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionSubmitMapper {
    long questionSubmit(QuestionSubmit questionSubmit);
    List<QuestionSubmit> listQuestionSubmit(@Param("x")Integer x, @Param("y")Integer y);
    Integer getCount();
    QuestionSubmit getById(long id);
    Boolean updateStatus(@Param("id")long id,@Param("status")String status);
}
