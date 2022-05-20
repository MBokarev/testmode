package ru.netology.web.data;

import lombok.Value;

import static com.codeborne.selenide.Selenide.$;

public class DataHelper {
    private DataHelper() {
    }

    public static class PresetUser {
        private PresetUser() {
        }

        public static RegistrationDto generatePresetUserStatusActive() {
            RegistrationDto preset = new RegistrationDto("vasya", "password", "active");
            return preset;
        }

        public static RegistrationDto generatePresetUserStatusBlocked() {
            RegistrationDto preset = new RegistrationDto("vasya", "password", "blocked");
            return preset;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

    public static class Registration {
        private Registration() {
        }


        public static User generateValidUser() {
            User user = new User("vasya", "password");
            $("[name=\"login\"]").setValue(user.getLogin());
            $("[name=\"password\"]").setValue(user.getPassword());
            $("[data-test-id=\"action-login\"]").click();
            return user;
        }

        public static User generateInvalidLoginUser() {
            User user = new User("petya", "password");
            $("[name=\"login\"]").setValue(user.getLogin());
            $("[name=\"password\"]").setValue(user.getPassword());
            $("[data-test-id=\"action-login\"]").click();
            return user;
        }

        public static User generateInvalidPasswordUser() {
            User user = new User("vasya", "qwerty123");
            $("[name=\"login\"]").setValue(user.getLogin());
            $("[name=\"password\"]").setValue(user.getPassword());
            $("[data-test-id=\"action-login\"]").click();
            return user;
        }
    }

    @Value
    public static class User {
        String login;
        String password;
    }
}