/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;

/**
 * JCache APIを提供するキャッシュの設定。 アプリケーションで使用するキャッシュを作成し、 JMX経由でアクセス可能な統計情報を有効化します。
 */
@Configuration(proxyBeanMethods = false)
@EnableCaching
class CacheConfiguration {

	/**
	 * 獣医情報用のキャッシュを作成するカスタマイザー。
	 * @return JCacheマネージャーカスタマイザー
	 */
	@Bean
	public JCacheManagerCustomizer petclinicCacheConfigurationCustomizer() {
		return cm -> cm.createCache("vets", cacheConfiguration());
	}

	/**
	 * JCacheプログラム設定API経由で統計情報を有効化するシンプルな設定を作成します。
	 * <p>
	 * JCache API標準によって提供される設定オブジェクト内では、 非常に限られた設定オプションしかありません。 本当に関連する設定オプション（サイズ制限など）は、
	 * 選択したJCache実装が提供する設定メカニズム経由で設定する必要があります。
	 * @return キャッシュ設定
	 */
	private javax.cache.configuration.Configuration<Object, Object> cacheConfiguration() {
		return new MutableConfiguration<>().setStatisticsEnabled(true);
	}

}
