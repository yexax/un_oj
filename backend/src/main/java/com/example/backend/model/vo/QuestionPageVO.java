package com.example.backend.model.vo;

import com.example.backend.model.entities.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuestionPageVO {
    List<Question> questionList;
    Integer total;
}
