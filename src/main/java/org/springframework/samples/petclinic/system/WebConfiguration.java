package org.springframework.samples.petclinic.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * アプリケーションの国際化（i18n）サポートを設定します。
 *
 * <p>
 * 言語固有のメッセージの読み込み、ユーザーの言語追跡、 URLパラメータ経由の言語変更（例: <code>?lang=de</code>）を処理します。
 * </p>
 *
 * @author Anuj Ashok Potdar
 */
@Configuration
@SuppressWarnings("unused")
public class WebConfiguration implements WebMvcConfigurer {

	/**
	 * リクエスト間でユーザーの言語設定を記憶するためにセッションストレージを使用します。 何も指定されていない場合は英語がデフォルトになります。
	 * @return セッションベースのLocaleResolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	/**
	 * <code>?lang=es</code>のようなURLパラメータを使用して言語を切り替えることを可能にします。
	 * @return 言語変更を処理するLocaleChangeInterceptor
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	/**
	 * 各リクエストで実行されるようにロケール変更インターセプターを登録します。
	 * @param registry インターセプターを追加するレジストリ
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}
