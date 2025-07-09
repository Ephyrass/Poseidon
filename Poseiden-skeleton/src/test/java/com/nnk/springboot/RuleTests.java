package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	@Test
	public void ruleTest() {
		RuleName rule = RuleName.builder()
			.name("Rule Name")
			.description("Description")
			.json("Json")
			.template("Template")
			.sqlStr("SQL")
			.sqlPart("SQL Part")
			.build();

		// Save
		rule = ruleNameRepository.save(rule);
		Assertions.assertNotNull(rule.getId());
		Assertions.assertEquals("Rule Name", rule.getName());

		// Update
		rule.setName("Rule Name Update");
		rule = ruleNameRepository.save(rule);
		Assertions.assertEquals("Rule Name Update", rule.getName());

		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		Assertions.assertFalse(listResult.isEmpty());

		// Delete
		Integer id = rule.getId();
		ruleNameRepository.delete(rule);
		Optional<RuleName> ruleList = ruleNameRepository.findById(id);
		Assertions.assertFalse(ruleList.isPresent());
	}
}
