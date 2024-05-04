/*
 *    Copyright (c) 2022 Ned Wolpert <ned.wolpert@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.codeheadsystems.oop.dao.ddb.converter;

import com.codeheadsystems.oop.dao.ddb.model.DdbEntry;
import com.codeheadsystems.oop.mock.Hasher;
import com.codeheadsystems.oop.mock.converter.JsonConverter;
import com.codeheadsystems.oop.mock.model.MockedData;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Ddb entry converter.
 */
@Singleton
public class DdbEntryConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DdbEntryConverter.class);

  private final Hasher hasher;
  private final JsonConverter jsonConverter;

  /**
   * Instantiates a new Ddb entry converter.
   *
   * @param hasher        the hasher
   * @param jsonConverter the json converter
   */
  @Inject
  public DdbEntryConverter(final Hasher hasher,
                           final JsonConverter jsonConverter) {
    this.hasher = hasher;
    this.jsonConverter = jsonConverter;
    LOGGER.info("DdbEntryConverter({},{})", hasher, jsonConverter);
  }

  /**
   * Convert ddb entry.
   *
   * @param namespace     the namespace
   * @param lookup        the lookup
   * @param discriminator the discriminator
   * @param data          the data
   * @return the ddb entry
   */
  public DdbEntry convert(final String namespace,
                          final String lookup,
                          final String discriminator,
                          final MockedData data) {
    final DdbEntry entry = convert(namespace, lookup, discriminator);
    final String json = jsonConverter.toJson(data);
    entry.setMockData(json);
    return entry;
  }

  /**
   * Convert ddb entry.
   *
   * @param namespace     the namespace
   * @param lookup        the lookup
   * @param discriminator the discriminator
   * @return the ddb entry
   */
  public DdbEntry convert(final String namespace,
                          final String lookup,
                          final String discriminator) {
    final String secondary = hasher.hash(lookup, discriminator);
    return new DdbEntry(namespace, secondary);
  }

  /**
   * To mocked data optional.
   *
   * @param entry the entry
   * @return the optional
   */
  public Optional<MockedData> toMockedData(final DdbEntry entry) {
    if (entry.getMockData() == null) {
      return Optional.empty();
    } else {
      return Optional.of(jsonConverter.convert(entry.getMockData(), MockedData.class));
    }
  }

}
