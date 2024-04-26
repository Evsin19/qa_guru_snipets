package selenide;

import com.codeborne.selenide.*;
import org.openqa.selenium.*;

import java.io.*;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

// this is not a full list, just the most common
public class Snippets {

  void browser_command_examples() {
    open("https://google.com");
    open("/customer/orders");     // -Dselenide.baseUrl=http://123.23.23.1
    open("/", AuthenticationType.BASIC,
            new BasicAuthCredentials("", "user", "password"));

    Selenide.back(); // Страница назад
    Selenide.refresh(); // Обновление страницы

    Selenide.clearBrowserCookies(); // Очищаем куки
    Selenide.clearBrowserLocalStorage(); // Очищает историю
    executeJavaScript("sessionStorage.clear();"); // no Selenide command for this yet

    Selenide.confirm(); // Нажать оке на алерте
    Selenide.dismiss(); // Нажать кнопку отменить на алерте

    Selenide.closeWindow(); // Закрыть окно браузера
    Selenide.closeWebDriver(); // Закрыть браузер

    Selenide.switchTo().frame("new"); // Переход во фрейм
    Selenide.switchTo().defaultContent(); // Возвращаемся к главному дереву страницы

    Selenide.switchTo().window("The Internet");

    var cookie = new Cookie("foo", "bar"); // Добавляем куки
    WebDriverRunner.getWebDriver().manage().addCookie(cookie);


  }

  void selectors_examples() {
    $("div").click(); // Поиск элементов
    element("div").click();

    $("div", 2).click(); // Ищет определенное вхождение

    $x("//h1/div").click();
    $(byXpath("//h1/div")).click();

    $(byText("full text")).click();
    $(withText("ull tex")).click();

    $(byTagAndText("div", "full text"));
    $(withTagAndText("div", "ull text"));

    $("").parent(); // родительский элемент
    $("").sibling(1); // вниз по дереву
    $("").preceding(1); // вверх по дереву
    $("").closest("div"); // ищем пердка
    $("").ancestor("div"); // the same as closest
    $("div:last-child"); // поиск последнего ребенка

    $("div").$("h1").find(byText("abc")).click(); // поиск по илементам
    // very optional
    $(byAttribute("abc", "x")).click();
    $("[abc=x]").click();

    $(byId("mytext")).click();
    $("#mytext").click();

    $(byClassName("red")).click();
    $(".red").click();
  }

  void actions_examples() {
    $("").click();
    $("").doubleClick();
    $("").contextClick(); // правый клик

    $("").hover(); // навести мышкой

    $("").setValue("text");// записываем текст
    $("").append("text"); //вставляет текс (не стирает)
    $("").clear();
    $("").setValue(""); // очищают поле

    $("div").sendKeys("c"); // симулировать нажатие клавишь в поле
    actions().sendKeys("c").perform(); // нажатие клавишь
    actions().sendKeys(Keys.chord(Keys.CONTROL, "f")).perform(); // Ctrl + F послать комбинацию клавишь
    $("html").sendKeys(Keys.chord(Keys.CONTROL, "f"));

    $("").pressEnter();
    $("").pressEscape();
    $("").pressTab();


    // complex actions with keybord and mouse, example
    actions().moveToElement($("div")).clickAndHold().moveByOffset(300, 200).release().perform();

    // old html actions don't work with many modern frameworks
    $("").selectOption("dropdown_option");// выбираем дропдаун
    $("").selectRadio("radio_options");

  }

  void assertions_examples() { // проверки
    $("").shouldBe(visible);
    $("").shouldNotBe(visible);
    $("").shouldHave(text("abc"));
    $("").shouldNotHave(text("abc"));
    $("").should(appear);
    $("").shouldNot(appear);


    //longer timeouts
    $("").shouldBe(visible, Duration.ofSeconds(30));// увеличиваем таймаут

  }

  void conditions_examples() {
    $("").shouldBe(visible);// проверка на наличие
    $("").shouldBe(hidden); // проверка на отсутствие

    $("").shouldHave(text("abc")); // вхождение
    $("").shouldHave(exactText("abc")); // только текс
    $("").shouldHave(textCaseSensitive("abc")); // кейс текста
    $("").shouldHave(exactTextCaseSensitive("abc"));
    $("").should(matchText("[0-9]abc$")); // формат

    $("").shouldHave(cssClass("red")); // проверка класса
    $("").shouldHave(cssValue("font-size", "12")); // проверка проперти

    $("").shouldHave(value("25"));// для поля
    $("").shouldHave(exactValue("25"));
    $("").shouldBe(empty);// поле пустое

    $("").shouldHave(attribute("disabled"));
    $("").shouldHave(attribute("name", "example"));
    $("").shouldHave(attributeMatching("name", "[0-9]abc$"));

    $("").shouldBe(checked); // for checkboxes

    // Warning! Only checks if it is in DOM, not if it is visible! You don't need it in most tests!
    $("").should(exist); // наличие элемента в дереве

    // Warning! Checks only the "disabled" attribute! Will not work with many modern frameworks
    $("").shouldBe(disabled); // кликабельность если есть атрибут
    $("").shouldBe(enabled);
  }

  void collections_examples() {

    $$("div"); // does nothing!

    $$x("//div"); // by XPath

    // selections
    $$("div").filterBy(text("123")).shouldHave(size(1));// фильтрация
    $$("div").excludeWith(text("123")).shouldHave(size(1));// которые не содержжат

    $$("div").first().click(); // переход к элементам
    elements("div").first().click();
    // $("div").click();
    $$("div").last().click();
    $$("div").get(1).click(); // the second! (start with 0)
    $("div", 1).click(); // same as previous
    $$("div").findBy(text("123")).click(); //  finds first

    // assertions
    $$("").shouldHave(size(0));
    $$("").shouldBe(CollectionCondition.empty); // the same

    $$("").shouldHave(texts("Alfa", "Beta", "Gamma"));
    $$("").shouldHave(exactTexts("Alfa", "Beta", "Gamma"));

    $$("").shouldHave(textsInAnyOrder("Beta", "Gamma", "Alfa"));
    $$("").shouldHave(exactTextsCaseSensitiveInAnyOrder("Beta", "Gamma", "Alfa"));

    $$("").shouldHave(itemWithText("Gamma")); // only one text

    $$("").shouldHave(sizeGreaterThan(0));
    $$("").shouldHave(sizeGreaterThanOrEqual(1));
    $$("").shouldHave(sizeLessThan(3));
    $$("").shouldHave(sizeLessThanOrEqual(2));


  }

  void file_operation_examples() throws FileNotFoundException {

    File file1 = $("a.fileLink").download(); // only for <a href=".."> links
    File file2 = $("div").download(DownloadOptions.using(FileDownloadMode.FOLDER)); // more common options, but may have problems with Grid/Selenoid

    File file = new File("src/test/resources/readme.txt");
    $("#file-upload").uploadFile(file);
    $("#file-upload").uploadFromClasspath("readme.txt");
    // don't forget to submit!
    $("uploadButton").click();
  }

  void javascript_examples() {
    executeJavaScript("alert('selenide')");
    executeJavaScript("alert(arguments[0]+arguments[1])", "abc", 12);
    long fortytwo = executeJavaScript("return arguments[0]*arguments[1];", 6, 7);

  }
}

