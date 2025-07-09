package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RatingTests {

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void ratingTest() {
		Rating rating = Rating.builder()
			.moodysRating("Moodys Rating")
			.sandPRating("Sand PRating")
			.fitchRating("Fitch Rating")
			.orderNumber(10)
			.build();

		// Save
		rating = ratingRepository.save(rating);

		// Find
		Optional<Rating> ratingList = ratingRepository.findById(rating.getId());
		Assertions.assertTrue(ratingList.isPresent());

		// Update
		Rating ratingResult = ratingList.get();
		ratingResult.setOrderNumber(20);
		ratingResult = ratingRepository.save(ratingResult);
		Assertions.assertEquals(20, ratingResult.getOrderNumber());

		// Find All
		List<Rating> listResult = ratingRepository.findAll();
		Assertions.assertFalse(listResult.isEmpty());

		// Delete
		ratingRepository.delete(ratingResult);
		Optional<Rating> deleted = ratingRepository.findById(ratingResult.getId());
		Assertions.assertFalse(deleted.isPresent());
	}
}
