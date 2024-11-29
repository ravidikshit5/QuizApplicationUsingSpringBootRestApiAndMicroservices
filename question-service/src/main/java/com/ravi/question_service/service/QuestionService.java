package com.ravi.question_service.service;


import com.ravi.question_service.dto.QuestionDTO;
import com.ravi.question_service.dto.ResponseDTO;
import com.ravi.question_service.entity.Question;
import com.ravi.question_service.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category);
    }

    public String addQuestion(Question question) {
        questionRepository.save(question);
        return "success";
    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionRepository.findRandomQuestionByCategory(categoryName,numQuestions);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionDTO>> getAllQuestionById(List<Integer> questionIds) {
        //List<Question> questions = questionRepository.findAllById(questionIds);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for(Integer id:questionIds){
            questions.add(questionRepository.findById(id).get());
        }

        for (Question question:questions){
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(question.getId());
            questionDTO.setQuestionTitle(question.getQuestionTitle());
            questionDTO.setOption1(question.getOption1());
            questionDTO.setOption2(question.getOption2());
            questionDTO.setOption3(question.getOption3());
            questionDTO.setOption4(question.getOption4());
            questionDTOS.add(questionDTO);
        }
        return new ResponseEntity<>(questionDTOS,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<ResponseDTO> responses) {

        int rightCount = 0;

        for (ResponseDTO r:responses){
            Question question = questionRepository.findById(r.getId()).get();
            if(r.getResponse().equals(question.getRightAnswer())) {
                rightCount++;
            }
        }
        return new ResponseEntity<>(rightCount,HttpStatus.OK);
    }
}
