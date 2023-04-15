package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.AllUser.*;


public class AuthorizationTest {
    @BeforeEach
    void setUpAll() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void shouldAuthorizationValidActiveUser() {
        SelenideElement form = $("[action='/']");
        DataGenerator.DataInfo user = addActiveUser();
        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$("[data-test-id='action-login']").click();
        $x("//h2[contains(text(),'Личный')]").shouldBe(Condition.visible);
    }

    @Test
    void shouldNotAuthorizationValidBlockedUser() {
        SelenideElement form = $("[action='/']");
        DataGenerator.DataInfo user = addBlockedUser();
        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible).shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotAuthorizationNotRegisteredUser() {
        SelenideElement form = $("[action='/']");
        DataGenerator.DataInfo user = notRegisteredUser();
        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible).shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotAuthorizationUserInvalidLogin() {
        SelenideElement form = $("[action='/']");
        DataGenerator.DataInfo user = invalidLoginUser();
        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible).shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotAuthorizationUserInvalidPassword() {
        SelenideElement form = $("[action='/']");
        DataGenerator.DataInfo user = invalidPasswordUser();
        form.$("[data-test-id='login'] input").setValue(user.getLogin());
        form.$("[data-test-id='password'] input").setValue(user.getPassword());
        form.$("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.visible).shouldHave(text("Неверно указан логин или пароль"));
    }
}
