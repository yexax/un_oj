package com.example.backend.model.vo;

import com.example.backend.model.entities.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSubmitPageVO {
    List<QuestionSubmit>questionSubmitList;
    Integer total;
}
