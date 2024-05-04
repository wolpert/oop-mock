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

package com.codeheadsystems.oop.client;

import com.codeheadsystems.oop.client.dao.MockDataDao;
import com.codeheadsystems.oop.mock.Hasher;
import com.codeheadsystems.oop.mock.model.MockedData;
import com.codeheadsystems.oop.mock.translator.Translator;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Oop mock client.
 */
public class OopMockClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(OopMockClient.class);

  private final MockDataDao dao;
  private final Translator translator;
  private final String namespace;

  /**
   * Instantiates a new Oop mock client.
   *
   * @param clazz      the clazz
   * @param hasher     the hasher
   * @param dao        the dao
   * @param translator the translator
   */
  @AssistedInject
  public OopMockClient(@Assisted final Class<?> clazz,
                       final Hasher hasher,
                       final MockDataDao dao,
                       final Translator translator) {
    LOGGER.info("OopMockClient({})", clazz);
    this.dao = dao;
    this.namespace = hasher.namespace(clazz);
    this.translator = translator;
  }

  /**
   * Mock setup.
   *
   * @param <R>      the type parameter
   * @param mockData the mock data
   * @param lookup   the lookup
   * @param id       the id
   */
  public <R> void mockSetup(final R mockData,
                            final String lookup,
                            final String id) {
    LOGGER.info("mockSetup({},{})", lookup, id);
    final MockedData storedMockData = translator.marshal(mockData);
    dao.store(namespace, lookup, id, storedMockData);
  }

  /**
   * Delete mock.
   *
   * @param lookup the lookup
   * @param id     the id
   */
  public void deleteMock(final String lookup,
                         final String id) {
    LOGGER.info("mockDelete({},{})", lookup, id);
    dao.delete(namespace, lookup, id);
  }

}
