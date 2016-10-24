/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.engine.impl.batch.job;

import org.camunda.bpm.engine.impl.batch.BatchConfiguration;

import java.util.List;

/**
 * @author Askar Akhmerov
 */
public class SetJobRetriesBatchConfiguration extends BatchConfiguration {

  protected int retries;

  public SetJobRetriesBatchConfiguration(List<String> ids, int retries) {
    super(ids);
    this.retries = retries;
  }

  public SetJobRetriesBatchConfiguration(List<String> ids) {
    super(ids);
  }

  public int getRetries() {
    return retries;
  }

  public void setRetries(int retries) {
    this.retries = retries;
  }
}
