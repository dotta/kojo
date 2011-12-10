/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo.inspect;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.Node.Property;

public class TableColumnProperty extends Property<String> {

    String name;

    public TableColumnProperty(String name) {
        super(String.class);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return "someValue";
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public void setValue(String t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
