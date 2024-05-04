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

import static org.assertj.core.api.Assertions.assertThat;

import com.codeheadsystems.oop.ImmutableOopMockConfiguration;
import com.codeheadsystems.oop.OopMockConfiguration;
import com.codeheadsystems.oop.OopMockFactory;
import com.codeheadsystems.oop.ResolverConfiguration;
import com.codeheadsystems.oop.client.OopMockClientFactory;
import com.codeheadsystems.oop.client.dagger.DaggerOopMockClientFactoryBuilder;
import com.codeheadsystems.oop.dagger.DaggerOopMockFactoryBuilder;
import com.codeheadsystems.oop.mock.dagger.OopConfigurationModule;
import com.codeheadsystems.oop.mock.dagger.ResolverModule;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Full dao test.
 */
public abstract class FullDaoTest {

  /**
   * The constant ID.
   */
  protected static final String ID = "id";

  /**
   * The Oop mock client factory.
   */
  protected OopMockClientFactory oopMockClientFactory;
  /**
   * The Oop mock factory.
   */
  protected OopMockFactory oopMockFactory;
  /**
   * The Client.
   */
  protected Client client;
  /**
   * The Server.
   */
  protected Server server;

  /**
   * The resolver configuration you expect clients to use in their configuration file.
   *
   * @return the resolver configuration
   */
  protected abstract ResolverConfiguration resolverConfiguration();

  /**
   * These are optional dependencies if your resolver dao needs them for construction.
   * If this is not null/empty, you need to document what is expected here from users,
   * as this requires coding changes by the users.
   *
   * @return the map
   */
  protected abstract Map<Class<?>, Object> resolverDeps();

  /**
   * Sets up oop mock.
   */
  @BeforeEach
  public void setUpOopMock() {
    final OopMockConfiguration oopMockConfiguration = ImmutableOopMockConfiguration.builder()
        .enabled(true)
        .delayResponseEnabled(false)
        .resolverConfiguration(resolverConfiguration())
        .build();
    oopMockClientFactory = DaggerOopMockClientFactoryBuilder.builder()
        .resolverConfigModule(new ResolverModule.ResolverConfigModule(resolverDeps()))
        .oopConfigurationModule(new OopConfigurationModule(oopMockConfiguration))
        .build().factory();
    oopMockFactory = DaggerOopMockFactoryBuilder.builder()
        .resolverConfigModule(new ResolverModule.ResolverConfigModule(resolverDeps()))
        .oopConfigurationModule(new OopConfigurationModule(oopMockConfiguration))
        .build().factory();
    server = new Server(oopMockFactory);
    client = new Client(oopMockClientFactory);
  }

  /**
   * Call without mock.
   */
  @Test
  public void callWithoutMock() {
    assertThat(client.callServerWithoutMock(server, ID))
        .isNotNull()
        .isEqualTo(Server.BASE_RESULT);
  }

  /**
   * Call with different id mocked.
   */
  @Test
  public void callWithDifferentIdMocked() {
    assertThat(client.callServerWithMockOnDifferentId(server, ID))
        .isNotNull()
        .isEqualTo(Server.BASE_RESULT);
  }

  /**
   * Call with mock.
   */
  @Test
  public void callWithMock() {
    assertThat(client.callServerMocked(server, ID))
        .isNotNull()
        .isEqualTo(Client.MOCKED_DATA);
  }

}
