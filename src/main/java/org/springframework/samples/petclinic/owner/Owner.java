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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.util.Assert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

/**
 * ペットの飼い主（オーナー）を表すドメインオブジェクト。 住所、都市、電話番号、および複数のペット情報を保持します。
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Oliver Drotbohm
 * @author Wick Dynex
 */
@Entity
@Table(name = "owners")
public class Owner extends Person {

	@Column(name = "address")
	@NotBlank
	private String address;

	@Column(name = "city")
	@NotBlank
	private String city;

	@Column(name = "telephone")
	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "{telephone.invalid}")
	private String telephone;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@OrderBy("name")
	private final List<Pet> pets = new ArrayList<>();

	/**
	 * 住所を取得します。
	 * @return 住所
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * 住所を設定します。
	 * @param address 住所
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 都市名を取得します。
	 * @return 都市名
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * 都市名を設定します。
	 * @param city 都市名
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 電話番号を取得します。
	 * @return 電話番号
	 */
	public String getTelephone() {
		return this.telephone;
	}

	/**
	 * 電話番号を設定します。
	 * @param telephone 電話番号（10桁の数字）
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * このオーナーが所有するペットのリストを取得します。
	 * @return ペットのリスト
	 */
	public List<Pet> getPets() {
		return this.pets;
	}

	/**
	 * 新しいペットをこのオーナーに追加します。 新規ペット（IDが未設定）の場合のみ追加します。
	 * @param pet 追加するペット
	 */
	public void addPet(Pet pet) {
		if (pet.isNew()) {
			getPets().add(pet);
		}
	}

	/**
	 * 指定された名前のペットを返します。 見つからない場合はnullを返します。
	 * @param name 検索するペットの名前
	 * @return 指定された名前のペット、またはnull
	 */
	public Pet getPet(String name) {
		return getPet(name, false);
	}

	/**
	 * 指定されたIDのペットを返します。 見つからない場合はnullを返します。
	 * @param id 検索するペットのID
	 * @return 指定されたIDのペット、またはnull
	 */
	public Pet getPet(Integer id) {
		for (Pet pet : getPets()) {
			if (!pet.isNew()) {
				Integer compId = pet.getId();
				if (Objects.equals(compId, id)) {
					return pet;
				}
			}
		}
		return null;
	}

	/**
	 * 指定された名前のペットを返します。 見つからない場合はnullを返します。
	 * @param name 検索するペットの名前
	 * @param ignoreNew 新規ペット（まだ保存されていないペット）を無視するかどうか
	 * @return 指定された名前のペット、またはnull
	 */
	public Pet getPet(String name, boolean ignoreNew) {
		for (Pet pet : getPets()) {
			String compName = pet.getName();
			if (compName != null && compName.equalsIgnoreCase(name)) {
				if (!ignoreNew || !pet.isNew()) {
					return pet;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId())
			.append("new", this.isNew())
			.append("lastName", this.getLastName())
			.append("firstName", this.getFirstName())
			.append("address", this.address)
			.append("city", this.city)
			.append("telephone", this.telephone)
			.toString();
	}

	/**
	 * 指定されたペットに診察記録を追加します。
	 * @param petId ペットの識別子、null不可
	 * @param visit 追加する診察記録、null不可
	 * @throws IllegalArgumentException petIdまたはvisitがnullの場合、または無効なペットIDの場合
	 */
	public void addVisit(Integer petId, Visit visit) {

		Assert.notNull(petId, "Pet identifier must not be null!");
		Assert.notNull(visit, "Visit must not be null!");

		Pet pet = getPet(petId);

		Assert.notNull(pet, "Invalid Pet identifier!");

		pet.addVisit(visit);
	}

}
