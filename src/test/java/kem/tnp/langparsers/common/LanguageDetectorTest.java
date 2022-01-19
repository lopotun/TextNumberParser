package kem.tnp.langparsers.common;

import kem.tnp.common.LanguageDetector;
import kem.tnp.common.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Evgeny Kurtser on 13-Jan-22 at 9:35 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
class LanguageDetectorTest {

	@Test
	void loadLanguagesWords() {
	}

	@Test
	void detectLanguageEn() {
		final String input = "PFJ uses exceptions only to represent cases of fatal, unrecoverable (technical) failures. Such an exception might be intercepted only for purposes of logging and/or graceful shutdown of the application. All other exceptions and their interception are discouraged and avoided as much as possible.\n" +
				"Business exceptions are another case of Special States. For propagation and handling of business-level errors, PFJ uses the Result<T> container. Again, this covers all cases when errors may appear - return values, input parameters, or fields. Practice shows that fields rarely (if ever) need to use this container.\n" +
				"глядеть\n" +
				"оно\n" +
				"сесть\n" +
				"There are no justified cases when business-level exceptions can be used. Interfacing with existing Java libraries and legacy code performed via dedicated wrapping methods. The Result<T> container contains an implementation of these wrapping methods.";

		final Optional<List<Tuple2<String, Float>>> languages = LanguageDetector.getLanguageDetector().detectLanguages(input);
		assertTrue(languages.isPresent());
		assertEquals(languages.get().get(0).getA(), "en");

		final Optional<String> language = LanguageDetector.getLanguageDetector().detectLanguage(input);
		assertTrue(language.isPresent());
		assertEquals(language.get(), "en");
	}

	@Test
	void detectLanguageRu() {
		final String input = "плюсы\n" +
				"Чем выше точка сборки, особенно после уровня 4.1 (примерно с уровня чуть выше середины анахаты), тем человек больше может всяких \"ништячков\" и \"прикольностей\" провернуть с мирозданием.\n" +
				"Скажем так, те вещи, которые делает моя жена, они для большинства - из области полнейшей фантастики и \"не верю\".\n" +
				"Да, в общем-то и я, при том, что сейчас у меня более чем странный период, могу без усилий скажем, разогнать очередь, пробку, найти нужный мне товар по вменяемой цене.\n" +
				"\n" +
				"минусы\n" +
				"просто банальная невозможность есть мясо (птицу и рыбу) без серьёзных последствий.\n" +
				"Я как-то с пятницы по вечер понедельника оказался в полной отключке головы ... а всего-то - несколько кусочков куриного холодца, немного солёного лосося.\n" +
				"1. При уровне сознания ниже анахата-чакры (варна: воин, торговец, рабочий) нет особых проблем c поеданием мяса и существенным влиянием на сознание.\n" +
				"2. Как только чел начинает оперировать на уровне анахаты и выше, чем выше забирается, тем проблематичнее есть то, что \"думать умеет\" - голова начинает раскалываться.\n" +
				"\n" +
				"Потеря друзей. Старые \"отваливаюся\" напрочь (почти) все. Да, новые тоже набираются, но сложно и не быстро. Точнее, невероятно медленно... и с ними уже не потусить, как с теми, кто отвалился \"по дороге\".\n" +
				"С родителями, родственниками и старыми друзьями - они вроде как всё ещё интересуются, но уже совсем чужие люди. Это очень хорошо описано в Гарри Поттер и Методы Рациональности, когда Гарри возвращается из Хогвартса и встречает родителей на вокзале. /если ещё не читали - очень рекомендую, в отличие от детской ГП, это совсем не для детей/ С каждым витком осознавания всякого, узнавания всякого \"странного\", всё больше начинаю себя ощущать как Гарри Поттер из HPMOR в самом начале главы №36 https://hpmor.ru/book/1/36/ или http://www.hpmor.com/chapter/36. И ведь не рассказать никому, кроме... ну, разве что, жены (которая и так всё знает и посему ей не будет особо синтересно :)). Нет, мы не живём в \"нулевом\"... но уже очень многие штуки у нас эээ ... по другому и совсем не понятно для тех, кто не в теме.\n" +
				"\n" +
				"And then there was the other reason for the tight feeling in his chest. His parents didn't know. They didn't know anything. They didn't know...\n" +
				"для полных социофобушков лучше жить в лесу.... превед медвед, баба Яга и т.д.\n" +
				"Так как города ОЧЕНЬ давят.\n" +
				"А центры больших городов с массой электронных коммуникаций - так и вообще трындец.\n" +
				"\n" +
				"\n" +
				"для ещё не совсем забывших как ходить по магазинам и кафешкам, желательно жить совсем рядом с городом, где много людей с высокой Т.С.: Киев, Прага, Санкт Петербург, Вена, Вильнюс (где тусит Макс Фрай :)), ...\n" +
				"Не в странах и городах, где общий \"фон\"... эээ ... максимум начала троечки, и не в Нетании, где ещё ниже.\n" +
				"\n" +
				"Так как иначе\n" +
				"\n" +
				"не с кем чаю выпить/в кафе сходить (что-то крепче - не можем уже),\n" +
				"даже на день рождения никто не приедет, и подарков никто не подарит... как минимум, потому, что мало тех, кого мы можем \"терпеть\" рядом с собой дольше получаса... вот все и отваливаются \uD83D\uDE42\n" +
				"никто не придёт, даже когда полнейший мрак и хочется перестать продолжать пыхтеть,\n" +
				"не к кому зайти, когда хочется с кем-то поделиться чем-то хорошим,\n" +
				"Потеря интереса к тому, чем занимался ранее по работе.\n" +
				"Повышая ТС оказываешься выпихнутым из социума ... и оказывается, что если не нашёл какую-то нишу не связанную с творчеством и т.п., работать на \"манипурной\" работе (не говоря уже о более нижнекастовых) вися на .. даже на начале анахаты очень не просто и невероятно истощает.\n" +
				"То, что ранее радовало, давало не мало денег и возможностей, вдруг оказывается не только не интересным, но примерно как запихивать себя в свой старый детский не особо чистый подгузник, выпавший из машины времени, благоухая и \"радуя глаз\".\n" +
				"- есть знания и умения?\n" +
				"Да, конечно!\n" +
				"- можешь?\n" +
				"Да, несомненно.\n" +
				"- сколько продержишься?\n" +
				"Ой, не долго, сблевану-ж быстро....\n" +
				"\n" +
				"а HRы всё так же продолжают бомбить \"подгузниками\", пока не переключился на чтото другое, не создал (вовремя) компанию и так далее.";

		final Optional<List<Tuple2<String, Float>>> languages = LanguageDetector.getLanguageDetector().detectLanguages(input);
		assertTrue(languages.isPresent());
		assertEquals(languages.get().get(0).getA(), "ru");

		final Optional<String> language = LanguageDetector.getLanguageDetector().detectLanguage(input);
		assertTrue(language.isPresent());
		assertEquals(language.get(), "ru");
	}

	@Test
	void detectLanguageUa() {
		final String input = "У фінському місті Тампере 12 січня розпочнуться випробування безпілотних мікроавтобусів з метою з'ясувати, чи можна у майбутньому використовувати роботизовані автомобілі як громадський транспорт.\n" +
				"Як повідомляє Укрінформ, про це повідомляє Yle.\n" +
				"\n" +
				"Автобуси-роботи компанії Nysse перевозитимуть пасажирів за маршрутом завдовжки три з половиною кілометри між кількома районами Тампере.\n" +
				"\n" +
				"«У машині встановлені лазерні радари. На екран виводиться зображення необроблених даних, яке дає змогу лазерному радару бачити навколишню обстановку. Ця сама система використовується для визначення місцезнаходження автомобіля», – повідомив Юссі Суомела з компанії Sensible 4.\n" +
				"\n" +
				"Сам безпілотний автобус рухається зі швидкістю 30 кілометрів на годину.\n" +
				"\n" +
				"Зазначається, що у міста Тампере прописані стратегічні цілі розвитку \"розумного міста\", й експеримент спрямований на збір інформації про те, як роботизовані автобуси могли б показати себе одним із гравців громадського транспорту.\n" +
				"\n" +
				"За словами Суомела, майбутнє автоматизованих автобусів залежатиме від їхньої економічної ефективності.\n" +
				"\n" +
				"Як повідомляв Укрінформ, на заході Японії почалася комерційна експлуатація дворежимних транспортних засобів (DMV), які здатні пересуватися як по автодорогах, так і по рейках.";

		final Optional<List<Tuple2<String, Float>>> languages = LanguageDetector.getLanguageDetector().detectLanguages(input);
		assertTrue(languages.isPresent());
		assertEquals(languages.get().get(0).getA(), "ua");
		final Optional<String> language = LanguageDetector.getLanguageDetector().detectLanguage(input);
		assertTrue(language.isPresent());
		assertEquals(language.get(), "ua");
	}
}