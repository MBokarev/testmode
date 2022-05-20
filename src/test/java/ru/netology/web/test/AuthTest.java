package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    @BeforeAll
    static void setUpAll() {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(DataHelper.PresetUser.generatePresetUserStatusActive()) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    static void setUpBlockedUser() {
        given()
                .spec(requestSpec)
                .body(DataHelper.PresetUser.generatePresetUserStatusBlocked())
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldGoToPersonalAccountPage() {
        Configuration.holdBrowserOpen = true;

        open("http://localhost:9999/");
        var validUser = DataHelper.Registration.generateValidUser();
        $("[class=\"App_appContainer__3jRx1\"]").shouldBe(visible);
    }

    @Test
    public void shouldShowErrorUserStatusBlocked() {
        setUpBlockedUser();
        Configuration.holdBrowserOpen = true;

        open("http://localhost:9999/");
        var validUser = DataHelper.Registration.generateValidUser();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible);
        $("[class=\"notification__content\"]").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldShowErrorInvalidLogin() {
        Configuration.holdBrowserOpen = true;

        open("http://localhost:9999/");
        var validUser = DataHelper.Registration.generateInvalidLoginUser();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible);
        $("[class=\"notification__content\"]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldShowErrorInvalidPassword() {
        Configuration.holdBrowserOpen = true;

        open("http://localhost:9999/");
        var validUser = DataHelper.Registration.generateInvalidPasswordUser();
        $("[data-test-id=\"error-notification\"]").shouldBe(visible);
        $("[class=\"notification__content\"]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}