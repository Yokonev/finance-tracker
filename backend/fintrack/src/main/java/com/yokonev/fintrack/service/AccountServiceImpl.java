package com.yokonev.fintrack.service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yokonev.fintrack.dto.AccountResponse;
import com.yokonev.fintrack.entity.Account;
import com.yokonev.fintrack.entity.AppUser;
import com.yokonev.fintrack.entity.value.Money;
import com.yokonev.fintrack.repository.AccountRepository;
import com.yokonev.fintrack.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * 
 * <p> Account service implementation. </p>
 * 
 * Allows to create, update, delete and get accounts for a user. 
 * The security context gives the ID of the user in the controller layer.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepo;
    private final UserRepository userRepo;

    public AccountServiceImpl(AccountRepository accountRepo, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long userId, Long accountId) {
        return toResponse(findOwnedAccount(userId, accountId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsFromUser(Long userId) {
        return accountRepo.findAllByOwnerId(userId).stream()
            .map(AccountServiceImpl::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public AccountResponse createAccount(Long userId, String accountName, String startBalance, String currency) {
        AppUser owner = userRepo.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("No user with ID " + userId));
        Money amount = new Money(new BigDecimal(startBalance), Currency.getInstance(currency));
        return toResponse(accountRepo.save(new Account(amount, accountName, owner)));
    }

    @Override
    @Transactional
    public AccountResponse updateAccountName(Long userId, Long accountId, String newName) {
        Account account = findOwnedAccount(userId, accountId);
        account.rename(newName);
        return toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse updateAccountBalance(Long userId, Long accountId, String newBalance) {
        Account account = findOwnedAccount(userId, accountId);
        account.setAmount(new Money(new BigDecimal(newBalance), account.getAmount().getCurrency()));
        return toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse updateAccountCurrency(Long userId, Long accountId, String newCurrency) {
        Account account = findOwnedAccount(userId, accountId);
        account.setAmount(new Money(account.getAmount().getAmount(), Currency.getInstance(newCurrency)));
        return toResponse(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Long userId, Long accountId) {
        accountRepo.delete(findOwnedAccount(userId, accountId));
    }

    /**
     * Fetches an account only if it belongs to the given user, so a user can never
     * read or modify another user's account by guessing its ID.
     */
    private Account findOwnedAccount(Long userId, Long accountId) {
        return accountRepo.findByIdAndOwnerId(accountId, userId)
            .orElseThrow(() -> new EntityNotFoundException("No account with ID " + accountId));
    }

    /**
     * Converts an Account entity to an account response DTO.
     * @param account The Account entity to be converted.
     * @return the DTO corresponding to the Account entity.
     */
    private static AccountResponse toResponse(Account account) {
        return new AccountResponse(
            account.getId(),
            account.getAccountName(),
            account.getAmount().getAmount(),
            account.getAmount().getCurrency().getCurrencyCode()
        );
    }

}
