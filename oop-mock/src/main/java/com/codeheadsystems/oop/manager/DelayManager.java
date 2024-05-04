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

package com.codeheadsystems.oop.manager;

import com.codeheadsystems.oop.OopMockConfiguration;
import java.time.Clock;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Delay manager.
 */
@Singleton
public class DelayManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(DelayManager.class);

  private final boolean delayEnabled;
  private final long maxDelayTimeMs;
  private final Clock clock;
  private final SleeperManager sleeper;

  /**
   * Instantiates a new Delay manager.
   *
   * @param configuration the configuration
   * @param clock         the clock
   * @param sleeper       the sleeper
   */
  @Inject
  public DelayManager(final OopMockConfiguration configuration,
                      final Clock clock,
                      final SleeperManager sleeper) {
    this.sleeper = sleeper;
    this.delayEnabled = configuration.delayResponseEnabled();
    this.clock = clock;
    this.maxDelayTimeMs = configuration.maxDelayTimeMs();
    LOGGER.info("DelayManager({},{},{})", delayEnabled, maxDelayTimeMs, clock);
  }

  /**
   * Start millis long.
   *
   * @return the long
   */
  public long startMillis() {
    return clock.millis();
  }

  /**
   * Delay.
   *
   * @param start the start
   * @param delay the delay
   */
  public void delay(final long start, final long delay) {
    if (delayEnabled) {
      LOGGER.debug("delay({},{}", start, delay);
      final long timeSpentSoFar = Math.max(0, clock.millis() - start); // at least zero.
      final long waitInMills = delay - timeSpentSoFar;
      // we use the min time for waiting, calculated or max wait time. Then the result must be greater than zero.
      final long actualWaitTime = Math.max(0L, Math.min(waitInMills, maxDelayTimeMs));
      LOGGER.debug("timeSpentSoFar:{}, waitInMills:{}, actualWaitTime:{}", timeSpentSoFar, waitInMills, actualWaitTime);
      sleeper.sleep(actualWaitTime);
    }
  }

}
