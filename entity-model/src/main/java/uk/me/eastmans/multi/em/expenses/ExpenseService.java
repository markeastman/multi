package uk.me.eastmans.multi.em.expenses;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.me.eastmans.multi.em.kafka.PostKafkaEvent;
import uk.me.eastmans.multi.em.security.User;

import java.util.List;
import java.util.Optional;

@Service
@PreAuthorize("isAuthenticated()")
public class ExpenseService {

    private final ExpenseHeaderRepository expenseHeaderRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final PostKafkaEvent postKafkaEvent;

    ExpenseService(PostKafkaEvent postKafkaEvent,
                   ExpenseCategoryRepository expenseCategoryRepository,
                   ExpenseHeaderRepository expenseHeaderRepository) {
        this.postKafkaEvent = postKafkaEvent;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.expenseHeaderRepository = expenseHeaderRepository;
    }

    @Transactional
    public Optional<ExpenseHeader> getExpense(long id) {
        return expenseHeaderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ExpenseHeader> listAll(User user) {
        return expenseHeaderRepository.findAllByOwner(user);
    }

    @Transactional(readOnly = true)
    public List<ExpenseCategory> listAllCategories() {
        return expenseCategoryRepository.findAll();
    }

    @Transactional
    public void deleteExpense(ExpenseHeader expenseHeader) {
        expenseHeaderRepository.delete(expenseHeader);
        expenseHeaderRepository.flush();
        postKafkaEvent.postUpdate("ExpenseHeader", expenseHeader.getId() );}

    @Transactional
    public void saveOrCreate(ExpenseHeader expense) {
        expense.calculateTotalAmount();
        boolean create = expense.getId() == null;
        expenseHeaderRepository.saveAndFlush(expense);
        if (create) {
            postKafkaEvent.postCreate("ExpenseHeader", expense.getId() );
        } else {
            postKafkaEvent.postUpdate("ExpenseHeader", expense.getId() );
        }
    }

    @Transactional
    public void saveOrCreate(ExpenseCategory expenseCategory) {
        // We need to calculate the total expense amount from the lines
        boolean create = expenseCategory.getId() == null;
        expenseCategoryRepository.saveAndFlush(expenseCategory);
        if (create) {
            postKafkaEvent.postCreate("ExpenseCategory", expenseCategory.getId() );
        } else {
            postKafkaEvent.postUpdate("ExpenseCategory", expenseCategory.getId() );
        }
    }
}