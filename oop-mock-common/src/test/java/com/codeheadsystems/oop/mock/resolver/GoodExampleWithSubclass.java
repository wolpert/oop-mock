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

package com.codeheadsystems.oop.mock.resolver;

import com.codeheadsystems.oop.mock.model.MockedData;
import java.util.Optional;
import javax.inject.Inject;

/**
 * The type Good example with subclass.
 */
public class GoodExampleWithSubclass implements MockDataResolver {

  /**
   * Instantiates a new Good example with subclass.
   *
   * @param subclas the subclas
   */
  @Inject
  public GoodExampleWithSubclass(final GoodExampleWithArg subclas) {

  }

  @Override
  public Optional<MockedData> resolve(final String namespace, final String lookup, final String discriminator) {
    return Optional.empty();
  }
}
