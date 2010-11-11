/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package fixengine.messages.fix42.bats.europe;

import fixengine.messages.Formattable;
import fixengine.messages.InvalidValueForTagException;

public enum CentralCounterpartyValue implements Formattable {
    EMCF("EMCF"), /* European Multilateral Clearing Facility */
    LCHL("LCHL"), /* LCH.CLearnet */
    XCLR("XCLR"); /* SIX x-clear */

    private String value;

    CentralCounterpartyValue(String value) {
        this.value = value;
    }

    @Override public String value() {
        return value;
    }

    public static CentralCounterpartyValue parse(String value) {
        for (CentralCounterpartyValue type : CentralCounterpartyValue.values()) {
            if (type.value.equals(value))
                return type;
        }
        throw new InvalidValueForTagException(value);
    }
}