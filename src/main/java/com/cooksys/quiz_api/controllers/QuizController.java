package com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

	private final QuizService quizService;

	@GetMapping
	public List<QuizResponseDto> getAllQuizzes() {
		return quizService.getAllQuizzes();
	}

	// TODO: Implement the remaining 6 endpoints from the documentation.
	@PostMapping
	public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
		return quizService.createQuiz(quizRequestDto);
	}

	@DeleteMapping("/{id}")
	public QuizResponseDto deleteQuiz(@PathVariable Long id) {
		return quizService.deleteQuiz(id);
	}

	@PatchMapping("/{id}/rename/{newName}")
	public QuizResponseDto patchQuizName(@PathVariable Long id, @PathVariable String newName) {
		return quizService.patchQuizName(id, newName);
	}

	@GetMapping("/{id}/random")
	public QuestionResponseDto getQuizRandomQuestion(@PathVariable Long id) {
		return quizService.getRandomQuestion(id);
	}
	
	@PatchMapping("/{id}/add")
	public QuizResponseDto patchQuizAddQuestion(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto) {
		return quizService.patchQuizAddQuestion(id, questionRequestDto);
	}
	
	@DeleteMapping("/{id}/delete/{questionID}")
	public QuestionResponseDto deleteQuestion(@PathVariable Long id, @PathVariable Long questionID) {
		return quizService.deleteQuestion(id, questionID);
	}
}
