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

import java.awt.BorderLayout;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.TreeTableView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.windows.TopComponent;

public class InspectorTopComponent extends TopComponent
        implements ExplorerManager.Provider {

    private final ExplorerManager manager = new ExplorerManager();

    public InspectorTopComponent() {
        setDisplayName("Object Inspector");
        setLayout(new BorderLayout());
        TreeTableView view = new TreeTableView();

        // specify properties that we want to see in Table
        Property p1 = new TableColumnProperty("type");
        Property p2 = new TableColumnProperty("id");
        Property p3 = new TableColumnProperty("value");

        view.setProperties(new Property[]{p1, p2, p3});

        view.setRootVisible(false);
        add(view, BorderLayout.CENTER);
    }

    public void inspectObject(String name, Object obj) {
        setDisplayName("Object Inspector - " + name);
//        manager.setRootContext(new ObjectInspectorNode(name, obj));

        ObjectInspectorNode realRoot = new ObjectInspectorNode(name, obj);
        Node[] arr = new Node[]{realRoot};
        Children.Array carr = new Children.Array();
        carr.add(arr);
        manager.setRootContext(new AbstractNode(carr));
    }

    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }
}

