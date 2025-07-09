package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BidTests {

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    public void bidListTest() {
        BidList bid = BidList.builder()
                .account("Account Test")
                .type("Type Test")
                .bidQuantity(10d)
                .build();

        // Save
        bid = bidListRepository.save(bid);
        Assertions.assertNotNull(bid.getId());

        // Update
        bid.setBidQuantity(20d);
        bid = bidListRepository.save(bid);
        Assertions.assertEquals(20d, bid.getBidQuantity());

        // Find
        Iterable<BidList> listResult = bidListRepository.findAll();
        Assertions.assertTrue(listResult.iterator().hasNext());

        // Delete
        bidListRepository.delete(bid);
        Assertions.assertFalse(bidListRepository.findById(bid.getId()).isPresent());
    }
}
