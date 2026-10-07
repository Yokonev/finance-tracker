package com.yokonev.fintrack.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.yokonev.fintrack.dto.AccountResponse;
import com.yokonev.fintrack.entity.Account;
import com.yokonev.fintrack.entity.AppUser;
import com.yokonev.fintrack.entity.value.Money;
import com.yokonev.fintrack.repository.AccountRepository;
import com.yokonev.fintrack.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private static final Long USER_ID = 42L;
    private static final Long ACCOUNT_ID = 7L;
    private static final Long UNKNOWN_ID = 99L;
    private static final String ACCOUNT_NAME = "Checking";
    private static final String BALANCE = "100.00";
    private static final String CURRENCY = "EUR";

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AppUser user;
    private Account account;

    @BeforeEach
    void setUp() {
        user = new AppUser("alice", "alice@example.com", "$2a$10$hashedpassword");
        ReflectionTestUtils.setField(user, "id", USER_ID);

        account = new Account(money(BALANCE, CURRENCY), ACCOUNT_NAME, user);
        ReflectionTestUtils.setField(account, "id", ACCOUNT_ID);
    }

    private static Money money(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }

    @Nested
    class GetAccountByIdTest {

        @Test
        @DisplayName("An account owned by the user maps correctly to a response")
        void mapsAccountToResponseTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When
            AccountResponse response = accountService.getAccountById(USER_ID, ACCOUNT_ID);

            // Then
            assertEquals(new AccountResponse(ACCOUNT_ID, ACCOUNT_NAME, new BigDecimal(BALANCE), CURRENCY), response);
        }

        @Test
        @DisplayName("Throws EntityNotFoundException when the account does not exist or is not owned by the user")
        void throwsEntityNotFoundWhenAccountNotOwnedTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, UNKNOWN_ID)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(EntityNotFoundException.class,
                () -> accountService.getAccountById(UNKNOWN_ID, ACCOUNT_ID)
            );
        }
    }

    @Nested
    class GetAccountsFromUserTest {

        @Test
        @DisplayName("Every account owned by the user maps correctly to a response")
        void mapsAllAccountsToResponsesTest() {
            // Given
            Account savings = new Account(money("2500.50", "USD"), "Savings", user);
            ReflectionTestUtils.setField(savings, "id", 8L);
            when(accountRepo.findAllByOwnerId(USER_ID)).thenReturn(List.of(account, savings));

            // When
            List<AccountResponse> responses = accountService.getAccountsFromUser(USER_ID);

            // Then
            assertEquals(List.of(
                new AccountResponse(ACCOUNT_ID, ACCOUNT_NAME, new BigDecimal(BALANCE), CURRENCY),
                new AccountResponse(8L, "Savings", new BigDecimal("2500.50"), "USD")
            ), responses);
        }

        @Test
        @DisplayName("Returns an empty list when the user has no accounts")
        void returnsEmptyListWhenNoAccountsTest() {
            // Given
            when(accountRepo.findAllByOwnerId(USER_ID)).thenReturn(List.of());

            // When
            List<AccountResponse> responses = accountService.getAccountsFromUser(USER_ID);

            // Then
            assertTrue(responses.isEmpty());
        }
    }

    @Nested
    class CreateAccountTest {

        @Test
        @DisplayName("Saves a new account owned by the user and maps it to a response")
        void savesAccountAndMapsToResponseTest() {
            // Given
            when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));
            when(accountRepo.save(any(Account.class))).thenAnswer(invocation -> {
                Account saved = invocation.getArgument(0);
                ReflectionTestUtils.setField(saved, "id", ACCOUNT_ID); // Normally generated by JPA
                return saved;
            });

            // When
            AccountResponse response = accountService.createAccount(USER_ID, ACCOUNT_NAME, BALANCE, CURRENCY);

            // Then
            ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepo).save(captor.capture());
            assertSame(user, captor.getValue().getOwner());
            assertEquals(new AccountResponse(ACCOUNT_ID, ACCOUNT_NAME, new BigDecimal(BALANCE), CURRENCY), response);
        }

        @Test
        @DisplayName("Throws EntityNotFoundException and saves nothing when the user does not exist")
        void throwsEntityNotFoundWhenUserDoesNotExistTest() {
            // Given
            when(userRepo.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(EntityNotFoundException.class,
                () -> accountService.createAccount(UNKNOWN_ID, ACCOUNT_NAME, BALANCE, CURRENCY)
            );
            verify(accountRepo, never()).save(any());
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when the start balance is not a number")
        void throwsIllegalArgumentWhenBalanceInvalidTest() {
            // Given
            when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

            // When / Then
            assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(USER_ID, ACCOUNT_NAME, "not-a-number", CURRENCY)
            );
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when the currency code is unknown")
        void throwsIllegalArgumentWhenCurrencyInvalidTest() {
            // Given
            when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

            // When / Then
            assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(USER_ID, ACCOUNT_NAME, BALANCE, "XYZ")
            );
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when the account name is blank")
        void throwsIllegalArgumentWhenNameBlankTest() {
            // Given
            when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

            // When / Then
            assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(USER_ID, "  ", BALANCE, CURRENCY)
            );
        }
    }

    @Nested
    class UpdateAccountNameTest {

        @Test
        @DisplayName("Renames the account and returns the updated response")
        void renamesAccountTest() {
            // Given
            String newName = "Main";
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When
            AccountResponse response = accountService.updateAccountName(USER_ID, ACCOUNT_ID, newName);

            // Then
            assertEquals(newName, account.getAccountName());
            assertEquals(new AccountResponse(ACCOUNT_ID, newName, new BigDecimal(BALANCE), CURRENCY), response);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException and keeps the old name when the new name is blank")
        void throwsIllegalArgumentWhenNameBlankTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When / Then
            assertThrows(IllegalArgumentException.class,
                () -> accountService.updateAccountName(USER_ID, ACCOUNT_ID, "  ")
            );
            assertEquals(ACCOUNT_NAME, account.getAccountName());
        }

        @Test
        @DisplayName("Throws EntityNotFoundException when the account is not owned by the user")
        void throwsEntityNotFoundWhenAccountNotOwnedTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, UNKNOWN_ID)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(EntityNotFoundException.class,
                () -> accountService.updateAccountName(UNKNOWN_ID, ACCOUNT_ID, "Main")
            );
        }
    }

    @Nested
    class UpdateAccountBalanceTest {

        @Test
        @DisplayName("Updates the balance and keeps the currency")
        void updatesBalanceKeepsCurrencyTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When
            AccountResponse response = accountService.updateAccountBalance(USER_ID, ACCOUNT_ID, "250.5");

            // Then
            assertEquals(new BigDecimal("250.50"), account.getAmount().getAmount());
            assertEquals(new AccountResponse(ACCOUNT_ID, ACCOUNT_NAME, new BigDecimal("250.50"), CURRENCY), response);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException and keeps the old balance when the balance is not a number")
        void throwsIllegalArgumentWhenBalanceInvalidTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When / Then
            assertThrows(IllegalArgumentException.class,
                () -> accountService.updateAccountBalance(USER_ID, ACCOUNT_ID, "not-a-number")
            );
            assertEquals(new BigDecimal(BALANCE), account.getAmount().getAmount());
        }

        @Test
        @DisplayName("Throws EntityNotFoundException when the account is not owned by the user")
        void throwsEntityNotFoundWhenAccountNotOwnedTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, UNKNOWN_ID)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(EntityNotFoundException.class,
                () -> accountService.updateAccountBalance(UNKNOWN_ID, ACCOUNT_ID, "250.50")
            );
        }
    }

    @Nested
    class UpdateAccountCurrencyTest {

        @Test
        @DisplayName("Updates the currency and keeps the balance")
        void updatesCurrencyKeepsBalanceTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When
            AccountResponse response = accountService.updateAccountCurrency(USER_ID, ACCOUNT_ID, "USD");

            // Then
            assertEquals(Currency.getInstance("USD"), account.getAmount().getCurrency());
            assertEquals(new AccountResponse(ACCOUNT_ID, ACCOUNT_NAME, new BigDecimal(BALANCE), "USD"), response);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException and keeps the old currency when the currency code is unknown")
        void throwsIllegalArgumentWhenCurrencyInvalidTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When / Then
            assertThrows(IllegalArgumentException.class,
                () -> accountService.updateAccountCurrency(USER_ID, ACCOUNT_ID, "XYZ")
            );
            assertEquals(Currency.getInstance(CURRENCY), account.getAmount().getCurrency());
        }

        @Test
        @DisplayName("Throws EntityNotFoundException when the account is not owned by the user")
        void throwsEntityNotFoundWhenAccountNotOwnedTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, UNKNOWN_ID)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(EntityNotFoundException.class,
                () -> accountService.updateAccountCurrency(UNKNOWN_ID, ACCOUNT_ID, "USD")
            );
        }
    }

    @Nested
    class DeleteAccountTest {

        @Test
        @DisplayName("Deletes the account when it is owned by the user")
        void deletesOwnedAccountTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, USER_ID)).thenReturn(Optional.of(account));

            // When
            accountService.deleteAccount(USER_ID, ACCOUNT_ID);

            // Then
            verify(accountRepo).delete(account);
        }

        @Test
        @DisplayName("Throws EntityNotFoundException and deletes nothing when the account is not owned by the user")
        void throwsEntityNotFoundWhenAccountNotOwnedTest() {
            // Given
            when(accountRepo.findByIdAndOwnerId(ACCOUNT_ID, UNKNOWN_ID)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(EntityNotFoundException.class,
                () -> accountService.deleteAccount(UNKNOWN_ID, ACCOUNT_ID)
            );
            verify(accountRepo, never()).delete(any());
        }
    }

}
