/*
 * Copyright 2009 the original author or authors.
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
package fixengine.messages;

import fixengine.Specification;

/**
 * @author Pekka Enberg 
 */
public class RepeatingGroupInstance {
    private Fields fields = new Fields();

    public void add(Field field) {
        fields.add(field);
    }

    public void parse(TokenStream stream) {
        while (true) {
            Tag tag = stream.tag();
            if (tag == null) {
                break;
            }
            Field field = fields.lookup(tag);
            if (field == null || field.isParsed()) {
                stream.push(tag.toString());
                break;
            }
            field.parse(stream);
        }
    }

    public String format() {
        return fields.format();
    }

    public boolean contains(Specification<Field> spec) {
        return fields.contains(spec);
    }

    public int checksum() {
        return fields.checksum();
    }

    public int length() {
        return fields.length();
    }
}