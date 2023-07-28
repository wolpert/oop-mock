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

package com.codeheadsystems.oop.test;

import com.codeheadsystems.oop.OopMock;
import com.codeheadsystems.oop.OopMockFactory;

/**
 * The type Server.
 */
public class Server {
  /**
   * The constant BASE_RESULT.
   */
  public static final String BASE_RESULT = "This is from the server";
  /**
   * The constant LOOKUP.
   */
  public static final String LOOKUP = "getBaseResult";

  private final OopMockFactory factory;

  /**
   * Instantiates a new Server.
   *
   * @param factory the factory
   */
  public Server(final OopMockFactory factory) {
    this.factory = factory;
  }


  /**
   * Gets base result.
   *
   * @param id the id
   * @return the base result
   */
  public String getBaseResult(String id) {
    final OopMock oopMock = factory.generate(Server.class);
    return oopMock.proxy(String.class, this::getBaseResult, LOOKUP, id);
  }

  private String getBaseResult() {
    return BASE_RESULT;
  }

}
