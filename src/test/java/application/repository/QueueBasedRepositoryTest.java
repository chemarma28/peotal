package application.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import finance.domain.Contact;
import finance.repository.QueueBasedRepository;
import finance.repository.errors.SuchElementOccurenceException;

public class QueueBasedRepositoryTest {
    QueueBasedRepository<Long, Contact> repo = new QueueBasedRepository<>();

    @BeforeEach
    void put() {
        Contact contact = Contact.builder().id(1L).firstName("Peter").build();

        try {
            repo.put(contact);
        } catch (SuchElementOccurenceException e) {
            Assertions.fail("Unexpected behaviour");
        }
    }

    @Test
    void when_findById_then_returnContactWithTheSameId() {
        Contact res = repo.find(1L);

        Assertions.assertEquals(1L, res.id);

        Assertions.assertEquals("Peter", res.firstName);
    }

    @Test
    void when_pull_then_returnEntityAndDeleteItFromTheRepository() {
        Contact res = repo.pull(1L);

        Assertions.assertEquals(1L, res.id);

        Assertions.assertEquals("Peter", res.firstName);
    }

    @Test
    void when_putEntityWithNullableId_then_throwException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            repo.put(Contact.builder().build());
        });
    }
}
