package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    //Add account
    public Account addAccount(Account account){
        //check blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException();
        }
        //check pass length
        if (account.getPassword() == null || account.getPassword().length() < 4){
            throw new IllegalArgumentException();
        }
        //check dupliate username
        if (accountDAO.usernameExists(account.getUsername())) {
            throw new IllegalArgumentException();
        }

        return accountDAO.insertAccount(account);
    }

    //Login account
    public Account loginAccount(Account account){
        //if (accountDAO.loginAccount(account.getAccount_id()) == null){
            //accountDAO.loginAccount(account);
            
        return accountDAO.loginAccount(account);
       // }else return null;
    }
}
