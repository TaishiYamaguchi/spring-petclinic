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
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * ペットを表すドメインオブジェクト。
 * 名前、生年月日、種類、および診察記録の履歴を保持します。
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Wick Dynex
 */
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id")
	@OrderBy("date ASC")
	private final Set<Visit> visits = new LinkedHashSet<>();

	/**
	 * 生年月日を設定します。
	 * 
	 * @param birthDate 生年月日
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * 生年月日を取得します。
	 * 
	 * @return 生年月日
	 */
	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	/**
	 * ペットの種類を取得します。
	 * 
	 * @return ペットの種類（例: 猫、犬、ハムスターなど）
	 */
	public PetType getType() {
		return this.type;
	}

	/**
	 * ペットの種類を設定します。
	 * 
	 * @param type ペットの種類
	 */
	public void setType(PetType type) {
		this.type = type;
	}

	/**
	 * このペットの診察記録一覧を取得します。
	 * 
	 * @return 診察記録のコレクション
	 */
	public Collection<Visit> getVisits() {
		return this.visits;
	}

	/**
	 * このペットに診察記録を追加します。
	 * 
	 * @param visit 追加する診察記録
	 */
	public void addVisit(Visit visit) {
		getVisits().add(visit);
	}

}
