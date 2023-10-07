package jm.task.core.jdbc;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;


public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("John", "Smit", (byte) 28);
        userService.saveUser("Sara", "Jons", (byte) 27);
        userService.saveUser("Alan", "Cooper", (byte) 32);
        userService.saveUser("Melissa", "Mosby", (byte) 26);
        System.out.println(userService.getAllUsers().toString());
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeConnection();
    }
}