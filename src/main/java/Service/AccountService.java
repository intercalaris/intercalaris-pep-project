package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();
    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty() 
            || account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        if (accountDAO.findAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        return accountDAO.createAccount(account);
    }
    public Account validateLogin(Account account) {
        Account storedAccount = accountDAO.findAccountByUsername(account.getUsername());
        if (storedAccount != null && storedAccount.getPassword().equals(account.getPassword())) {
            return storedAccount;
        }
        return null;
    }
}
