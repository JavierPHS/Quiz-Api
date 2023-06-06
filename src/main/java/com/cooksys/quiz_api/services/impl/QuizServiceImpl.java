package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Random;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizRepository quizRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final QuizMapper quizMapper;
	private final QuestionMapper questionMapper;

	@Override
	public List<QuizResponseDto> getAllQuizzes() {

		return quizMapper.entitiesToDtos(quizRepository.findAll());
	}

	@Override
	public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {

		Quiz quizToSave = quizMapper.requestDtoToEntity(quizRequestDto);
		quizRepository.saveAndFlush(quizToSave);

		List<Question> questions = quizRequestDto.getQuestions();
		for (Question q : questions) {
			questionRepository.saveAndFlush(q);
			q.setQuiz(quizToSave);
		}
		for (Question q : questions) {
			List<Answer> answers = q.getAnswers();
			for (Answer a : answers) {
				answerRepository.saveAndFlush(a);
				a.setQuestion(q);
			}
		}
		return quizMapper.entityToDto(quizRepository.saveAndFlush(quizToSave));

	}

	@Override
	public QuizResponseDto deleteQuiz(Long id) {

		Quiz quiz = quizRepository.getById(id);
		Quiz deletedQuiz = quiz;
//		try {
//			quizRepository.deleteById(id);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		List<Question> questions = quiz.getQuestions();
		for (Question q : questions) {
			q.setQuiz(null);
			questionRepository.saveAndFlush(q);
		}
		quizRepository.delete(quiz);

		return quizMapper.entityToDto(deletedQuiz);
	}

	@Override
	public QuizResponseDto patchQuizName(Long id, String name) {

		Quiz quiz = quizRepository.getById(id);
		quiz.setName(name);
		return quizMapper.entityToDto(quizRepository.saveAndFlush(quiz));

	}

	@Override
	public QuestionResponseDto getRandomQuestion(Long id) {

		Quiz quiz = quizRepository.getById(id);
		List<Question> questions = quiz.getQuestions();

		Random rand = new Random();
		Question question = questions.get(rand.nextInt(questions.size()));

		return questionMapper.entityToDto(question);
	}

	@Override
	public QuizResponseDto patchQuizAddQuestion(Long id, QuestionRequestDto questionRequestDto) {

		Question questionToAdd = questionMapper.requestDtoToEntity(questionRequestDto);

		Quiz quiz = quizRepository.getById(id);
		questionToAdd.setQuiz(quiz);
		questionRepository.saveAndFlush(questionToAdd);

		List<Answer> answers = questionToAdd.getAnswers();
		for (Answer a : answers) {
			a.setQuestion(questionToAdd);
			answerRepository.saveAndFlush(a);
		}

		return quizMapper.entityToDto(quizRepository.saveAndFlush(quiz));
	}

	@Override
	public QuestionResponseDto deleteQuestion(Long id, Long questionID) {

		Quiz quiz = quizRepository.getById(id);
		List<Question> questions = quiz.getQuestions();
		Question questionToBeRemoved = null;

//		try {
//			for (Question q : questions) {
//				if(questionID.equals(q.getId())) {
//					questions.remove(q);
//					questionToBeRemoved = q;
//				}
//				
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		for (Question q : questions) {
			if (questionID.equals(q.getId())) {
				questionToBeRemoved = q;
				q.setQuiz(null);
				break;
			}
		}
		questions.remove(questionToBeRemoved);
		quiz.setQuestions(questions);
		quizRepository.saveAndFlush(quiz);
		return questionMapper.entityToDto(questionToBeRemoved);
	}

}
